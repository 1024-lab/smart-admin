package net.lab1024.sa.admin.module.business.ai.relay.service.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.constant.RelayErrorCode;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayChannelDao;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayChannelGroupDao;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayChannelEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayChannelGroupEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelGroupAddForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelGroupQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayChannelGroupUpdateForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayChannelGroupVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelayChannelGroupAdminService {

    @Resource
    private RelayChannelGroupDao relayChannelGroupDao;

    @Resource
    private RelayChannelDao relayChannelDao;

    public PageResult<RelayChannelGroupVO> queryPage(RelayChannelGroupQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<RelayChannelGroupVO> list = relayChannelGroupDao.queryPage(page, queryForm);
        // 解析 channel_ids 字符串为 List
        for (RelayChannelGroupVO vo : list) {
            vo.setChannelIdList(parseChannelIds(vo.getChannelIds()));
        }
        fillChannelInfo(list);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    public List<RelayChannelGroupVO> queryList(RelayChannelGroupQueryForm queryForm) {
        List<RelayChannelGroupVO> list = relayChannelGroupDao.queryList(queryForm);
        for (RelayChannelGroupVO vo : list) {
            vo.setChannelIdList(parseChannelIds(vo.getChannelIds()));
        }
        return list;
    }

    private List<Long> parseChannelIds(String channelIds) {
        if (channelIds == null || channelIds.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(channelIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public ResponseDTO<String> add(RelayChannelGroupAddForm addForm) {
        RelayChannelGroupEntity entity = new RelayChannelGroupEntity();
        entity.setChannelName(addForm.getChannelName());
        entity.setChannelIds(String.join(",", addForm.getChannelIdList().stream().map(String::valueOf).collect(Collectors.toList())));
        entity.setRemark(addForm.getRemark());
        entity.setDisabledFlag(0);
        entity.setDeletedFlag(0);
        relayChannelGroupDao.insert(entity);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> update(RelayChannelGroupUpdateForm updateForm) {
        RelayChannelGroupEntity entity = relayChannelGroupDao.selectById(updateForm.getChannelGroupId());
        if (entity == null || entity.getDeletedFlag() == 1) {
            return ResponseDTO.error(RelayErrorCode.CHANNEL_GROUP_NOT_FOUND);
        }
        entity.setChannelName(updateForm.getChannelName());
        entity.setChannelIds(String.join(",", updateForm.getChannelIdList().stream().map(String::valueOf).collect(Collectors.toList())));
        entity.setRemark(updateForm.getRemark());
        relayChannelGroupDao.updateById(entity);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> delete(Long channelGroupId) {
        RelayChannelGroupEntity entity = relayChannelGroupDao.selectById(channelGroupId);
        if (entity == null) {
            return ResponseDTO.ok();
        }
        entity.setDeletedFlag(1);
        relayChannelGroupDao.updateById(entity);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> updateDisabledFlag(Long channelGroupId, Integer disabledFlag) {
        RelayChannelGroupEntity entity = relayChannelGroupDao.selectById(channelGroupId);
        if (entity == null || entity.getDeletedFlag() == 1) {
            return ResponseDTO.error(RelayErrorCode.CHANNEL_GROUP_NOT_FOUND);
        }
        entity.setDisabledFlag(disabledFlag);
        relayChannelGroupDao.updateById(entity);
        return ResponseDTO.ok();
    }

    /**
     * 批量填充渠道名称信息
     */
    private void fillChannelInfo(List<RelayChannelGroupVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 收集所有渠道ID
        List<Long> allChannelIds = list.stream()
                .filter(vo -> vo.getChannelIdList() != null)
                .flatMap(vo -> vo.getChannelIdList().stream())
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(allChannelIds)) {
            return;
        }
        // 批量查询渠道名称
        LambdaQueryWrapper<RelayChannelEntity> wrapper = new LambdaQueryWrapper<RelayChannelEntity>()
                .select(RelayChannelEntity::getChannelId, RelayChannelEntity::getChannelName)
                .in(RelayChannelEntity::getChannelId, allChannelIds)
                .eq(RelayChannelEntity::getDeletedFlag, 0);
        Map<Long, String> channelNameMap = relayChannelDao.selectList(wrapper).stream()
                .collect(Collectors.toMap(RelayChannelEntity::getChannelId, RelayChannelEntity::getChannelName));
        // 组装到VO
        for (RelayChannelGroupVO vo : list) {
            if (CollectionUtils.isEmpty(vo.getChannelIdList())) {
                vo.setChannelNames("");
                continue;
            }
            String names = vo.getChannelIdList().stream()
                    .map(id -> channelNameMap.getOrDefault(id, String.valueOf(id)))
                    .collect(Collectors.joining(", "));
            vo.setChannelNames(names);
        }
    }
}
