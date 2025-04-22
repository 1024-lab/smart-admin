package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;

import java.util.List;

public interface SprinklerRepository extends IService<SprinklerEntity> {
    List<SprinklerEntity> getListBySprinklerSerials(List<String> sprinklerSerial);
}
