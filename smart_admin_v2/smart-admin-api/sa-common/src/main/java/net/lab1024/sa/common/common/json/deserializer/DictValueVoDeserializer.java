package net.lab1024.sa.common.common.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.module.support.dict.domain.vo.DictValueVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典反序列化
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-08-12 22:17:53
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
public class DictValueVoDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        List<DictValueVO> list = new ArrayList<>();
        ObjectCodec objectCodec = jsonParser.getCodec();
        JsonNode listOrObjectNode = objectCodec.readTree(jsonParser);
        String deserialize = "";
        try {
            if (listOrObjectNode.isArray()) {
                for (JsonNode node : listOrObjectNode) {
                    list.add(objectCodec.treeToValue(node, DictValueVO.class));
                }
            } else {
                list.add(objectCodec.treeToValue(listOrObjectNode, DictValueVO.class));
            }
            deserialize = list.stream().map(DictValueVO::getValueCode).collect(Collectors.joining(","));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            deserialize = listOrObjectNode.asText();
        }
        return deserialize;
    }


}
