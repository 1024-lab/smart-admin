package net.lab1024.smartadmin.module.support.heartbeat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 心跳日志数据库操作
 *
 * @author : simajinqiang
 * Date: 2018/7/9
 * Time: 17:37
 */
@Component
@Mapper
public interface HeartBeatRecordDao extends BaseMapper<HeartBeatRecordEntity> {

    /**
     * 新增心跳日志
     *
     * @param heartBeatRecordEntity
     */
    void insertHeartBeat(HeartBeatRecordEntity heartBeatRecordEntity);

    /**
     * 更新心跳日志
     *
     * @param id
     * @param heartBeatTime
     */
    void updateHeartBeatTimeById(@Param("id") Long id, @Param("heartBeatTime") Date heartBeatTime);

    /**
     * 查询心跳日志
     *
     * @param heartBeatRecordEntity
     * @return
     */
    HeartBeatRecordEntity query(HeartBeatRecordEntity heartBeatRecordEntity);


    /**
     * 分页查询心跳记录
     * @return
     */
    List<HeartBeatRecordVO> pageQuery(Page page);
}
