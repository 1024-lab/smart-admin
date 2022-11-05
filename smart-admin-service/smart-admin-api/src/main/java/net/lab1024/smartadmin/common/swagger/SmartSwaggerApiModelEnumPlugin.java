/*
 *
 *  Copyright 2015 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package net.lab1024.smartadmin.common.swagger;

import net.lab1024.smartadmin.common.anno.ApiModelPropertyEnum;
import net.lab1024.smartadmin.common.domain.BaseEnum;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.reflect.AnnotatedElement;

import static springfox.documentation.schema.Annotations.findPropertyAnnotation;

/**
 * swagger 用于说明枚举类字段说明
 * SWAGGER_PLUGIN_ORDER+1 是将此配置放在原来的后面执行
 *
 * @author listen
 * @date 2019年5月16日 15:36:56
 */
public class SmartSwaggerApiModelEnumPlugin implements ModelPropertyBuilderPlugin {

    @Override
    public void apply(ModelPropertyContext context) {
        Optional<ApiModelPropertyEnum> enumOptional = Optional.absent();

        if (context.getAnnotatedElement().isPresent()) {
            enumOptional = enumOptional.or(findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
        }
        if (context.getBeanPropertyDefinition().isPresent()) {
            enumOptional = enumOptional.or(findPropertyAnnotation(context.getBeanPropertyDefinition().get(), ApiModelPropertyEnum.class));
        }

        if (enumOptional.isPresent()) {
            ApiModelPropertyEnum anEnum = enumOptional.get();
            String enumInfo = BaseEnum.getInfo(anEnum.value());
            context.getBuilder()
                    .required(enumOptional.transform(toIsRequired()).or(false))
                    .description(anEnum.enumDesc() + ":" + enumInfo)
                    .example(enumOptional.transform(toExample()).orNull())
                    .isHidden(anEnum.hidden());
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    static Function<ApiModelPropertyEnum, Boolean> toIsRequired() {
        return annotation -> annotation.required();
    }

    public static Optional<ApiModelPropertyEnum> findApiModePropertyAnnotation(AnnotatedElement annotated) {
        return Optional.fromNullable(AnnotationUtils.getAnnotation(annotated, ApiModelPropertyEnum.class));
    }

    static Function<ApiModelPropertyEnum, String> toExample() {
        return annotation -> {
            String example = annotation.example();
            if (StringUtils.isBlank(example)) {
                return "";
            }
            return example;
        };
    }
}
