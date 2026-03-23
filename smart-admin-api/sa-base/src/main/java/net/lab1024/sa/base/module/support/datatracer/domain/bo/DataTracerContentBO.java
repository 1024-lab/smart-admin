package net.lab1024.sa.base.module.support.datatracer.domain.bo;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * 变动内容
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-07-23 19:38:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class DataTracerContentBO {

    /**
     * 变动字段
     */
    private Field field;

    /**
     * 变动字段的值
     */
    private Object fieldValue;

    /**
     * 变动字段描述
     */
    private String fieldDesc;

    /**
     * 变动内容
     */
    private String fieldContent;

}
