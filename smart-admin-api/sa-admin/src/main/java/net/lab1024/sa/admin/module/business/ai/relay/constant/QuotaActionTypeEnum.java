package net.lab1024.sa.admin.module.business.ai.relay.constant;

import net.lab1024.sa.base.common.enumeration.BaseEnum;

public enum QuotaActionTypeEnum implements BaseEnum {

    RECHARGE(1, "充值"),
    PRECONSUME(2, "预扣"),
    SETTLE_EXTRA(3, "结算补扣"),
    SETTLE_REFUND(4, "结算返还"),
    ROLLBACK(5, "回滚");

    private final Integer value;
    private final String desc;

    QuotaActionTypeEnum(Integer value, String desc) {
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
