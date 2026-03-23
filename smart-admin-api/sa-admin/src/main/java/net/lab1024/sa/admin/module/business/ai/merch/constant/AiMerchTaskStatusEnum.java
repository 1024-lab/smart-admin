package net.lab1024.sa.admin.module.business.ai.merch.constant;

public enum AiMerchTaskStatusEnum {

    WAITING("WAITING", "待执行"),
    PROCESSING("PROCESSING", "处理中"),
    SUCCESS("SUCCESS", "成功"),
    FAILED("FAILED", "失败");

    private final String value;
    private final String desc;

    AiMerchTaskStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
