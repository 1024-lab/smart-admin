package net.lab1024.sa.admin.module.business.oa.invoice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.oa.enterprise.EnterpriseService;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.sa.admin.module.business.oa.invoice.domain.*;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerConst;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * OA发票信息
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-06-23 19:32:59
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
@Slf4j
public class InvoiceService {

    @Resource
    private InvoiceDao invoiceDao;

    @Resource
    private EnterpriseService enterpriseService;

    @Resource
    private DataTracerService dataTracerService;

    /**
     * 分页查询发票信息
     */
    public ResponseDTO<PageResult<InvoiceVO>> queryByPage(InvoiceQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<InvoiceVO> invoiceList = invoiceDao.queryPage(page, queryForm);
        PageResult<InvoiceVO> pageResult = SmartPageUtil.convert2PageResult(page, invoiceList);
        return ResponseDTO.ok(pageResult);
    }

    public ResponseDTO<List<InvoiceVO>> queryList(Long enterpriseId) {
        InvoiceQueryForm queryForm = new InvoiceQueryForm();
        queryForm.setDeletedFlag(Boolean.FALSE);
        queryForm.setDisabledFlag(Boolean.FALSE);
        queryForm.setEnterpriseId(enterpriseId);
        List<InvoiceVO> invoiceList = invoiceDao.queryPage(null, queryForm);
        return ResponseDTO.ok(invoiceList);
    }

    /**
     * 查询发票信息详情
     */
    public ResponseDTO<InvoiceVO> getDetail(Long invoiceId) {
        // 校验发票信息是否存在
        InvoiceVO invoiceVO = invoiceDao.getDetail(invoiceId, Boolean.FALSE);
        if (Objects.isNull(invoiceVO)) {
            return ResponseDTO.userErrorParam("发票信息不存在");
        }
        return ResponseDTO.ok(invoiceVO);
    }

    /**
     * 新建发票信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createInvoice(InvoiceAddForm createVO) {
        Long enterpriseId = createVO.getEnterpriseId();
        // 校验企业是否存在
        EnterpriseVO enterpriseVO = enterpriseService.getDetail(enterpriseId);
        if (Objects.isNull(enterpriseVO)) {
            return ResponseDTO.userErrorParam("企业不存在");
        }
        // 验证发票信息账号是否重复
        InvoiceEntity validateInvoice = invoiceDao.queryByAccountNumber(enterpriseId, createVO.getAccountNumber(), null, Boolean.FALSE);
        if (Objects.nonNull(validateInvoice)) {
            return ResponseDTO.userErrorParam("发票信息账号重复");
        }
        // 数据插入
        InvoiceEntity insertInvoice = SmartBeanUtil.copy(createVO, InvoiceEntity.class);
        invoiceDao.insert(insertInvoice);
        dataTracerService.addTrace(enterpriseId, DataTracerTypeEnum.OA_ENTERPRISE, "新增发票：" + DataTracerConst.HTML_BR + dataTracerService.getChangeContent(insertInvoice));
        return ResponseDTO.ok();
    }

    /**
     * 编辑发票信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateInvoice(InvoiceUpdateForm updateVO) {
        Long enterpriseId = updateVO.getEnterpriseId();
        // 校验企业是否存在
        EnterpriseVO enterpriseVO = enterpriseService.getDetail(enterpriseId);
        if (Objects.isNull(enterpriseVO)) {
            return ResponseDTO.userErrorParam("企业不存在");
        }
        Long invoiceId = updateVO.getInvoiceId();
        // 校验发票信息是否存在
        InvoiceEntity invoiceDetail = invoiceDao.selectById(invoiceId);
        if (Objects.isNull(invoiceDetail) || invoiceDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("发票信息不存在");
        }
        // 验证发票信息账号是否重复
        InvoiceEntity validateInvoice = invoiceDao.queryByAccountNumber(updateVO.getEnterpriseId(), updateVO.getAccountNumber(), invoiceId, Boolean.FALSE);
        if (Objects.nonNull(validateInvoice)) {
            return ResponseDTO.userErrorParam("发票信息账号重复");
        }
        // 数据编辑
        InvoiceEntity updateInvoice = SmartBeanUtil.copy(updateVO, InvoiceEntity.class);
        invoiceDao.updateById(updateInvoice);
        dataTracerService.addTrace(enterpriseId, DataTracerTypeEnum.OA_ENTERPRISE, "更新发票：" + DataTracerConst.HTML_BR + dataTracerService.getChangeContent(invoiceDetail, updateInvoice));
        return ResponseDTO.ok();
    }


    /**
     * 删除发票信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> deleteInvoice(Long invoiceId) {
        // 校验发票信息是否存在
        InvoiceEntity invoiceDetail = invoiceDao.selectById(invoiceId);
        if (Objects.isNull(invoiceDetail) || invoiceDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("发票信息不存在");
        }
        invoiceDao.deleteInvoice(invoiceId, Boolean.TRUE);
        dataTracerService.addTrace(invoiceDetail.getEnterpriseId(), DataTracerTypeEnum.OA_ENTERPRISE, "删除发票：" + DataTracerConst.HTML_BR + dataTracerService.getChangeContent(invoiceDetail));
        return ResponseDTO.ok();
    }
}
