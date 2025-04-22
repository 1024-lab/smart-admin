package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant;

import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.BaseCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.DamagedSprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MachineSprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.MaintainingSprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.RmaSprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerCreateForm;

public enum RepositorySprinklerClazzEnum {
    USABLE(0, UsableSprinklerCreateForm.class),
    MACHINE(1, MachineSprinklerCreateForm.class),
    MAINTAINING(2, MaintainingSprinklerCreateForm.class),
    DAMAGED(3, DamagedSprinklerCreateForm.class),
    RMA(4, RmaSprinklerCreateForm.class),
    ;

    private final int code;
    private final Class<? extends BaseCreateForm> clazz;

    RepositorySprinklerClazzEnum(int code, Class<? extends BaseCreateForm> clazz) {
        this.code = code;
        this.clazz = clazz;
    }

    // 根据整型code获取枚举对象（关键转换）
    public static RepositorySprinklerClazzEnum fromCode(int code) {
        for (RepositorySprinklerClazzEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    // 获取关联的Class对象
    public Class<? extends BaseCreateForm> getSprinklerClass() {
        return this.clazz;
    }
}
