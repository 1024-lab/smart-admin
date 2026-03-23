package net.lab1024.sa.base.module.support.table;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.sa.base.module.support.table.domain.TableColumnEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 表格自定义列（前端用户自定义表格列，并保存到数据库里）
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 22:52:21
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
public interface TableColumnDao extends BaseMapper<TableColumnEntity> {

    TableColumnEntity selectByUserIdAndTableId(@Param("userId") Long userId, @Param("userType") Integer userType, @Param("tableId") Integer tableId);

    void deleteTableColumn(@Param("userId") Long userId, @Param("userType") Integer userType, @Param("tableId") Integer tableId);
}
