package net.lab1024.smartadmin.module.business.peony.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.business.peony.dao.PeonyDao;
import net.lab1024.smartadmin.module.business.peony.domain.dto.PeonyAddDTO;
import net.lab1024.smartadmin.module.business.peony.domain.dto.PeonyUpdateDTO;
import net.lab1024.smartadmin.module.business.peony.domain.dto.PeonyQueryDTO;
import net.lab1024.smartadmin.module.business.peony.domain.entity.PeonyEntity;
import net.lab1024.smartadmin.module.business.peony.domain.vo.PeonyVO;
import net.lab1024.smartadmin.module.business.peony.domain.vo.PeonyExcelVO;
import net.lab1024.smartadmin.util.SmartPageUtil;
import net.lab1024.smartadmin.util.SmartBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service
public class PeonyService {

    @Autowired
    private PeonyDao peonyDao;

    /**
     * 根据id查询
     */
    public PeonyEntity getById(Long id){
        return  peonyDao.selectById(id);
    }

    /**
     * 分页查询
     * @author 卓大
     * @date 2020-04-06 18:17:56
     */
    public ResponseDTO<PageResultDTO<PeonyVO>> queryByPage(PeonyQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2QueryPage(queryDTO);
        IPage<PeonyVO> voList = peonyDao.queryByPage(page, queryDTO);
        PageResultDTO<PeonyVO> pageResultDTO = SmartPageUtil.convert2PageResult(voList);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * 添加
     * @author 卓大
     * @date 2020-04-06 18:17:56
     */
    public ResponseDTO<String> add(PeonyAddDTO addDTO) {
        PeonyEntity entity = SmartBeanUtil.copy(addDTO, PeonyEntity.class);
        peonyDao.insert(entity);
        return ResponseDTO.succ();
    }

    /**
     * 编辑
     * @author 卓大
     * @date 2020-04-06 18:17:56
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(PeonyUpdateDTO updateDTO) {
        PeonyEntity entity = SmartBeanUtil.copy(updateDTO, PeonyEntity.class);
        peonyDao.updateById(entity);
        return ResponseDTO.succ();
    }

    /**
     * 删除
     * @author 卓大
     * @date 2020-04-06 18:17:56
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> deleteByIds(List<Long> idList) {
        peonyDao.deleteByIdList(idList);
        return ResponseDTO.succ();
    }

    /**
     * 查询全部导出对象
     * @author 卓大
     * @date 2020-04-06 18:17:56
     */
    public List<PeonyExcelVO> queryAllExportData(PeonyQueryDTO queryDTO) {
        return peonyDao.queryAllExportData( queryDTO);
    }

    /**
     * 批量查询导出对象
     * @author 卓大
     * @date 2020-04-06 18:17:56
     */
    public List<PeonyExcelVO> queryBatchExportData(List<Long> idList) {
        return peonyDao.queryBatchExportData(idList);
    }
}
