package net.lab1024.sa.base.common.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.Lists;
import net.lab1024.sa.base.module.support.file.domain.vo.FileVO;
import net.lab1024.sa.base.module.support.file.service.FileService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 文件key进行序列化对象
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020/8/15 22:06
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public class FileKeyVoSerializer extends JsonSerializer<String> {

    @Resource
    private FileService fileService;


    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isEmpty(value)) {
            jsonGenerator.writeObject(Lists.newArrayList());
            return;
        }
        if(fileService == null){
            jsonGenerator.writeString(value);
            return;
        }
        String[] fileKeyArray = value.split(",");
        List<String> fileKeyList = Arrays.asList(fileKeyArray);
        List<FileVO> fileKeyVOList = fileService.getFileList(fileKeyList);
        jsonGenerator.writeObject(fileKeyVOList);
    }
}
