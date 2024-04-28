package net.lab1024.sa.admin.module.system.login.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.extra.servlet.ServletUtil;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.sa.admin.module.system.department.service.DepartmentService;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.sa.admin.module.system.employee.service.EmployeeService;
import net.lab1024.sa.admin.module.system.login.domain.LoginForm;
import net.lab1024.sa.admin.module.system.login.domain.LoginResultVO;
import net.lab1024.sa.admin.module.system.login.domain.RequestEmployee;
import net.lab1024.sa.admin.module.system.menu.domain.vo.MenuVO;
import net.lab1024.sa.admin.module.system.role.domain.vo.RoleVO;
import net.lab1024.sa.admin.module.system.role.service.RoleEmployeeService;
import net.lab1024.sa.admin.module.system.role.service.RoleMenuService;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.constant.RequestHeaderConst;
import net.lab1024.sa.base.common.constant.StringConst;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.UserPermission;
import net.lab1024.sa.base.common.enumeration.UserTypeEnum;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartEnumUtil;
import net.lab1024.sa.base.common.util.SmartIpUtil;
import net.lab1024.sa.base.constant.LoginDeviceEnum;
import net.lab1024.sa.base.module.support.captcha.CaptchaService;
import net.lab1024.sa.base.module.support.captcha.domain.CaptchaVO;
import net.lab1024.sa.base.module.support.config.ConfigKeyEnum;
import net.lab1024.sa.base.module.support.config.ConfigService;
import net.lab1024.sa.base.module.support.loginlog.LoginLogResultEnum;
import net.lab1024.sa.base.module.support.loginlog.LoginLogService;
import net.lab1024.sa.base.module.support.loginlog.domain.LoginLogEntity;
import net.lab1024.sa.base.module.support.loginlog.domain.LoginLogVO;
import net.lab1024.sa.base.module.support.securityprotect.domain.LoginFailEntity;
import net.lab1024.sa.base.module.support.securityprotect.service.ProtectLoginService;
import net.lab1024.sa.base.module.support.securityprotect.service.ProtectPasswordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 登录
 *
 * @Author 1024创新实验室: 开云
 * @Date 2021-12-01 22:56:34
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class LoginService implements StpInterface {

    /**
     * 万能密码的 sa token loginId 前缀
     */
    private static final String SUPER_PASSWORD_LOGIN_ID_PREFIX = "S";

    /**
     * 最大在线缓存人数
     */
    private static final long CACHE_MAX_ONLINE_PERSON_COUNT = 1000L;

    /**
     * 登录信息二级缓存
     */
    private final ConcurrentMap<Long, RequestEmployee> loginEmployeeCache = new ConcurrentLinkedHashMap.Builder<Long, RequestEmployee>().maximumWeightedCapacity(CACHE_MAX_ONLINE_PERSON_COUNT).build();


    /**
     * 权限 缓存
     */
    private final ConcurrentMap<Long, UserPermission> permissionCache = new ConcurrentLinkedHashMap.Builder<Long, UserPermission>().maximumWeightedCapacity(CACHE_MAX_ONLINE_PERSON_COUNT).build();

    @Resource
    private EmployeeService employeeService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private ConfigService configService;

    @Resource
    private LoginLogService loginLogService;

    @Resource
    private RoleEmployeeService roleEmployeeService;

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private ProtectLoginService protectLoginService;

    @Resource
    private ProtectPasswordService profectPasswordService;

    /**
     * 获取验证码
     */
    public ResponseDTO<CaptchaVO> getCaptcha() {
        return ResponseDTO.ok(captchaService.generateCaptcha());
    }

    /**
     * 员工登陆
     *
     * @return 返回用户登录信息
     */
    public ResponseDTO<LoginResultVO> login(LoginForm loginForm, String ip, String userAgent) {

        LoginDeviceEnum loginDeviceEnum = SmartEnumUtil.getEnumByValue(loginForm.getLoginDevice(), LoginDeviceEnum.class);
        if (loginDeviceEnum == null) {
            return ResponseDTO.userErrorParam("登录设备暂不支持！");
        }

        // 校验 图形验证码
        ResponseDTO<String> checkCaptcha = captchaService.checkCaptcha(loginForm);
        if (!checkCaptcha.getOk()) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, checkCaptcha.getMsg());
        }

        // 验证登录名
        EmployeeEntity employeeEntity = employeeService.getByLoginName(loginForm.getLoginName());
        if (null == employeeEntity) {
            return ResponseDTO.userErrorParam("登录名不存在！");
        }

        // 验证账号状态
        if (employeeEntity.getDisabledFlag()) {
            saveLoginLog(employeeEntity, ip, userAgent, "账号已禁用", LoginLogResultEnum.LOGIN_FAIL);
            return ResponseDTO.userErrorParam("您的账号已被禁用,请联系工作人员！");
        }

        // 解密前端加密的密码
        String requestPassword = profectPasswordService.decryptPassword(loginForm.getPassword());

        // 验证密码 是否为万能密码
        String superPassword = configService.getConfigValue(ConfigKeyEnum.SUPER_PASSWORD);
        boolean superPasswordFlag = superPassword.equals(requestPassword);

        // 万能密码特殊操作
        if (superPasswordFlag) {

            // 对于万能密码：受限制sa token 要求loginId唯一，万能密码只能插入一段uuid
            String saTokenLoginId = SUPER_PASSWORD_LOGIN_ID_PREFIX + StringConst.COLON + UUID.randomUUID().toString().replace("-", "") + StringConst.COLON + employeeEntity.getEmployeeId();
            // 万能密码登录只能登录30分钟
            StpUtil.login(saTokenLoginId, 1800);

        } else {

            // 按照等保登录要求，进行登录失败次数校验
            ResponseDTO<LoginFailEntity> loginFailEntityResponseDTO = protectLoginService.checkLogin(employeeEntity.getEmployeeId(), UserTypeEnum.ADMIN_EMPLOYEE);
            if (!loginFailEntityResponseDTO.getOk()) {
                return ResponseDTO.error(loginFailEntityResponseDTO);
            }

            // 密码错误
            if (!employeeEntity.getLoginPwd().equals(EmployeeService.getEncryptPwd(requestPassword))) {
                // 记录登录失败
                saveLoginLog(employeeEntity, ip, userAgent, "密码错误", LoginLogResultEnum.LOGIN_FAIL);
                // 记录等级保护次数
                String msg = protectLoginService.recordLoginFail(employeeEntity.getEmployeeId(), UserTypeEnum.ADMIN_EMPLOYEE, employeeEntity.getLoginName(), loginFailEntityResponseDTO.getData());
                return msg == null ? ResponseDTO.userErrorParam("登录名或密码错误！") : ResponseDTO.error(UserErrorCode.LOGIN_FAIL_WILL_LOCK, msg);
            }

            String saTokenLoginId = UserTypeEnum.ADMIN_EMPLOYEE.getValue() + StringConst.COLON + employeeEntity.getEmployeeId();
            // 登录
            StpUtil.login(saTokenLoginId, String.valueOf(loginDeviceEnum.getDesc()));
        }

        // 获取员工信息
        RequestEmployee requestEmployee = loadLoginInfo(employeeEntity);

        // 放入缓存
        loginEmployeeCache.put(employeeEntity.getEmployeeId(), requestEmployee);

        // 移除登录失败
        protectLoginService.removeLoginFail(employeeEntity.getEmployeeId(), UserTypeEnum.ADMIN_EMPLOYEE);

        // 获取登录结果信息
        LoginResultVO loginResultVO = getLoginResult(requestEmployee);

        //保存登录记录
        saveLoginLog(employeeEntity, ip, userAgent, superPasswordFlag ? "万能密码登录" : loginDeviceEnum.getDesc(), LoginLogResultEnum.LOGIN_SUCCESS);

        // 设置 token
        loginResultVO.setToken(StpUtil.getTokenValue());

        // 清除权限缓存
        permissionCache.remove(employeeEntity.getEmployeeId());

        return ResponseDTO.ok(loginResultVO);
    }


    /**
     * 获取登录结果信息
     */
    public LoginResultVO getLoginResult(RequestEmployee requestEmployee) {

        // 基础信息
        LoginResultVO loginResultVO = SmartBeanUtil.copy(requestEmployee, LoginResultVO.class);

        // 前端菜单和功能点清单
        List<RoleVO> roleList = roleEmployeeService.getRoleIdList(requestEmployee.getEmployeeId());
        List<MenuVO> menuAndPointsList = roleMenuService.getMenuList(roleList.stream().map(RoleVO::getRoleId).collect(Collectors.toList()), requestEmployee.getAdministratorFlag());
        loginResultVO.setMenuList(menuAndPointsList);

        // 更新下后端权限缓存
        UserPermission userPermission = getUserPermission(requestEmployee.getUserId());
        permissionCache.put(requestEmployee.getUserId(), userPermission);

        // 上次登录信息
        LoginLogVO loginLogVO = loginLogService.queryLastByUserId(requestEmployee.getEmployeeId(), UserTypeEnum.ADMIN_EMPLOYEE, LoginLogResultEnum.LOGIN_SUCCESS);
        if (loginLogVO != null) {
            loginResultVO.setLastLoginIp(loginLogVO.getLoginIp());
            loginResultVO.setLastLoginIpRegion(loginLogVO.getLoginIpRegion());
            loginResultVO.setLastLoginTime(loginLogVO.getCreateTime());
            loginResultVO.setLastLoginUserAgent(loginLogVO.getUserAgent());
        }

        return loginResultVO;
    }


    /**
     * 获取登录的用户信息
     */
    private RequestEmployee loadLoginInfo(EmployeeEntity employeeEntity) {

        // 基础信息
        RequestEmployee requestEmployee = SmartBeanUtil.copy(employeeEntity, RequestEmployee.class);
        requestEmployee.setUserType(UserTypeEnum.ADMIN_EMPLOYEE);

        // 部门信息
        DepartmentVO department = departmentService.getDepartmentById(employeeEntity.getDepartmentId());
        requestEmployee.setDepartmentName(null == department ? StringConst.EMPTY : department.getName());

        return requestEmployee;
    }


    /**
     * 根据登陆token 获取员请求工信息
     */
    public RequestEmployee getLoginEmployee(String loginId, HttpServletRequest request) {
        if (loginId == null) {
            return null;
        }

        Long requestEmployeeId = getEmployeeIdByLoginId(loginId);
        if (requestEmployeeId == null) {
            return null;
        }

        RequestEmployee requestEmployee = loginEmployeeCache.get(requestEmployeeId);
        if (requestEmployee == null) {
            // 员工基本信息
            EmployeeEntity employeeEntity = employeeService.getById(requestEmployeeId);
            if (employeeEntity == null) {
                return null;
            }

            requestEmployee = this.loadLoginInfo(employeeEntity);
            loginEmployeeCache.put(requestEmployeeId, requestEmployee);
        }

        // 更新请求ip和user agent
        requestEmployee.setUserAgent(ServletUtil.getHeaderIgnoreCase(request, RequestHeaderConst.USER_AGENT));
        requestEmployee.setIp(ServletUtil.getClientIP(request));

        return requestEmployee;
    }

    /**
     * 根据 loginId 获取 员工id
     */
    Long getEmployeeIdByLoginId(String loginId) {

        if (loginId == null) {
            return null;
        }

        try {
            // 如果是 万能密码 登录的用户
            String employeeIdStr = null;
            if (loginId.startsWith(SUPER_PASSWORD_LOGIN_ID_PREFIX)) {
                employeeIdStr = loginId.split(StringConst.COLON)[2];
            } else {
                employeeIdStr = loginId.substring(2);
            }

            return Long.parseLong(employeeIdStr);
        } catch (Exception e) {
            log.error("loginId parse error , loginId : {}", loginId, e);
            return null;
        }
    }


    /**
     * 退出登录
     */
    public ResponseDTO<String> logout(String token, RequestUser requestUser) {

        // sa token 登出
        StpUtil.logoutByTokenValue(token);

        // 清空登录信息缓存
        loginEmployeeCache.remove(requestUser.getUserId());

        //保存登出日志
        LoginLogEntity loginEntity = LoginLogEntity.builder()
                .userId(requestUser.getUserId())
                .userType(requestUser.getUserType().getValue())
                .userName(requestUser.getUserName())
                .userAgent(requestUser.getUserAgent())
                .loginIp(requestUser.getIp())
                .loginIpRegion(SmartIpUtil.getRegion(requestUser.getIp()))
                .loginResult(LoginLogResultEnum.LOGIN_OUT.getValue())
                .createTime(LocalDateTime.now())
                .build();
        loginLogService.log(loginEntity);

        return ResponseDTO.ok();
    }

    /**
     * 保存登录日志
     */
    private void saveLoginLog(EmployeeEntity employeeEntity, String ip, String userAgent, String remark, LoginLogResultEnum result) {
        LoginLogEntity loginEntity = LoginLogEntity.builder()
                .userId(employeeEntity.getEmployeeId())
                .userType(UserTypeEnum.ADMIN_EMPLOYEE.getValue())
                .userName(employeeEntity.getActualName())
                .userAgent(userAgent)
                .loginIp(ip)
                .loginIpRegion(SmartIpUtil.getRegion(ip))
                .remark(remark)
                .loginResult(result.getValue())
                .createTime(LocalDateTime.now())
                .build();
        loginLogService.log(loginEntity);
    }


    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long employeeId = this.getEmployeeIdByLoginId((String) loginId);
        if (employeeId == null) {
            return Collections.emptyList();
        }

        UserPermission userPermission = permissionCache.get(employeeId);
        if (userPermission == null) {
            userPermission = getUserPermission(employeeId);
            permissionCache.put(employeeId, userPermission);
        }

        return userPermission.getPermissionList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long employeeId = this.getEmployeeIdByLoginId((String) loginId);
        if (employeeId == null) {
            return Collections.emptyList();
        }

        UserPermission userPermission = permissionCache.get(employeeId);
        if (userPermission == null) {
            userPermission = getUserPermission(employeeId);
            permissionCache.put(employeeId, userPermission);
        }
        return userPermission.getRoleList();
    }

    /**
     * 获取用户的权限（包含 角色列表、权限列表）
     */
    private UserPermission getUserPermission(Long employeeId) {

        UserPermission userPermission = new UserPermission();
        userPermission.setPermissionList(new ArrayList<>());
        userPermission.setRoleList(new ArrayList<>());

        // 角色列表
        List<RoleVO> roleList = roleEmployeeService.getRoleIdList(employeeId);
        userPermission.getRoleList().addAll(roleList.stream().map(RoleVO::getRoleCode).collect(Collectors.toSet()));

        // 前端菜单和功能点清单
        EmployeeEntity employeeEntity = employeeService.getById(employeeId);

        List<MenuVO> menuAndPointsList = roleMenuService.getMenuList(roleList.stream().map(RoleVO::getRoleId).collect(Collectors.toList()), employeeEntity.getAdministratorFlag());

        // 权限列表
        HashSet<String> permissionSet = new HashSet<>();
        for (MenuVO menu : menuAndPointsList) {
            if (menu.getPermsType() == null) {
                continue;
            }

            String perms = menu.getApiPerms();
            if (StringUtils.isEmpty(perms)) {
                continue;
            }
            //接口权限
            String[] split = perms.split(",");
            permissionSet.addAll(Arrays.asList(split));
        }
        userPermission.getPermissionList().addAll(permissionSet);

        return userPermission;
    }
}
