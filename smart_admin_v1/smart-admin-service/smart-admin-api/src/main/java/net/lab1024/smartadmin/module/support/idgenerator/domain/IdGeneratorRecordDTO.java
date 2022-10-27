package net.lab1024.smartadmin.module.support.idgenerator.domain;

import lombok.Data;

/**
 * @Auther: yandanyang
 * @Date: 2018/8/7 0007 13:33
 * @Description:
 */
@Data
public class IdGeneratorRecordDTO {

    private Long generatorId;

    private Integer year;

    private Integer month;

    private Integer day;

    private Long lastNumber;
}
