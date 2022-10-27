package net.lab1024.smartadmin.module.support.idgenerator.domain;

import java.util.Date;

/**
 * @Auther: yandanyang
 * @Date: 2018/8/7 0007 13:33
 * @Description:
 */
public class IdGeneratorLastNumberDTO {
    private Date updateTime;
    private Long lastNumber;
    private Date databaseTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(Long lastNumber) {
        this.lastNumber = lastNumber;
    }

    public Date getDatabaseTime() {
        return databaseTime;
    }

    public void setDatabaseTime(Date databaseTime) {
        this.databaseTime = databaseTime;
    }
}
