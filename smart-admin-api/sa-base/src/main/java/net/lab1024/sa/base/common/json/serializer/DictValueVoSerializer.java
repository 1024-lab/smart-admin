package net.lab1024.sa.base.common.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.Lists;
import net.lab1024.sa.base.module.support.dict.domain.vo.DictValueVO;
import net.lab1024.sa.base.module.support.dict.service.DictCacheService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
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
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public class DictValueVoSerializer extends JsonSerializer<String> {

    @Resource
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
                DictValueVO dictValueVO = dictCacheService.selectValueByValueCode(e);
                if(dictValueVO != null){
                    dictValueVOList.add(dictValueVO);
                }
            }
        });
        jsonGenerator.writeObject(dictValueVOList);

    }
}
