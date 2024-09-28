package net.lab1024.sa.base.module.support.datatracer.annoation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 支持查询sql
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-07-23 19:38:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataTracerFieldSql {

    /**
     * 关联字段名称
     * @return
     */
    String relateColumn() default "id";

    /**
     * 关联显示的字段
     * @return
     */
    String relateDisplayColumn() default "";
    /**
     * 是否关联字段查询Mapper
     * @return
     */
    Class<? extends BaseMapper> relateMapper() default BaseMapper.class;

}
