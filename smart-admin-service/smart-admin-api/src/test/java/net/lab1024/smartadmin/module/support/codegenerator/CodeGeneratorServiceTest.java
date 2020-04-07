package net.lab1024.smartadmin.module.support.codegenerator;

import net.lab1024.smartadmin.BaseTest;
import net.lab1024.smartadmin.module.support.codegenerator.constant.SqlOperateTypeEnum;
import net.lab1024.smartadmin.module.support.codegenerator.domain.CodeGeneratorDTO;
import net.lab1024.smartadmin.module.support.codegenerator.domain.CodeGeneratorQueryColumnDTO;
import net.lab1024.smartadmin.module.support.codegenerator.service.CodeGeneratorService;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * IdGeneratorService Tester.
 *
 * @author zhuoda
 * @version 1.0
 */
public class CodeGeneratorServiceTest extends BaseTest {

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Test
    public void testGenerate() throws Exception {
        /**
         * !!! 所有查询条件均包含了createTime和updateTime的 范围查询 !!!
         */

        // t_peony 牡丹花表

        //搜索字段 kind， 使用like搜索
        CodeGeneratorQueryColumnDTO kind = CodeGeneratorQueryColumnDTO.builder()
                .columnName("kind")
                .sqlOperate(SqlOperateTypeEnum.LIKE).build();
        //搜索字段 kind， 使用 == 搜索
        CodeGeneratorQueryColumnDTO id = CodeGeneratorQueryColumnDTO.builder()
                .columnName("id")
                .sqlOperate(SqlOperateTypeEnum.EQUALS).build();
        //搜索字段 name， 使用like搜索
        CodeGeneratorQueryColumnDTO name = CodeGeneratorQueryColumnDTO.builder()
                .columnName("name")
                .sqlOperate(SqlOperateTypeEnum.LIKE).build();
        //搜索字段 color， 使用like搜索
        CodeGeneratorQueryColumnDTO color = CodeGeneratorQueryColumnDTO.builder()
                .columnName("color")
                .sqlOperate(SqlOperateTypeEnum.LIKE).build();

        List<CodeGeneratorQueryColumnDTO> queryColumnList = Lists.newArrayList(id, kind, name, color);

        CodeGeneratorDTO codeGenerator = CodeGeneratorDTO.builder()
                .author("卓大")//class 注释作者
                .company("1024创新实验室( www.1024lab.net )")//class注释公司名字
                .tableName("t_peony")//表名
                .tablePrefix("t_")//表名前缀
                .basePackage("net.lab1024.smartadmin")//包名
                .modulePackage("business.peony")//业务子包名
                .queryColumnList(queryColumnList)//加入搜搜字段
                .build();//构建

        //当需要代码生成的时候，请将注释去掉
//        codeGeneratorService.codeGenerator(codeGenerator);
    }

}
