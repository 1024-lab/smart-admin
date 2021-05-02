package net.lab1024.smartadmin.module.system.login;

import net.lab1024.smartadmin.common.constant.JudgeEnum;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.constant.CommonConst;
import net.lab1024.smartadmin.module.system.department.DepartmentDao;
import net.lab1024.smartadmin.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.smartadmin.module.system.employee.EmployeeDao;
import net.lab1024.smartadmin.module.system.employee.constant.EmployeeResponseCodeConst;
import net.lab1024.smartadmin.module.system.employee.constant.EmployeeStatusEnum;
import net.lab1024.smartadmin.module.system.employee.domain.dto.EmployeeDTO;
import net.lab1024.smartadmin.module.system.employee.domain.dto.EmployeeLoginFormDTO;
import net.lab1024.smartadmin.module.business.log.LogService;
import net.lab1024.smartadmin.module.business.log.userloginlog.domain.UserLoginLogEntity;
import net.lab1024.smartadmin.module.system.login.domain.KaptchaVO;
import net.lab1024.smartadmin.module.system.login.domain.LoginDetailVO;
import net.lab1024.smartadmin.module.system.login.domain.LoginPrivilegeDTO;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import net.lab1024.smartadmin.module.system.privilege.domain.entity.PrivilegeEntity;
import net.lab1024.smartadmin.module.system.privilege.service.PrivilegeEmployeeService;
import net.lab1024.smartadmin.util.SmartBeanUtil;
import net.lab1024.smartadmin.util.SmartDigestUtil;
import net.lab1024.smartadmin.util.SmartIPUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/3/27 0027 下午 18:10
 * @since JDK1.8
 */
@Slf4j
@Service
public class LoginService {

    private static final String VERIFICATION_CODE_REDIS_PREFIX = "vc_%s";

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private PrivilegeEmployeeService privilegeEmployeeService;

    @Autowired
    private LoginTokenService loginTokenService;

    @Autowired
    private LogService logService;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private ValueOperations<String, String> redisValueOperations;

