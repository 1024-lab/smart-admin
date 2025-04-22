package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.SprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SprinklerRepositoryImpl extends ServiceImpl<SprinklerDao, SprinklerEntity> implements SprinklerRepository {
    @Override
    public List<SprinklerEntity> getListBySprinklerSerials(List<String> sprinklerSerials) {
        LambdaQueryWrapper<SprinklerEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SprinklerEntity::getSprinklerSerial, sprinklerSerials);
        return this.list(queryWrapper);
    }
}
