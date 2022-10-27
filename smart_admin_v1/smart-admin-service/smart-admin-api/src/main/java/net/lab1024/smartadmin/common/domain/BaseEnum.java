package net.lab1024.smartadmin.common.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author listen
 * @date 2018-07-17 下午 3:52
 */
public interface BaseEnum {

    /**
     * 获取枚举类的值
     *
     * @return Object
     */
    Object getValue();

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    String getDesc();

    /**
     * 比较参数是否与枚举类的value相同
     *
     * @param value
     * @return boolean
     */
    default boolean equalsValue(Object value) {
        return Objects.equals(getValue(), value);
    }

    /**
     * 比较枚举类是否相同
     *
     * @param baseEnum
     * @return boolean
     */
    default boolean equals(BaseEnum baseEnum) {
        return Objects.equals(getValue(), baseEnum.getValue()) && Objects.equals(getDesc(), baseEnum.getDesc());
    }

    /**
     * 返回枚举类的说明
     *
     * @param clazz 枚举类类对象
     * @return
     */
    static String getInfo(Class<? extends BaseEnum> clazz) {
        BaseEnum[] enums = clazz.getEnumConstants();
        LinkedHashMap<String, JSONObject> json = new LinkedHashMap<>(enums.length);
        for (BaseEnum e : enums) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", new DeletedQuotationAware(e.getValue()));
            jsonObject.put("desc", new DeletedQuotationAware(e.getDesc()));
            json.put(e.toString(), jsonObject);
        }

        String enumJson = JSON.toJSONString(json, true);
        enumJson = enumJson.replaceAll("\"", "");
        enumJson= enumJson.replaceAll("\t","&nbsp;&nbsp;");
        enumJson = enumJson.replaceAll("\n","<br>");
        String prefix = "  <br>  export const <br> " + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, clazz.getSimpleName() + " = <br> ");
        return prefix + "" + enumJson + " <br>";
    }


    @Data
    class DeletedQuotationAware implements JSONAware {

        private String value;

        public DeletedQuotationAware(Object value) {
            if(value == null){
                this.value = "";
            }else if (value instanceof String) {
                this.value = "'" + value + "'";
            }else {
                this.value = value.toString();
            }
        }

        @Override
        public String toJSONString() {
            return value;
        }
    }
}