    /**
     * 登陆
     *
     * @param loginForm 登录名 密码
     * @return 登录用户基本信息
     */
    public ResponseDTO<LoginDetailVO> login(@Valid EmployeeLoginFormDTO loginForm, HttpServletRequest request) {
//        String redisVerificationCode = redisValueOperations.get(loginForm.getCodeUuid());
//        //增加删除已使用的验证码方式 频繁登录
//        redisValueOperations.getOperations().delete(loginForm.getCodeUuid());
//        if (StringUtils.isEmpty(redisVerificationCode)) {
//            return ResponseDTO.wrap(EmployeeResponseCodeConst.VERIFICATION_CODE_INVALID);
//        }
//        if (!redisVerificationCode.equalsIgnoreCase(loginForm.getCode())) {
//            return ResponseDTO.wrap(EmployeeResponseCodeConst.VERIFICATION_CODE_INVALID);
//        }
        String loginPwd = SmartDigestUtil.encryptPassword(CommonConst.Password.SALT_FORMAT, loginForm.getLoginPwd());
        EmployeeDTO employeeDTO = employeeDao.login(loginForm.getLoginName(), loginPwd);
        if (null == employeeDTO) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.LOGIN_FAILED);
        }
        if (EmployeeStatusEnum.DISABLED.equalsValue(employeeDTO.getIsDisabled())) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.IS_DISABLED);
        }
        //jwt token赋值
        String compactJws = loginTokenService.generateToken(employeeDTO);

        LoginDetailVO loginDTO = SmartBeanUtil.copy(employeeDTO, LoginDetailVO.class);

        //获取前端功能权限
        loginDTO.setPrivilegeList(initEmployeePrivilege(employeeDTO.getId()));

        loginDTO.setXAccessToken(compactJws);
        DepartmentEntity departmentEntity = departmentDao.selectById(employeeDTO.getDepartmentId());
        loginDTO.setDepartmentName(departmentEntity.getName());

        //判断是否为超管
        Boolean isSuperman = privilegeEmployeeService.isSuperman(loginDTO.getId());
        loginDTO.setIsSuperMan(isSuperman);
        //登陆操作日志
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        UserLoginLogEntity logEntity =
                UserLoginLogEntity.builder()
                        .userId(employeeDTO.getId())
                        .userName(employeeDTO.getActualName())
                        .remoteIp(SmartIPUtil.getRemoteIp(request))
                        .remotePort(request.getRemotePort())
                        .remoteBrowser(userAgent.getBrowser().getName())
                        .remoteOs(userAgent.getOperatingSystem().getName())
                        .loginStatus(JudgeEnum.YES.getValue()).build();
        logService.addLog(logEntity);
        return ResponseDTO.succData(loginDTO);
    }

    /**
     * 手机端退出登陆，清除token缓存
     *
     * @param requestToken
     * @return 退出登陆是否成功，bool
     */
    public ResponseDTO<Boolean> logoutByToken(RequestTokenBO requestToken) {
        privilegeEmployeeService.removeCache(requestToken.getRequestUserId());
        return ResponseDTO.succ();
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public ResponseDTO<KaptchaVO> verificationCode() {
        KaptchaVO kaptchaVO = new KaptchaVO();
        String uuid = buildVerificationCodeRedisKey(UUID.randomUUID().toString());
        String kaptchaText = defaultKaptcha.createText();

        String base64Code = "";

        BufferedImage image = defaultKaptcha.createImage(kaptchaText);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            base64Code = Base64.encodeBase64String(outputStream.toByteArray());
        } catch (Exception e) {
            log.error("verificationCode exception .{}", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    log.error("verificationCode outputStream close exception .{}", e);
                }
            }
        }
        kaptchaVO.setUuid(uuid);
        kaptchaVO.setCode("data:image/png;base64," + base64Code);
        redisValueOperations.set(uuid, kaptchaText, 60L, TimeUnit.SECONDS);
        return ResponseDTO.succData(kaptchaVO);
    }

    private String buildVerificationCodeRedisKey(String uuid) {
        return String.format(VERIFICATION_CODE_REDIS_PREFIX, uuid);
    }

    /**
     * 初始化员工权限
     *
     * @param employeeId
     * @return
     */
    public List<LoginPrivilegeDTO> initEmployeePrivilege(Long employeeId) {
        List<PrivilegeEntity> privilegeList = privilegeEmployeeService.getPrivilegesByEmployeeId(employeeId);
        privilegeEmployeeService.updateCachePrivilege(employeeId, privilegeList);
        return SmartBeanUtil.copyList(privilegeList, LoginPrivilegeDTO.class);
    }

    public LoginDetailVO getSession(RequestTokenBO requestUser) {
        LoginDetailVO loginDTO = SmartBeanUtil.copy(requestUser.getEmployeeBO(), LoginDetailVO.class);
        List<PrivilegeEntity> privilegeEntityList = privilegeEmployeeService.getEmployeeAllPrivilege(requestUser.getRequestUserId());
        //======  开启缓存   ======
        if (privilegeEntityList == null) {
            List<LoginPrivilegeDTO> loginPrivilegeDTOS = initEmployeePrivilege(requestUser.getRequestUserId());
            loginDTO.setPrivilegeList(loginPrivilegeDTOS);
        } else {
            loginDTO.setPrivilegeList(SmartBeanUtil.copyList(privilegeEntityList, LoginPrivilegeDTO.class));
        }

        //======  不开启缓存   ======
//        List<LoginPrivilegeDTO> loginPrivilegeDTOS = initEmployeePrivilege(requestUser.getRequestUserId());
//        loginDTO.setPrivilegeList(loginPrivilegeDTOS);

        //判断是否为超管
        Boolean isSuperman = privilegeEmployeeService.isSuperman(loginDTO.getId());
        loginDTO.setIsSuperMan(isSuperman);
        return loginDTO;
    }
}
