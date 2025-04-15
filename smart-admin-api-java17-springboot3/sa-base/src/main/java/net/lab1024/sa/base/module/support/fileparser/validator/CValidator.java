package net.lab1024.sa.base.module.support.fileparser.validator;


import java.util.regex.Pattern;
//imp 增强校验器
public class CValidator {
    //可排序格式正则
    private static final Pattern SORTABLE_PATTEN = Pattern.compile("^\\d{6}-\\d{2}$");

    private static final Pattern UNSORTABLE_PATTEN = Pattern.compile("^[A-Z0-9]{2}L\\d+$");

    public static boolean isSortable(String value){
        if(value==null || value.trim().isEmpty()){
            return false;
        }
        return SORTABLE_PATTEN.matcher(value).matches();
    }

    public static boolean isValid(String value){
        if(value==null || value.trim().isEmpty()){
            return false;
        }
        return isSortable(value) || UNSORTABLE_PATTEN.matcher(value).matches();
    }
}
