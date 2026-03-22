package net.lab1024.sa.admin.module.business.ai.merch.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.merch.domain.entity.AiMerchTaskEntity;
import net.lab1024.sa.admin.module.business.ai.merch.domain.form.AiMerchTaskQueryForm;
import net.lab1024.sa.admin.module.business.ai.merch.domain.vo.AiMerchTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiMerchTaskDao extends BaseMapper<AiMerchTaskEntity> {

    List<AiMerchTaskVO> queryPage(Page<?> page, @Param("queryForm") AiMerchTaskQueryForm queryForm);
}
