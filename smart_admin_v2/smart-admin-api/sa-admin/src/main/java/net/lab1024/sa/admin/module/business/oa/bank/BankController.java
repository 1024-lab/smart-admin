package net.lab1024.sa.admin.module.business.oa.bank;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.sa.admin.constant.AdminSwaggerTagConst;
import net.lab1024.sa.admin.module.business.oa.bank.domain.BankCreateForm;
import net.lab1024.sa.admin.module.business.oa.bank.domain.BankQueryForm;
import net.lab1024.sa.admin.module.business.oa.bank.domain.BankUpdateForm;
import net.lab1024.sa.admin.module.business.oa.bank.domain.BankVO;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.RequestUser;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.common.util.SmartRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * OA办公-OA银行信息
 *
 * @Author 1024创新实验室:善逸
 * @Date 2022/6/23 21:59:22
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@RestController
@Api(tags = {AdminSwaggerTagConst.Business.OA_BANK})
public class BankController {

    @Autowired
    private BankService bankService;

    @ApiOperation(value = "分页查询银行信息 @author 善逸")
    @PostMapping("/oa/bank/page/query")
    public ResponseDTO<PageResult<BankVO>> queryByPage(@RequestBody @Valid BankQueryForm queryDTO) {
        return bankService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "根据企业ID查询银行信息列表 @author 善逸")
    @GetMapping("/oa/bank/query/list/{enterpriseId}")
    public ResponseDTO<List<BankVO>> queryList(@PathVariable Long enterpriseId) {
        return bankService.queryList(enterpriseId);
    }

    @ApiOperation(value = "查询银行信息详情 @author 善逸")
    @GetMapping("/oa/bank/get/{bankId}")
    public ResponseDTO<BankVO> getDetail(@PathVariable Long bankId) {
        return bankService.getDetail(bankId);
    }

    @ApiOperation(value = "新建银行信息 @author 善逸")
    @PostMapping("/oa/bank/create")
    public ResponseDTO<String> createBank(@RequestBody @Valid BankCreateForm createVO) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        createVO.setCreateUserId(requestUser.getUserId());
        createVO.setCreateUserName(requestUser.getUserName());
        return bankService.createBank(createVO);
    }

    @ApiOperation(value = "编辑银行信息 @author 善逸")
    @PostMapping("/oa/bank/update")
    public ResponseDTO<String> updateBank(@RequestBody @Valid BankUpdateForm updateVO) {
        return bankService.updateBank(updateVO);
    }

    @ApiOperation(value = "删除银行信息 @author 善逸")
    @GetMapping("/oa/bank/delete/{bankId}")
    public ResponseDTO<String> deleteBank(@PathVariable Long bankId) {
        return bankService.deleteBank(bankId);
    }
}
