package net.lab1024.sa.admin.module.business.ai.relay.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.constant.RelayErrorCode;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayTokenDao;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayTokenEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayTokenAddForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayTokenQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayTokenUpdateForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayTokenCreateResultVO;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayTokenVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;

@Service
public class RelayTokenAdminService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String TOKEN_PREFIX = "sk-sa-";

    @Resource
    private RelayTokenDao relayTokenDao;

    public PageResult<RelayTokenVO> queryPage(RelayTokenQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<RelayTokenVO> list = relayTokenDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    public ResponseDTO<RelayTokenCreateResultVO> add(RelayTokenAddForm addForm) {
        // 生成随机 token
        byte[] randomBytes = new byte[32];
        RANDOM.nextBytes(randomBytes);
        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        String plainToken = TOKEN_PREFIX + randomPart;
        String prefix = plainToken.substring(0, 10);
        String hash = sha256(plainToken);

        RelayTokenEntity entity = new RelayTokenEntity();
        entity.setEmployeeId(addForm.getEmployeeId());
        entity.setTokenName(addForm.getTokenName());
        entity.setTokenPrefix(prefix);
        entity.setTokenHash(hash);
        entity.setExpiresTime(addForm.getExpiresTime());
        entity.setRemark(addForm.getRemark());
        entity.setRequestCount(0L);
        entity.setDisabledFlag(0);
        entity.setDeletedFlag(0);
        relayTokenDao.insert(entity);

        RelayTokenCreateResultVO result = new RelayTokenCreateResultVO();
        result.setTokenId(entity.getTokenId());
        result.setTokenName(entity.getTokenName());
        result.setPlainToken(plainToken);
        result.setTokenPrefix(prefix);
        return ResponseDTO.ok(result);
    }

    public ResponseDTO<String> update(RelayTokenUpdateForm updateForm) {
        RelayTokenEntity entity = relayTokenDao.selectById(updateForm.getTokenId());
        if (entity == null || entity.getDeletedFlag() == 1) {
            return ResponseDTO.error(RelayErrorCode.TOKEN_NOT_FOUND);
        }
        if (updateForm.getTokenName() != null) {
            entity.setTokenName(updateForm.getTokenName());
        }
        if (updateForm.getExpiresTime() != null) {
            entity.setExpiresTime(updateForm.getExpiresTime());
        }
        if (updateForm.getDisabledFlag() != null) {
            entity.setDisabledFlag(updateForm.getDisabledFlag());
        }
        if (updateForm.getRemark() != null) {
            entity.setRemark(updateForm.getRemark());
        }
        relayTokenDao.updateById(entity);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> delete(Long tokenId) {
        RelayTokenEntity entity = relayTokenDao.selectById(tokenId);
        if (entity == null) {
            return ResponseDTO.ok();
        }
        entity.setDeletedFlag(1);
        relayTokenDao.updateById(entity);
        return ResponseDTO.ok();
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256计算失败", e);
        }
    }
}
