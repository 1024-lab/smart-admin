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

    /**
     * JS 安全整数范围
     * 根据 JS Number.MIN_SAFE_INTEGER 与 Number.MAX_SAFE_INTEGER 得来
     */
    private static final long JS_MIN_SAFE_INTEGER = -9007199254740991L;
    private static final long JS_MAX_SAFE_INTEGER = 9007199254740991L;


    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (null == value) {
            gen.writeNull();
            return;
        }
        // 如果超出了 JavaScript 安全整数范围，则序列化为字符串
        if (value < JS_MIN_SAFE_INTEGER || value > JS_MAX_SAFE_INTEGER) {
            gen.writeString(Long.toString(value));
        } else {
            // 否则，序列化为数字
            gen.writeNumber(value);
        }
    }
}