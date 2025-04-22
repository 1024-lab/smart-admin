package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.service;

import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SprinklerService{
    ResponseDTO<PageResult<SprinklerVO>> queryByPage(@Valid SprinklerQueryForm queryForm, Byte type);

    ResponseDTO<String> batchImport(@Valid MultipartFile file, RequestUser requestUser);

    void createBatchSprinkler(List<SprinklerCreateForm> createVOs);
}
