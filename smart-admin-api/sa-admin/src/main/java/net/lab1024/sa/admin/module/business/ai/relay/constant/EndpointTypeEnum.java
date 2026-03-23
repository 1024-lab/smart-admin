package net.lab1024.sa.admin.module.business.ai.relay.constant;

import net.lab1024.sa.base.common.enumeration.BaseEnum;

public enum EndpointTypeEnum implements BaseEnum {

    CHAT(1, "Chat"),
    EMBEDDING(2, "Embedding"),
    BOTH(3, "Chat+Embedding");

    private final Integer value;
    private final String desc;

    EndpointTypeEnum(Integer value, String desc) {
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
