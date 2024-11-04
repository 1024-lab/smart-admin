package net.lab1024.sa.base.module.support.codegenerator.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.module.support.codegenerator.constant.CodeGeneratorConstant;
import net.lab1024.sa.base.module.support.codegenerator.dao.CodeGeneratorConfigDao;
import net.lab1024.sa.base.module.support.codegenerator.dao.CodeGeneratorDao;
import net.lab1024.sa.base.module.support.codegenerator.domain.entity.CodeGeneratorConfigEntity;
import net.lab1024.sa.base.module.support.codegenerator.domain.form.CodeGeneratorConfigForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.form.CodeGeneratorPreviewForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.form.TableQueryForm;
import net.lab1024.sa.base.module.support.codegenerator.domain.model.*;
import net.lab1024.sa.base.module.support.codegenerator.domain.vo.TableColumnVO;
import net.lab1024.sa.base.module.support.codegenerator.domain.vo.TableConfigVO;
import net.lab1024.sa.base.module.support.codegenerator.domain.vo.TableVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

/**
 * 代码生成器 Service
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-06-30 22:15:38
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class CodeGeneratorService {

    @Resource
    private CodeGeneratorDao codeGeneratorDao;

    @Resource
    private CodeGeneratorConfigDao codeGeneratorConfigDao;

    @Resource
    private CodeGeneratorTemplateService codeGeneratorTemplateService;


    /**
     * 列信息
     *
     * @param tableName
     * @return
     */
    public List<TableColumnVO> getTableColumns(String tableName) {
        return codeGeneratorDao.selectTableColumn(tableName);
    }


    /**
     * 查询数据库表数据
     *
     * @param tableQueryForm
     * @return
     */
    public PageResult<TableVO> queryTableList(TableQueryForm tableQueryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(tableQueryForm);
        List<TableVO> tableVOList = codeGeneratorDao.queryTableList(page, tableQueryForm);
        return SmartPageUtil.convert2PageResult(page, tableVOList);
    }

    /**
     * 获取 表的 配置信息
     *
     * @param table
     * @return
     */
    public TableConfigVO getTableConfig(String table) {

        TableConfigVO config = new TableConfigVO();

        CodeGeneratorConfigEntity codeGeneratorConfigEntity = codeGeneratorConfigDao.selectById(table);
        if (codeGeneratorConfigEntity == null) {
            return config;
        }

        if (SmartStringUtil.isNotEmpty(codeGeneratorConfigEntity.getBasic())) {
            CodeBasic basic = JSON.parseObject(codeGeneratorConfigEntity.getBasic(), CodeBasic.class);
            config.setBasic(basic);
        }

        if (SmartStringUtil.isNotEmpty(codeGeneratorConfigEntity.getFields())) {
            List<CodeField> fields = JSONArray.parseArray(codeGeneratorConfigEntity.getFields(), CodeField.class);
            config.setFields(fields);
        }

        if (SmartStringUtil.isNotEmpty(codeGeneratorConfigEntity.getInsertAndUpdate())) {
            CodeInsertAndUpdate insertAndUpdate = JSON.parseObject(codeGeneratorConfigEntity.getInsertAndUpdate(), CodeInsertAndUpdate.class);
            config.setInsertAndUpdate(insertAndUpdate);
        }

        if (SmartStringUtil.isNotEmpty(codeGeneratorConfigEntity.getDeleteInfo())) {
            CodeDelete deleteInfo = JSON.parseObject(codeGeneratorConfigEntity.getDeleteInfo(), CodeDelete.class);
            config.setDeleteInfo(deleteInfo);
        }

        if (SmartStringUtil.isNotEmpty(codeGeneratorConfigEntity.getQueryFields())) {
            List<CodeQueryField> queryFields = JSONArray.parseArray(codeGeneratorConfigEntity.getQueryFields(), CodeQueryField.class);
            config.setQueryFields(queryFields);
        }

        if (SmartStringUtil.isNotEmpty(codeGeneratorConfigEntity.getTableFields())) {
            List<CodeTableField> tableFields = JSONArray.parseArray(codeGeneratorConfigEntity.getTableFields(), CodeTableField.class);
            config.setTableFields(tableFields);
        }

        return config;
    }

    /**
     * 更新配置
     *
     * @param form
     * @return
     */
    public synchronized ResponseDTO<String> updateConfig(CodeGeneratorConfigForm form) {
        long existCount = codeGeneratorDao.countByTableName(form.getTableName());
        if (existCount == 0) {
            return ResponseDTO.userErrorParam("表不存在，请联系后端查看下数据库");
        }

        CodeGeneratorConfigEntity codeGeneratorConfigEntity = codeGeneratorConfigDao.selectById(form.getTableName());
        boolean updateFlag = true;
        if (codeGeneratorConfigEntity == null) {
            codeGeneratorConfigEntity = new CodeGeneratorConfigEntity();
            updateFlag = false;
        }

        // 校验假删，必须有 deleted_flag 字段
        List<TableColumnVO> tableColumns = getTableColumns(form.getTableName());
        if (null != form.getDeleteInfo() && form.getDeleteInfo().getIsSupportDelete() && !form.getDeleteInfo().getIsPhysicallyDeleted()) {
            Optional<TableColumnVO> any = tableColumns.stream().filter(e -> e.getColumnName().equals(CodeGeneratorConstant.DELETED_FLAG)).findAny();
            if (!any.isPresent()) {
                return ResponseDTO.userErrorParam("表结构中没有假删字段：" + CodeGeneratorConstant.DELETED_FLAG + ",请仔细排查");
            }
        }

        // 校验表必须有主键
        if(!tableColumns.stream().filter( e -> "PRI".equalsIgnoreCase(e.getColumnKey())).findAny().isPresent()){
            return ResponseDTO.userErrorParam("表必须有主键，请联系后端查看下数据库表结构");
        }

        codeGeneratorConfigEntity.setTableName(form.getTableName());
        codeGeneratorConfigEntity.setBasic(JSON.toJSONString(form.getBasic()));
        codeGeneratorConfigEntity.setFields(JSONArray.toJSONString(form.getFields()));
        codeGeneratorConfigEntity.setInsertAndUpdate(JSON.toJSONString(form.getInsertAndUpdate()));
        codeGeneratorConfigEntity.setDeleteInfo(JSON.toJSONString(form.getDeleteInfo()));
        codeGeneratorConfigEntity.setQueryFields(JSONArray.toJSONString(form.getQueryFields()));
        codeGeneratorConfigEntity.setTableFields(JSONArray.toJSONString(form.getTableFields()));

        if (updateFlag) {
            codeGeneratorConfigDao.updateById(codeGeneratorConfigEntity);
        } else {
            codeGeneratorConfigDao.insert(codeGeneratorConfigEntity);
        }
        return ResponseDTO.ok();
    }

    /**
     * 预览
     *
     * @param form
     * @return
     */
    public ResponseDTO<String> preview(CodeGeneratorPreviewForm form) {
        long existCount = codeGeneratorDao.countByTableName(form.getTableName());
        if (existCount == 0) {
            return ResponseDTO.userErrorParam("表不存在，请联系后端查看下数据库");
        }

        CodeGeneratorConfigEntity codeGeneratorConfigEntity = codeGeneratorConfigDao.selectById(form.getTableName());
        if (codeGeneratorConfigEntity == null) {
            return ResponseDTO.userErrorParam("配置信息不存在，请先进行配置");
        }

        List<TableColumnVO> columns = getTableColumns(form.getTableName());
        if (CollectionUtils.isEmpty(columns)) {
            return ResponseDTO.userErrorParam("表没有列信息无法生成");
        }

        String result = codeGeneratorTemplateService.generate(form.getTableName(), form.getTemplateFile(), codeGeneratorConfigEntity);
        return ResponseDTO.ok(result);

    }

    /**
     * 下载代码
     * @param tableName
     * @return
     */
    public ResponseDTO<byte[]> download(String tableName) {
        if (SmartStringUtil.isBlank(tableName)) {
            return ResponseDTO.userErrorParam("表名不能为空");
        }

        long existCount = codeGeneratorDao.countByTableName(tableName);
        if (existCount == 0) {
            return ResponseDTO.userErrorParam("表不存在，请联系后端查看下数据库");
        }

        CodeGeneratorConfigEntity codeGeneratorConfigEntity = codeGeneratorConfigDao.selectById(tableName);
        if (codeGeneratorConfigEntity == null) {
            return ResponseDTO.userErrorParam("配置信息不存在，请先进行配置");
        }

        List<TableColumnVO> columns = getTableColumns(tableName);
        if (CollectionUtils.isEmpty(columns)) {
            return ResponseDTO.userErrorParam("表没有列信息无法生成");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        codeGeneratorTemplateService.zipGeneratedFiles(out, tableName, codeGeneratorConfigEntity);
        return ResponseDTO.ok(out.toByteArray());
    }
}