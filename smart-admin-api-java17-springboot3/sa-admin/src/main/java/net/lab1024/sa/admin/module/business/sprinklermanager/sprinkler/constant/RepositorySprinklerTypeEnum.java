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
    USABLE_REPOSITORY(0, "usable"),

    /**
     * 机台
     */
    MACHINE_REPOSITORY(1, "machine"),

    /**
     * 维修仓
     */
    MAINTAINING_REPOSITORY(2, "maintaining"),

    /**
     * 破损仓
     */
    DAMAGED_REPOSITORY(3, "damaged"),

    /**
     * RMA
     */
    RMA_REPOSITORY(4, "rma"),
    ;

    private Integer value;
    private String desc;

    RepositorySprinklerTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }


}
