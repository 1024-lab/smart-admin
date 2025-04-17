package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.constraints.NotBlank;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public interface SprinklerRepository extends IService<SprinklerEntity> {
    List<SprinklerVO> queryPage(Page<?> page, SprinklerQueryForm queryForm);

    SprinklerEntity queryBySprinklerSerial(@NotBlank(message = "喷头序列号不能为空") @Length(max = 20, message = "sprinklerSerial最多20字符") String sprinklerSerial, Object o, Boolean aFalse);

    List<SprinklerEntity> getListBySprinklerSerial();
}
