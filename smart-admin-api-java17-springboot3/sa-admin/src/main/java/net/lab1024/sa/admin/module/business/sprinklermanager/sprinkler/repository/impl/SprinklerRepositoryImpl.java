package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.SprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SprinklerRepositoryImpl extends ServiceImpl<SprinklerDao, SprinklerEntity> implements SprinklerRepository {
    @Override
    public List<SprinklerVO> queryPage(Page<?> page, SprinklerQueryForm queryForm) {
        return baseMapper.queryPage(page, queryForm);
    }
}
