package net.lab1024.sa.admin.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesNoEnum {

    NO("0", "否"),
    YES("1", "是");

    private final String value;
    private final String desc;
}
