package com.gangquan360.smartadmin.module.codegenerator;

import com.gangquan360.smartadmin.BaseTest;
import com.gangquan360.smartadmin.module.codegenerator.constant.SqlOperateTypeEnum;
import com.gangquan360.smartadmin.module.codegenerator.domain.CodeGeneratorDTO;
import com.gangquan360.smartadmin.module.codegenerator.domain.CodeGeneratorQueryColumnDTO;
import com.gangquan360.smartadmin.module.codegenerator.service.CodeGeneratorService;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * IdGeneratorService Tester.
 *
 * @author lizongliang
 * @version 1.0
 */
public class CodeGeneratorServiceTest extends BaseTest {

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Test
    public void testGenerate() throws Exception {
                CodeGeneratorQueryColumnDTO createTimeBetween = CodeGeneratorQueryColumnDTO.builder()
                        .columnName("create_time")
                        .sqlOperate(SqlOperateTypeEnum.TIME_BETWEEN).build();
                CodeGeneratorQueryColumnDTO like = CodeGeneratorQueryColumnDTO.builder()
                        .columnName("title")
                        .sqlOperate(SqlOperateTypeEnum.LIKE).build();

                List<CodeGeneratorQueryColumnDTO> queryColumnList = Lists.newArrayList(createTimeBetween,like);
                CodeGeneratorDTO codeGenerator = CodeGeneratorDTO.builder()
                        .author("yandanyang")
                        .company("钢圈")
                        .tableName("t_notice")
                        .tablePrefix("t_")
                        .basePackage("com.gangquan360.smartadmin")
                        .queryColumnList(queryColumnList).build();
                codeGeneratorService.codeGenerator(codeGenerator);
    }

}
