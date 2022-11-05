package net.lab1024.sa.common.common.swagger;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.swagger2.mappers.ModelMapper;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2MapperImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static springfox.documentation.builders.BuilderDefaults.nullToEmptyList;

/**
 * 修改 api 顺序
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021/8/11 22:05
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@ConditionalOnBean(ModelMapper.class)
@Component
@Primary
public class Swagger2MapperImplExtension extends ServiceModelToSwagger2MapperImpl {

    @Override
    protected Map<String, Path> mapApiListings(Multimap<String, ApiListing> apiListings) {
        Map<String, Path> paths = new LinkedHashMap<>();
        Multimap<String, ApiListing> apiListingMap = LinkedListMultimap.create();
        Iterator iter = apiListings.entries().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, ApiListing> entry = (Map.Entry<String, ApiListing>) iter.next();
            ApiListing apis = entry.getValue();
            List<ApiDescription> apiList = apis.getApis();
            apiList.sort((left, right) -> {
                int position1 = left.getOperations().get(0).getPosition();
                int position2 = right.getOperations().get(0).getPosition();
                if (position1 == position2) {
                    return String.CASE_INSENSITIVE_ORDER.compare(left.getPath(), right.getPath());
                }
                return Integer.compare(position1, position2);
            });
            try {
                // 因ApiListing的属性都是final故需要通过反射来修改值
                modify(apis, "apis", apiList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            apiListingMap.put(entry.getKey(), apis);
        }

        for (ApiListing each : apiListingMap.values()) {
            for (ApiDescription api : each.getApis()) {
                paths.put(api.getPath(), mapOperations(api, Optional.ofNullable(paths.get(api.getPath()))));
            }
        }
        return paths;
    }

    private Path mapOperations(ApiDescription api, Optional<Path> existingPath) {
        Path path = existingPath.orElse(new Path());
        for (springfox.documentation.service.Operation each : nullToEmptyList(api.getOperations())) {
            Operation operation = mapOperation(each);
            path.set(each.getMethod().toString().toLowerCase(), operation);
        }
        return path;
    }

    public static void modify(Object object, String fieldName, Object newFieldValue) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        field.set(object, newFieldValue);
    }
}
