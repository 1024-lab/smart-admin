package net.lab1024.smartadmin.module.support.codegenerator.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/5/11 0011 上午 10:12
 * @since JDK1.8
 */
@Data
@Builder
public class CodeGeneratorDTO {

    /**
     * 需要生成代码的表名
     */
    private String tableName;

    /**
     * 表前缀
     */
    private String tablePrefix;

    /**
     * 基础包路径
     */
    private String basePackage;

    /**
     * module下的子包
     */
    private String modulePackage;

    /**
     * 公司
     */
    private String company;

    /**
     * 作者
     */
    private String author;

    /**
     * 需要构建查询方法的列
     */
    private List<CodeGeneratorQueryColumnDTO> queryColumnList;
}
