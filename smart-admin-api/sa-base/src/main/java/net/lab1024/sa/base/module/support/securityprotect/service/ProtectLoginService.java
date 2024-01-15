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
import org.springframework.beans.factory.annotation.Value;
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
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>，Since 2012
 */

@Service
public class ProtectLoginService {

    private static final String LOGIN_LOCK_MSG = "您已连续登录失败%s次，账号锁定%s分钟，解锁时间为：%s，请您耐心等待！";

    private static final String LOGIN_FAIL_MSG = "登录名或密码错误！连续登录失败%s次，账号将锁定%s分钟！您还可以再尝试%s次！";

    /**
     * 连续登录失败次数则锁定，-1表示不受限制，可以一直登录
     */
    @Value("${classified-protect.login-max-fail-times}")
    private Integer loginMaxFailTimes;

    /**
     * 连续登录失败锁定时间（单位：秒），-1表示不锁定
     */
    @Value("${classified-protect.login-fail-locked-seconds}")
    private Integer loginFailLockedSeconds;

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

        // 无需校验
        if (loginMaxFailTimes < 1) {
            return ResponseDTO.ok();
        }


        LoginFailEntity loginFailEntity = loginFailDao.selectByUserIdAndUserType(userId, userType.getValue());
        if (loginFailEntity == null) {
            return ResponseDTO.ok();
        }

        // 校验次数
        if (loginFailEntity.getLoginFailCount() < loginMaxFailTimes) {
            return ResponseDTO.ok(loginFailEntity);
        }

        // 校验锁定时长
        if(loginFailEntity.getLoginLockBeginTime().plusSeconds(loginFailLockedSeconds).isBefore(LocalDateTime.now())){
            // 过了锁定时间
            return ResponseDTO.ok(loginFailEntity);
        }

        LocalDateTime unlockTime = loginFailEntity.getLoginLockBeginTime().plusSeconds(loginFailLockedSeconds);
        return ResponseDTO.error(UserErrorCode.LOGIN_FAIL_LOCK, String.format(LOGIN_LOCK_MSG, loginFailEntity.getLoginFailCount(), loginFailLockedSeconds / 60, LocalDateTimeUtil.formatNormal(unlockTime)));
    }

    /**
     * 登录失败后记录
     *
     * @param userId
     * @param userType
     * @param loginFailEntity
     */
    public String recordLoginFail(Long userId, UserTypeEnum userType, String loginName, LoginFailEntity loginFailEntity) {

        // 无需校验
        if (loginMaxFailTimes < 1) {
            return null;
        }

        if (loginFailEntity == null) {
            loginFailEntity = LoginFailEntity.builder()
                    .userId(userId)
                    .userType(userType.getValue())
                    .loginName(loginName)
                    .loginFailCount(1)
                    .lockFlag(false)
                    .loginLockBeginTime(null).build();
            loginFailDao.insert(loginFailEntity);
        } else {

            // 如果是已经锁定状态，则重新计算
            if(loginFailEntity.getLockFlag()){
                loginFailEntity.setLockFlag(false);
                loginFailEntity.setLoginFailCount(1);
                loginFailEntity.setLoginLockBeginTime(null);
            }else{
                loginFailEntity.setLoginLockBeginTime(LocalDateTime.now());
                loginFailEntity.setLoginFailCount(loginFailEntity.getLoginFailCount() + 1);
                loginFailEntity.setLockFlag(loginFailEntity.getLoginFailCount() >= loginMaxFailTimes);
            }

            loginFailEntity.setLoginName(loginName);
            loginFailDao.updateById(loginFailEntity);
        }

        // 提示信息
        if (loginFailEntity.getLoginFailCount() >= loginMaxFailTimes) {
            LocalDateTime unlockTime = loginFailEntity.getLoginLockBeginTime().plusSeconds(loginFailLockedSeconds);
            return String.format(LOGIN_LOCK_MSG, loginFailEntity.getLoginFailCount(), loginFailLockedSeconds / 60, LocalDateTimeUtil.formatNormal(unlockTime));
        } else {
            return String.format(LOGIN_FAIL_MSG, loginMaxFailTimes, loginFailLockedSeconds / 60, loginMaxFailTimes - loginFailEntity.getLoginFailCount());
        }
    }

    /**
     * 清除登录失败
     *
     * @param userId
     * @param userType
     */
    public void removeLoginFail(Long userId, UserTypeEnum userType) {
        // 无需校验
        if (loginMaxFailTimes < 1) {
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
        PageResult<LoginFailVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return pageResult;
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