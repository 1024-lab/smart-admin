package net.lab1024.sa.admin.module.business.oa.notice.service;

import cn.hutool.core.util.StrUtil;
import net.lab1024.sa.admin.module.business.oa.notice.dao.NoticeTypeDao;
import net.lab1024.sa.admin.module.business.oa.notice.domain.entity.NoticeTypeEntity;
import net.lab1024.sa.admin.module.business.oa.notice.domain.vo.NoticeTypeVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 通知。公告 类型
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class NoticeTypeService {

    @Resource
    private NoticeTypeDao noticeTypeDao;

    /**
     * 查询全部
     * @return
     */
    public List<NoticeTypeVO> getAll() {
        return SmartBeanUtil.copyList(noticeTypeDao.selectList(null), NoticeTypeVO.class);
    }

    public NoticeTypeVO getByNoticeTypeId(Long noticceTypeId) {
        return SmartBeanUtil.copy(noticeTypeDao.selectById(noticceTypeId), NoticeTypeVO.class);
    }

    public synchronized ResponseDTO<String> add(String name) {
        if (StrUtil.isBlank(name)) {
            return ResponseDTO.userErrorParam("类型名称不能为空");
        }

        List<NoticeTypeEntity> noticeTypeEntityList = noticeTypeDao.selectList(null);
        if (!CollectionUtils.isEmpty(noticeTypeEntityList)) {
            boolean exist = noticeTypeEntityList.stream().map(NoticeTypeEntity::getNoticeTypeName).collect(Collectors.toSet()).contains(name);
            if (exist) {
                return ResponseDTO.userErrorParam("类型名称已经存在");
            }
        }
        noticeTypeDao.insert(NoticeTypeEntity.builder().noticeTypeName(name).build());
        return ResponseDTO.ok();
    }

    public synchronized ResponseDTO<String> update(Long noticeTypeId, String name) {
        if (StrUtil.isBlank(name)) {
            return ResponseDTO.userErrorParam("类型名称不能为空");
        }

        NoticeTypeEntity noticeTypeEntity = noticeTypeDao.selectById(noticeTypeId);
        if (noticeTypeEntity == null) {
            return ResponseDTO.userErrorParam("类型名称不存在");
        }

        List<NoticeTypeEntity> noticeTypeEntityList = noticeTypeDao.selectList(null);
        if (!CollectionUtils.isEmpty(noticeTypeEntityList)) {
            Optional<NoticeTypeEntity> optionalNoticeTypeEntity = noticeTypeEntityList.stream().filter(e -> e.getNoticeTypeName().equals(name)).findFirst();
            if (optionalNoticeTypeEntity.isPresent() && !optionalNoticeTypeEntity.get().getNoticeTypeId().equals(noticeTypeId)) {
                return ResponseDTO.userErrorParam("类型名称已经存在");
            }
        }
        noticeTypeEntity.setNoticeTypeName(name);
        noticeTypeDao.updateById(noticeTypeEntity);
        return ResponseDTO.ok();
    }

    public synchronized ResponseDTO<String> delete(Long noticeTypeId) {
        noticeTypeDao.deleteById(noticeTypeId);
        return ResponseDTO.ok();
    }

}
