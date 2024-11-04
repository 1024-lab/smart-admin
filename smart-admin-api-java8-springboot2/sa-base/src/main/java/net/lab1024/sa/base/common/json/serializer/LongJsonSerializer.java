package net.lab1024.sa.base.common.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Long类型序列化
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2020-06-02 22:55:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
public class LongJsonSerializer extends JsonSerializer<Long> {

    public static final LongJsonSerializer INSTANCE = new LongJsonSerializer();

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (null == value) {
            gen.writeNull();
            return;
        }
        // js中最大安全整数16位 Number.MAX_SAFE_INTEGER
        String longStr = String.valueOf(value);
        if (longStr.length() > 16) {
            gen.writeString(longStr);
        } else {
            gen.writeNumber(value);
        }
    }
}