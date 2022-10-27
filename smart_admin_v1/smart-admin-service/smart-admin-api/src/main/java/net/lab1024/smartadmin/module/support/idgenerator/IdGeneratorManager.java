package net.lab1024.smartadmin.module.support.idgenerator;

import net.lab1024.smartadmin.common.exception.SmartBusinessException;
import net.lab1024.smartadmin.module.support.idgenerator.domain.IdGeneratorLastNumberDTO;
import net.lab1024.smartadmin.module.support.idgenerator.domain.IdGeneratorPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * 全局id生成器
 * zhuo
 */
@Service
public class IdGeneratorManager {

    @Autowired
    private IdGeneratorDao idGeneratorDao;

    @Transactional(rollbackFor = Exception.class)
    public long[] generate(IdGeneratorPOJO idGeneratorPOJO, int stepLength) {
        IdGeneratorLastNumberDTO idGeneratorLastNumberDTO = idGeneratorDao.selectLastNumber(idGeneratorPOJO.getIdGeneratorEntity().getId());
        if (idGeneratorLastNumberDTO == null) {
            throw new SmartBusinessException("IdGenerator， id 数据库不存在" + idGeneratorPOJO.getIdGeneratorEntity().getId());
        }

        Long lastNumber = idGeneratorLastNumberDTO.getLastNumber();
        if (lastNumber == null) {
            lastNumber = idGeneratorPOJO.getIdGeneratorEntity().getInitNumber();
        } else {
            lastNumber = lastNumber + 1;
        }

        Date updateTime = idGeneratorLastNumberDTO.getUpdateTime();
        if (updateTime == null) {
            updateTime = idGeneratorLastNumberDTO.getDatabaseTime();
        }

        Long startValue = -1L, endValue = -1L;
        switch (idGeneratorPOJO.getIdGeneratorRuleTypeEnum()) {
            case NO_CYCLE:
                startValue = lastNumber.longValue();
                endValue = startValue + stepLength;
                break;
            default:
                SimpleDateFormat format = new SimpleDateFormat(idGeneratorPOJO.getIdGeneratorRuleTypeEnum().getExt());
                if (format.format(idGeneratorLastNumberDTO.getDatabaseTime()).equals(format.format(updateTime))) {
                    startValue = lastNumber.longValue();
                    endValue = startValue + stepLength;
                } else {
                    startValue = idGeneratorPOJO.getIdGeneratorEntity().getInitNumber();
                    endValue = startValue + stepLength;
                }
                break;
        }

        idGeneratorDao.updateLastNumber(idGeneratorPOJO.getIdGeneratorEntity().getId(), endValue - 1);
        LocalDate localDate = LocalDate.now();
        idGeneratorDao.replaceIdGeneratorRecord(idGeneratorPOJO.getIdGeneratorEntity().getId(), localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), endValue - 1);
        return new long[]{startValue, endValue};
    }
}
