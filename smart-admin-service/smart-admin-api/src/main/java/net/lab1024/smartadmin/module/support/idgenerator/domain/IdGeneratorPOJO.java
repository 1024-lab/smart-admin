package net.lab1024.smartadmin.module.support.idgenerator.domain;

import net.lab1024.smartadmin.module.support.idgenerator.constant.IdGeneratorRuleTypeEnum;

/**
 * @Auther: yandanyang
 * @Date: 2018/8/7 0007 13:33
 * @Description:
 */
public class IdGeneratorPOJO {

    private IdGeneratorRuleTypeEnum idGeneratorRuleTypeEnum;
    private IdGeneratorEntity idGeneratorEntity;
    private int numberCount = 0;
    private boolean haveYear = false;
    private boolean haveMonth = false;
    private boolean haveDay = false;

    public IdGeneratorPOJO(IdGeneratorRuleTypeEnum idGeneratorRuleTypeEnum, IdGeneratorEntity idGeneratorEntity) {
        this.idGeneratorRuleTypeEnum = idGeneratorRuleTypeEnum;
        this.idGeneratorEntity = idGeneratorEntity;
    }

    public IdGeneratorRuleTypeEnum getIdGeneratorRuleTypeEnum() {
        return idGeneratorRuleTypeEnum;
    }

    public void setIdGeneratorRuleTypeEnum(IdGeneratorRuleTypeEnum idGeneratorRuleTypeEnum) {
        this.idGeneratorRuleTypeEnum = idGeneratorRuleTypeEnum;
    }

    public IdGeneratorEntity getIdGeneratorEntity() {
        return idGeneratorEntity;
    }

    public void setIdGeneratorEntity(IdGeneratorEntity idGeneratorEntity) {
        this.idGeneratorEntity = idGeneratorEntity;
    }

    public int getNumberCount() {
        return numberCount;
    }

    public void setNumberCount(int numberCount) {
        this.numberCount = numberCount;
    }

    public boolean isHaveYear() {
        return haveYear;
    }

    public void setHaveYear(boolean haveYear) {
        this.haveYear = haveYear;
    }

    public boolean isHaveMonth() {
        return haveMonth;
    }

    public void setHaveMonth(boolean haveMonth) {
        this.haveMonth = haveMonth;
    }

    public boolean isHaveDay() {
        return haveDay;
    }

    public void setHaveDay(boolean haveDay) {
        this.haveDay = haveDay;
    }
}
