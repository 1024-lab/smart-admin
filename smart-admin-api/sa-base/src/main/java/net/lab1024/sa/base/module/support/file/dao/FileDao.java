package net.lab1024.sa.base.module.support.file.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.base.module.support.file.domain.vo.FileVO;
import net.lab1024.sa.base.module.support.file.domain.entity.FileEntity;
import net.lab1024.sa.base.module.support.file.domain.form.FileQueryForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 文件服务
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2019年10月11日 15:34:47
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
@Component
public interface FileDao extends BaseMapper<FileEntity> {

    /**
     * 文件key单个查询
     *
     * @param fileKey
     * @return
     */
    FileVO getByFileKey(@Param("fileKey") String fileKey);


    /**
     * 批量获取
     */
    List<FileVO> selectByFileKeyList(@Param("fileKeyList") Collection<String> fileKeyList);

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<FileVO> queryPage(Page page, @Param("queryForm") FileQueryForm queryForm);

}
