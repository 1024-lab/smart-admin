package net.lab1024.sa.base.common.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 校验数据是否为空的包装类
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2020/10/16 21:06:11
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class ValidateData<T> {

    @NotNull(message = "数据不能为空哦")
    private T data;
}