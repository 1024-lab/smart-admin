package net.lab1024.sa.base.module.support.securityprotect.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.enumeration.UserTypeEnum;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.securityprotect.dao.LoginFailDao;
import net.lab1024.sa.base.module.support.securityprotect.domain.LoginFailEntity;
import net.lab1024.sa.base.module.support.securityprotect.domain.LoginFailQueryForm;
import net.lab1024.sa.base.module.support.securityprotect.domain.LoginFailVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 三级等保 登录 相关
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2023/10/11 19:25:59
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>，Since 2012
 */

@Service
public class SecurityLoginService {

    private static final String LOGIN_LOCK_MSG = "您已连续登录失败%s次，账号锁定%s分钟，解锁时间为：%s，请您耐心等待！";

    private static final String LOGIN_FAIL_MSG = "登录名或密码错误！连续登录失败%s次，账号将锁定%s分钟！您还可以再尝试%s次！";

    @Resource
    private Level3ProtectConfigService level3ProtectConfigService;

    @Resource
    private LoginFailDao loginFailDao;


    /**
     * 检查是否可以登录
     *
     * @param userId
     * @param userType
     * @return
     */
    public ResponseDTO<LoginFailEntity> checkLogin(Long userId, UserTypeEnum userType) {

        // 若登录最大失败次数小于1，无需校验
        if (level3ProtectConfigService.getLoginFailMaxTimes() < 1) {
            return ResponseDTO.ok();
        }


        LoginFailEntity loginFailEntity = loginFailDao.selectByUserIdAndUserType(userId, userType.getValue());
        if (loginFailEntity == null) {
            return ResponseDTO.ok();
        }

        // 校验登录失败次数
        if (loginFailEntity.getLoginFailCount() < level3ProtectConfigService.getLoginFailMaxTimes()) {
            return ResponseDTO.ok(loginFailEntity);
        }

        // 校验是否锁定
        if (loginFailEntity.getLoginLockBeginTime() == null) {
            return ResponseDTO.ok(loginFailEntity);
        }

        // 校验锁定时长
        if (loginFailEntity.getLoginLockBeginTime().plusSeconds(level3ProtectConfigService.getLoginFailLockSeconds()).isBefore(LocalDateTime.now())) {
            // 过了锁定时间
            return ResponseDTO.ok(loginFailEntity);
        }

        LocalDateTime unlockTime = loginFailEntity.getLoginLockBeginTime().plusSeconds(level3ProtectConfigService.getLoginFailLockSeconds());
        return ResponseDTO.error(UserErrorCode.LOGIN_FAIL_LOCK, String.format(LOGIN_LOCK_MSG, loginFailEntity.getLoginFailCount(), level3ProtectConfigService.getLoginFailLockSeconds() / 60, LocalDateTimeUtil.formatNormal(unlockTime)));
    }

    /**
     * 登录失败后记录
     *
     * @param userId
     * @param userType
     * @param loginFailEntity
     */
    public String recordLoginFail(Long userId, UserTypeEnum userType, String loginName, LoginFailEntity loginFailEntity) {

        // 若登录最大失败次数小于1，无需记录
        if (level3ProtectConfigService.getLoginFailMaxTimes() < 1) {
            return null;
        }

        // 登录失败
        int loginFailCount = loginFailEntity == null ? 1 : loginFailEntity.getLoginFailCount() + 1;
        boolean lockFlag = loginFailCount >= level3ProtectConfigService.getLoginFailMaxTimes();
        LocalDateTime lockBeginTime = lockFlag ? LocalDateTime.now() : null;

        if (loginFailEntity == null) {
            loginFailEntity = LoginFailEntity.builder()
                    .userId(userId)
                    .userType(userType.getValue())
                    .loginName(loginName)
                    .loginFailCount(loginFailCount)
                    .lockFlag(lockFlag)
                    .loginLockBeginTime(lockBeginTime)
                    .build();
            loginFailDao.insert(loginFailEntity);
        } else {
            loginFailEntity.setLoginLockBeginTime(lockBeginTime);
            loginFailEntity.setLoginFailCount(loginFailCount);
            loginFailEntity.setLockFlag(lockFlag);
            loginFailEntity.setLoginName(loginName);
            loginFailDao.updateById(loginFailEntity);
        }

        // 提示信息
        if (lockFlag) {
            LocalDateTime unlockTime = loginFailEntity.getLoginLockBeginTime().plusSeconds(level3ProtectConfigService.getLoginFailLockSeconds());
            return String.format(LOGIN_LOCK_MSG, loginFailEntity.getLoginFailCount(), level3ProtectConfigService.getLoginFailLockSeconds() / 60, LocalDateTimeUtil.formatNormal(unlockTime));
        } else {
            return String.format(LOGIN_FAIL_MSG, level3ProtectConfigService.getLoginFailMaxTimes(), level3ProtectConfigService.getLoginFailLockSeconds() / 60, level3ProtectConfigService.getLoginFailMaxTimes() - loginFailEntity.getLoginFailCount());
        }
    }

    /**
     * 清除登录失败
     *
     * @param userId
     * @param userType
     */
    public void removeLoginFail(Long userId, UserTypeEnum userType) {

        // 若登录最大失败次数小于1，无需校验
        if (level3ProtectConfigService.getLoginFailMaxTimes() < 1) {
            return;
        }

        loginFailDao.deleteByUserIdAndUserType(userId, userType.getValue());
    }

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public PageResult<LoginFailVO> queryPage(LoginFailQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<LoginFailVO> list = loginFailDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 批量删除
     *
     * @param idList
     * @return
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return ResponseDTO.ok();
        }

        loginFailDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

}
;