package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant;


import lombok.Getter;
import net.lab1024.sa.base.common.enumeration.BaseEnum;

/**
 * 喷头仓库类型
 *
 * @Author 海印：芦苇
 */
@Getter
public enum RepositorySprinklerTypeEnum implements BaseEnum {

    /**
     * 可用仓
     */
    USABLE_REPOSITORY(0, "Usable"),

    /**
     * 机台
     */
    MACHINE_REPOSITORY(1, "Machine"),

    /**
     * 维修仓
     */
    MAINTAINING_REPOSITORY(2, "Maintaining"),

    /**
     * 破损仓
     */
    DAMAGED_REPOSITORY(3, "Damaged"),

    /**
     * RMA
     */
    RMA_REPOSITORY(4, "Rma"),
    ;

    private Integer value;
    private String desc;

    RepositorySprinklerTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }


}
