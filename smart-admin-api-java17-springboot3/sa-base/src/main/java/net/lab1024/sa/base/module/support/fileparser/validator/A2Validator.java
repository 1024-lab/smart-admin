package net.lab1024.sa.base.module.support.fileparser.validator;


import java.util.regex.Pattern;

public class A2Validator {
    //可排序格式正则
    private static final Pattern SORTABLE_PATTEN = Pattern.compile("^\\d{6}-\\d{2}$");

    private static final Pattern UNSORTABLE_PATTEN = Pattern.compile("^[A-Z0-9]{2}L\\d+$");

    public static boolean isSortable(String value){
        return SORTABLE_PATTEN.matcher(value).matches();
    }

    public static boolean isValid(String value){
        return SORTABLE_PATTEN.matcher(value).matches() || UNSORTABLE_PATTEN.matcher(value).matches();
    }
}
