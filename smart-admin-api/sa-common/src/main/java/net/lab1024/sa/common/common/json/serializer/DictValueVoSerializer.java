package net.lab1024.sa.common.common.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.Lists;
import net.lab1024.sa.common.module.support.dict.domain.vo.DictValueVO;
import net.lab1024.sa.common.module.support.dict.service.DictCacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 字典序列化
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022-08-12 22:17:53
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
public class DictValueVoSerializer extends JsonSerializer<String> {

    @Autowired
    private DictCacheService dictCacheService;


    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isEmpty(value)) {
            jsonGenerator.writeObject(Lists.newArrayList());
            return;
        }

        String[] valueCodeArray = value.split(",");
        List<String> valueCodeList = Arrays.asList(valueCodeArray);
        List<DictValueVO> dictValueVOList = Lists.newArrayList();
        valueCodeList.forEach(e->{
            if(StringUtils.isNotBlank(e)){
                DictValueVO dictValueVO = dictCacheService.selectValueByValueCode(value);
                if(dictValueVO != null){
                    dictValueVOList.add(dictValueVO);
                }
            }
        });
        jsonGenerator.writeObject(dictValueVOList);

    }
}
