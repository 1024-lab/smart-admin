package net.lab1024.sa.base.module.support.helpdoc.service;

import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.module.support.helpdoc.dao.HelpDocCatalogDao;
import net.lab1024.sa.base.module.support.helpdoc.dao.HelpDocDao;
import net.lab1024.sa.base.module.support.helpdoc.domain.entity.HelpDocCatalogEntity;
import net.lab1024.sa.base.module.support.helpdoc.domain.form.HelpDocCatalogAddForm;
import net.lab1024.sa.base.module.support.helpdoc.domain.form.HelpDocCatalogUpdateForm;
import net.lab1024.sa.base.module.support.helpdoc.domain.vo.HelpDocCatalogVO;
import net.lab1024.sa.base.module.support.helpdoc.domain.vo.HelpDocVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 帮助文档 目录
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-20 23:11:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class HelpDocCatalogService {

    @Resource
    private HelpDocCatalogDao helpDocCatalogDao;

    @Resource
    private HelpDocDao helpDocDao;

    /**
     * 查询全部目录
     *
     * @return
     */
    public List<HelpDocCatalogVO> getAll() {
        return SmartBeanUtil.copyList(helpDocCatalogDao.selectList(null), HelpDocCatalogVO.class);
    }

    /**
     * 添加目录
     *
     * @param helpDocCatalogAddForm
     * @return
     */
    public synchronized ResponseDTO<String> add(HelpDocCatalogAddForm helpDocCatalogAddForm) {
        List<HelpDocCatalogVO> helpDocCatalogList = getAll();
        Optional<HelpDocCatalogVO> exist = helpDocCatalogList.stream().filter(e -> helpDocCatalogAddForm.getName().equals(e.getName())).findFirst();
        if (exist.isPresent()) {
            return ResponseDTO.userErrorParam("存在相同名称的目录了");
        }

        helpDocCatalogDao.insert(SmartBeanUtil.copy(helpDocCatalogAddForm, HelpDocCatalogEntity.class));
        return ResponseDTO.ok();
    }

    /**
     * 更新目录
     *
     * @param updateForm
     * @return
     */
    public synchronized ResponseDTO<String> update(HelpDocCatalogUpdateForm updateForm) {
        HelpDocCatalogEntity helpDocCatalogEntity = helpDocCatalogDao.selectById(updateForm.getHelpDocCatalogId());
        if (helpDocCatalogEntity == null) {
            return ResponseDTO.userErrorParam("目录不存在");
        }

        List<HelpDocCatalogVO> helpDocCatalogList = getAll();
        Optional<HelpDocCatalogVO> exist = helpDocCatalogList.stream().filter(e -> updateForm.getName().equals(e.getName())).findFirst();
        if (exist.isPresent() && !exist.get().getHelpDocCatalogId().equals(updateForm.getHelpDocCatalogId())) {
            return ResponseDTO.userErrorParam("存在相同名称的目录了");
        }
        helpDocCatalogDao.updateById(SmartBeanUtil.copy(updateForm, HelpDocCatalogEntity.class));
        return ResponseDTO.ok();
    }

    /**
     * 删除目录（如果有子目录、或者有帮助文档，则不能删除）
     *
     * @param helpDocCatalogId
     * @return
     */
    public synchronized ResponseDTO<String> delete(Long helpDocCatalogId) {
        if (helpDocCatalogId == null) {
            return ResponseDTO.ok();
        }

        HelpDocCatalogEntity helpDocCatalogEntity = helpDocCatalogDao.selectById(helpDocCatalogId);
        if (helpDocCatalogEntity == null) {
            return ResponseDTO.userErrorParam("目录不存在");
        }

        //如果有子目录，则不能删除
        Optional<HelpDocCatalogVO> existOptional = getAll().stream().filter(e -> helpDocCatalogId.equals(e.getParentId())).findFirst();
        if (existOptional.isPresent()) {
            return ResponseDTO.userErrorParam("存在子目录：" + existOptional.get().getName());
        }

        //查询是否有帮助文档
        List<HelpDocVO> helpDocVOList = helpDocDao.queryHelpDocByCatalogId(helpDocCatalogId);
        if (CollectionUtils.isNotEmpty(helpDocVOList)) {
            return ResponseDTO.userErrorParam("目录下存在文档，不能删除");
        }
        helpDocCatalogDao.deleteById(helpDocCatalogId);
        return ResponseDTO.ok();
    }

}
