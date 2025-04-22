package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory;

import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerClazzEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.BaseCreateForm;
import org.springframework.stereotype.Component;

@Component
public class RepositorySprinklerCreateFormFactory {
    private RepositorySprinklerClazzEnum type;

    // 通过枚举类型直接获取Class
    public Class<? extends BaseCreateForm> getSprinklerClass(int typeCode) {
        type = RepositorySprinklerClazzEnum.fromCode(typeCode);
        return type.getSprinklerClass();
    }

    // 若需要直接实例化对象（可选）
    public Object createSprinkler(int typeCode) throws Exception {
        return type.getSprinklerClass().getDeclaredConstructor().newInstance();
    }
}
