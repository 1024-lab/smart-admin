package net.lab1024.sa.common.module.support.changelog.manager;

import net.lab1024.sa.common.module.support.changelog.dao.ChangeLogDao;
import net.lab1024.sa.common.module.support.changelog.domain.entity.ChangeLogEntity;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 系统更新日志  Manager
 *
 * @Author 卓大
 * @Date 2022-09-26 14:53:50
 * @Copyright 1024创新实验室
 */
@Service
public class ChangeLogManager extends ServiceImpl<ChangeLogDao, ChangeLogEntity> {


}
