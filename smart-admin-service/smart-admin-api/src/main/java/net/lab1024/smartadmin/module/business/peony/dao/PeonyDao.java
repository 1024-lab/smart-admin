package net.lab1024.smartadmin.module.business.peony.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.lab1024.smartadmin.module.business.peony.domain.dto.PeonyQueryDTO;
import net.lab1024.smartadmin.module.business.peony.domain.entity.PeonyEntity;
import net.lab1024.smartadmin.module.business.peony.domain.vo.PeonyVO;
import net.lab1024.smartadmin.module.business.peony.domain.vo.PeonyExcelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [ 牡丹花 ]
 *
 * @author 卓大
 * @version 1.0
 * @company 1024创新实验室( www.1024lab.net )
 * @copyright (c)  1024创新实验室( www.1024lab.net )Inc. All rights reserved.
 * @date 2020-04-06 18:17:56
 * @since JDK1.8
 */
@Mapper
@Component
public interface PeonyDao extends BaseMapper<PeonyEntity> {

    /**
     * 分页查询
     * @param queryDTO
     * @return PeonyVO
    */
    IPage<PeonyVO> queryByPage(Page page, @Param("queryDTO") PeonyQueryDTO queryDTO);

    /**
     * 根据id删除
     * @param id
     * @return
    */
    void deleteById(@Param("id") Long id);

    /**
     * 根据id批量删除
     * @param idList
     * @return
    */
    void deleteByIdList(@Param("idList") List<Long> idList);

        /**
     * 查询所有导出数据
     * @param queryDTO
     * @return
     */
    List<PeonyExcelVO> queryAllExportData(@Param("queryDTO") PeonyQueryDTO queryDTO);

        /**
         * 查询批量导出数据
         * @param idList
         * @return
         */
    List<PeonyExcelVO> queryBatchExportData(@Param("idList") List<Long> idList);
}
