package net.lab1024.sa.admin.module.business.ai.relay.service.routing;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.relay.constant.EndpointTypeEnum;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayChannelDao;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayChannelEntity;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 优先级 + 权重随机路由
 * <p>
 * 1. 查询 enabled 且支持目标模型的渠道，排除已失败渠道
 * 2. 按 priority ASC 分组，取最高优先级组
 * 3. 组内按 weight 加权随机选择
 */
@Slf4j
@Component
public class PriorityWeightedRouter implements ChannelRouter {

    @Resource
    private RelayChannelDao relayChannelDao;

    @Override
    public RelayChannelEntity selectChannel(String modelName, EndpointTypeEnum endpointType, Set<Long> excludeChannelIds) {
        List<RelayChannelEntity> candidates = relayChannelDao.selectEnabledByModelAndEndpoint(
                modelName, endpointType.getValue(), excludeChannelIds);

        if (candidates == null || candidates.isEmpty()) {
            return null;
        }

        // 已按 priority ASC 排序，取最高优先级（最小值）
        int highestPriority = candidates.get(0).getPriority();
        List<RelayChannelEntity> topGroup = candidates.stream()
                .filter(c -> c.getPriority() == highestPriority)
                .collect(Collectors.toList());

        if (topGroup.size() == 1) {
            return topGroup.get(0);
        }

        // 加权随机
        return weightedRandom(topGroup);
    }

    private RelayChannelEntity weightedRandom(List<RelayChannelEntity> channels) {
        int totalWeight = 0;
        for (RelayChannelEntity ch : channels) {
            totalWeight += (ch.getWeight() != null && ch.getWeight() > 0) ? ch.getWeight() : 1;
        }

        int random = ThreadLocalRandom.current().nextInt(totalWeight);
        int cumulative = 0;
        for (RelayChannelEntity ch : channels) {
            cumulative += (ch.getWeight() != null && ch.getWeight() > 0) ? ch.getWeight() : 1;
            if (random < cumulative) {
                return ch;
            }
        }

        // fallback
        return channels.get(0);
    }
}
