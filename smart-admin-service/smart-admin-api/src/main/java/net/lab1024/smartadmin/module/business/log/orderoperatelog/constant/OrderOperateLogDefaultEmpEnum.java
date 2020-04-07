package net.lab1024.smartadmin.module.business.log.orderoperatelog.constant;

/**
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
public enum OrderOperateLogDefaultEmpEnum {

    DEFAULT_EMP(0,"系统");


    private Integer empId;

    private String empName;

    OrderOperateLogDefaultEmpEnum(Integer empId,String empName) {
        this.empId = empId;
        this.empName = empName;
    }

    public int getEmpId() {
        return empId;
    }

    public String getEmpName() {
        return empName;
    }

   
    
}

