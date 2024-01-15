package net.lab1024.sa.base.common.swagger;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import io.swagger.v3.oas.models.Operation;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.module.support.apiencrypt.annotation.ApiDecrypt;
import net.lab1024.sa.base.module.support.apiencrypt.annotation.ApiEncrypt;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限、接口加解密等
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2023/12/26 13:47:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */

public class SmartOperationCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {

        List<String> noteList = new ArrayList<>();

        // 请求参数加密
        List<String> encryptBuilderList = new ArrayList<>();

        if (handlerMethod.getMethodAnnotation(ApiDecrypt.class) != null ||
                handlerMethod.getBeanType().getAnnotation(ApiDecrypt.class) != null) {
            encryptBuilderList.add("【请求参数加密】");
        }

        if (handlerMethod.getMethodAnnotation(ApiEncrypt.class) != null ||
                handlerMethod.getBeanType().getAnnotation(ApiEncrypt.class) != null) {
            encryptBuilderList.add("【返回结果加密】");
        }

        if (!encryptBuilderList.isEmpty()) {
            noteList.add("<br/><font style=\"color:red\" class=\"light-red\">接口安全：" + SmartStringUtil.join(",", encryptBuilderList) + "</font>");
        }

        // 权限
        noteList.addAll(getPermission(handlerMethod));

        // 更新
        operation.setDescription(SmartStringUtil.join("<br/>", noteList));

        return operation;
    }


    private List<String> getPermission(HandlerMethod handlerMethod) {
        List<String> values = new ArrayList<>();

        StringBuilder permissionStringBuilder = new StringBuilder();
        SaCheckPermission classPermissions = handlerMethod.getBeanType().getAnnotation(SaCheckPermission.class);
        if (classPermissions != null) {
            permissionStringBuilder.append("<font style=\"color:red\" class=\"light-red\">");
            permissionStringBuilder.append("类：").append(getAnnotationNote(classPermissions.value(), classPermissions.mode()));
            permissionStringBuilder.append("</font></br>");
        }

        SaCheckPermission methodPermission = handlerMethod.getMethodAnnotation(SaCheckPermission.class);
        if (methodPermission != null) {
            permissionStringBuilder.append("<font style=\"color:red\" class=\"light-red\">");
            permissionStringBuilder.append("方法：").append(getAnnotationNote(methodPermission.value(), methodPermission.mode()));
            permissionStringBuilder.append("</font></br>");
        }

        if (permissionStringBuilder.length() > 0) {
            permissionStringBuilder.insert(0, "<font style=\"color:red\" class=\"light-red\">权限校验：</font></br>");
            values.add(permissionStringBuilder.toString());
        }


        StringBuilder roleStringBuilder = new StringBuilder();
        SaCheckRole classCheckRole = handlerMethod.getBeanType().getAnnotation(SaCheckRole.class);
        if (classCheckRole != null) {
            roleStringBuilder.append("<font style=\"color:red\" class=\"light-red\">");
            roleStringBuilder.append("类：").append(getAnnotationNote(classCheckRole.value(), classCheckRole.mode()));
            roleStringBuilder.append("</font></br>");
        }

        SaCheckPermission methodCheckRole = handlerMethod.getMethodAnnotation(SaCheckPermission.class);
        if (methodCheckRole != null) {
            roleStringBuilder.append("<font style=\"color:red\" class=\"light-red\">");
            roleStringBuilder.append("方法：").append(getAnnotationNote(methodCheckRole.value(), methodCheckRole.mode()));
            roleStringBuilder.append("</font></br>");
        }

        if (roleStringBuilder.length() > 0) {
            roleStringBuilder.insert(0, "<font style=\"color:red\" class=\"light-red\">角色校验：</font></br>");
            values.add(roleStringBuilder.toString());
        }

        return values;
    }

    private String getAnnotationNote(String[] values, SaMode mode) {
        if (mode.equals(SaMode.AND)) {
            return String.join(" 且 ", values);
        } else {
            return String.join(" 或 ", values);
        }
    }
}
