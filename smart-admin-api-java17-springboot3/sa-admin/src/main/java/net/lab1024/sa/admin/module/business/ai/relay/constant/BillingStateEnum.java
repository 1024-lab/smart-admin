package net.lab1024.sa.admin.module.business.ai.relay.constant;

import net.lab1024.sa.base.common.enumeration.BaseEnum;

public enum BillingStateEnum implements BaseEnum {

    PRECONSUMED(0, "预扣中"),
    SETTLED(1, "已结算"),
    REFUNDED(2, "已退款"),
    FAILED(3, "结算失败");

    private final Integer value;
    private final String desc;

    BillingStateEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
