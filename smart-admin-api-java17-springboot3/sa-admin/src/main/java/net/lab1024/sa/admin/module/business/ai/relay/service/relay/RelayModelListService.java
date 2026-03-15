package net.lab1024.sa.admin.module.business.ai.relay.service.relay;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayModelDao;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayModelEntity;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * GET /v1/models — 返回当前启用的模型列表（OpenAI 兼容格式）
 */
@Slf4j
@Service
public class RelayModelListService {

    @Resource
    private RelayModelDao relayModelDao;

    public String listModels() {
        List<RelayModelEntity> models = relayModelDao.selectAllEnabled();

        JSONArray data = new JSONArray();
        for (RelayModelEntity m : models) {
            JSONObject item = new JSONObject();
            item.put("id", m.getModelName());
            item.put("object", "model");
            item.put("created", m.getCreateTime() != null
                    ? m.getCreateTime().toEpochSecond(java.time.ZoneOffset.ofHours(8)) : 0);
            item.put("owned_by", "relay");
            data.add(item);
        }

        JSONObject result = new JSONObject();
        result.put("object", "list");
        result.put("data", data);
        return result.toJSONString();
    }
}
