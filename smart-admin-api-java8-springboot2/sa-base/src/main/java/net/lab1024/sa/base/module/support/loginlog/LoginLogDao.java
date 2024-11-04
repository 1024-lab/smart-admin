package net.lab1024.sa.base.module.support.loginlog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.base.module.support.loginlog.domain.LoginLogEntity;
import net.lab1024.sa.base.module.support.loginlog.domain.LoginLogQueryForm;
import net.lab1024.sa.base.module.support.loginlog.domain.LoginLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 登录日志
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022/07/22 19:46:23
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
@Component
public interface LoginLogDao extends BaseMapper<LoginLogEntity> {

    /**
     * 分页查询
     *
     * @param page
     * @param queryForm
     * @return LoginLogVO
     */
    List<LoginLogVO> queryByPage(Page page, @Param("query") LoginLogQueryForm queryForm);

    /**
     * 查询上一个登录记录
     *
     * @param userId
     * @param userType
     * @return LoginLogVO
     */
    LoginLogVO queryLastByUserId(@Param("userId") Long userId,@Param("userType") Integer userType, @Param("loginLogResult")Integer loginLogResult);


}
