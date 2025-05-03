package net.lab1024.sa.admin.module.business.oa.invoice.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.oa.invoice.service.InvoiceService;
import net.lab1024.sa.admin.module.business.oa.invoice.domain.InvoiceAddForm;
import net.lab1024.sa.admin.module.business.oa.invoice.domain.InvoiceQueryForm;
import net.lab1024.sa.admin.module.business.oa.invoice.domain.InvoiceUpdateForm;
import net.lab1024.sa.admin.module.business.oa.invoice.domain.InvoiceVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OA发票信息
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-06-23 19:32:59
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@RestController
@Tag(name = AdminSwaggerTagConst.Business.OA_INVOICE)
public class InvoiceController {

    @Resource
    private InvoiceService invoiceService;

    @Operation(summary = "分页查询发票信息 @author 善逸")
    @PostMapping("/oa/invoice/page/query")
    @SaCheckPermission("oa:invoice:query")
    public ResponseDTO<PageResult<InvoiceVO>> queryByPage(@RequestBody @Valid InvoiceQueryForm queryForm) {
        return invoiceService.queryByPage(queryForm);
    }

    @Operation(summary = "查询发票信息详情 @author 善逸")
    @GetMapping("/oa/invoice/get/{invoiceId}")
    @SaCheckPermission("oa:invoice:query")
    public ResponseDTO<InvoiceVO> getDetail(@PathVariable Long invoiceId) {
        return invoiceService.getDetail(invoiceId);
    }

    @Operation(summary = "新建发票信息 @author 善逸")
    @PostMapping("/oa/invoice/create")
    @SaCheckPermission("oa:invoice:add")
    public ResponseDTO<String> createInvoice(@RequestBody @Valid InvoiceAddForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return invoiceService.createInvoice(createVO);
    }

    @OperateLog
    @Operation(summary = "编辑发票信息 @author 善逸")
    @PostMapping("/oa/invoice/update")
    @SaCheckPermission("oa:invoice:update")
    public ResponseDTO<String> updateInvoice(@RequestBody @Valid InvoiceUpdateForm updateVO) {
        return invoiceService.updateInvoice(updateVO);
    }

    @Operation(summary = "删除发票信息 @author 善逸")
    @GetMapping("/invoice/delete/{invoiceId}")
    @SaCheckPermission("oa:invoice:delete")
    public ResponseDTO<String> deleteInvoice(@PathVariable Long invoiceId) {
        return invoiceService.deleteInvoice(invoiceId);
    }

    @Operation(summary = "查询列表 @author lidoudou")
    @GetMapping("/oa/invoice/query/list/{enterpriseId}")
    @SaCheckPermission("oa:invoice:query")
    public ResponseDTO<List<InvoiceVO>> queryList(@PathVariable Long enterpriseId) {
        return invoiceService.queryList(enterpriseId);
    }


}
