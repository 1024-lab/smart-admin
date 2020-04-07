package net.lab1024.smartadmin.module.support.idgenerator;

import net.lab1024.smartadmin.common.exception.SmartBusinessException;
import net.lab1024.smartadmin.module.support.idgenerator.constant.IdGeneratorEnum;
import net.lab1024.smartadmin.module.support.idgenerator.constant.IdGeneratorRuleTypeEnum;
import net.lab1024.smartadmin.module.support.idgenerator.domain.IdGeneratorEntity;
import net.lab1024.smartadmin.module.support.idgenerator.domain.IdGeneratorPOJO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局id生成器
 * zhuo
 */
@Slf4j
@Service
public class IdGeneratorService {

    private Map<Long, IdGeneratorPOJO> idGeneratorMap;

    @Autowired
    private IdGeneratorDao idGeneratorDao;

    @Autowired
    private IdGeneratorManager idGeneratorManager;

    @PostConstruct
    void init() {
        this.idGeneratorMap = new ConcurrentHashMap<>();
        List<IdGeneratorEntity> idGeneratorEntities = idGeneratorDao.selectAll();
        if (idGeneratorEntities != null) {
            idGeneratorEntities.forEach(e -> {
                IdGeneratorRuleTypeEnum idGeneratorRuleTypeEnum = this.getIdGeneratorRuleTypeEnum(e.getRuleType());
                if (idGeneratorRuleTypeEnum != null) {
                    IdGeneratorPOJO idGeneratorPOJO = new IdGeneratorPOJO(idGeneratorRuleTypeEnum, e);
                    String ruleFormat = e.getRuleFormat();
                    int startNInx = ruleFormat.indexOf("[n");
                    int endNInx = ruleFormat.indexOf("n]");
                    idGeneratorPOJO.setNumberCount(endNInx - startNInx);
                    idGeneratorPOJO.setHaveDay(ruleFormat.contains("[dd]"));
                    idGeneratorPOJO.setHaveMonth(ruleFormat.contains("[mm]"));
                    idGeneratorPOJO.setHaveYear(ruleFormat.contains("[yyyy]"));
                    this.idGeneratorMap.put(e.getId(), idGeneratorPOJO);
                } else {
                    log.error("cannot find rule type , id : {}, key name : {} ", e.getId(), e.getKeyName());
                }
            });
        }
    }

    public String generate(IdGeneratorEnum idGeneratorEnum) {
        return generate(idGeneratorEnum, 1).get(0);
    }

    /**
     * @param idGeneratorEnum
     * @param stepLength
     * @return
     */
    public List<String> generate(IdGeneratorEnum idGeneratorEnum, int stepLength) {
        IdGeneratorPOJO idGeneratorPOJO = validateParams(idGeneratorEnum, stepLength);
        long[] generateIds = idGeneratorManager.generate(idGeneratorPOJO, stepLength);
        Long startValue = generateIds[0], endValue = generateIds[1];
        LocalDate now = LocalDate.now();
        String year = String.valueOf(now.getYear());
        String month = now.getMonthValue() > 9 ? String.valueOf(now.getMonthValue()) : "0" + now.getMonthValue();
        String day = now.getDayOfMonth() > 9 ? String.valueOf(now.getDayOfMonth()) : "0" + now.getDayOfMonth();
        ArrayList<String> codeList = new ArrayList<>();
        for (long loop = startValue; loop < endValue; loop++) {
            String generateBillCode = this.replaceAndFill(idGeneratorPOJO, loop, year, month, day);
            codeList.add(generateBillCode);
        }
        return codeList;
    }

    private IdGeneratorRuleTypeEnum getIdGeneratorRuleTypeEnum(String ruleType) {
        for (IdGeneratorRuleTypeEnum en : IdGeneratorRuleTypeEnum.values()) {
            if (en.name().equalsIgnoreCase(ruleType)) {
                return en;
            }
        }
        return null;
    }

    private IdGeneratorPOJO validateParams(IdGeneratorEnum idGeneratorEnum, int stepLength) {
        if (stepLength < 1) {
            throw new SmartBusinessException("IdGenerator， step过短" + stepLength);
        }

        IdGeneratorPOJO idGeneratorPOJO = this.idGeneratorMap.get(idGeneratorEnum.getId());
        if (idGeneratorPOJO == null) {
            throw new SmartBusinessException("IdGenerator， id 不存在" + idGeneratorEnum);
        }
        return idGeneratorPOJO;
    }

    /**
     * 替换特殊rule，即替换[yyyy][mm][dd][nnn]等规则
     */
    private String replaceAndFill(IdGeneratorPOJO idGeneratorPOJO, Long number, String year, String month, String day) {
        StringBuilder numberStringBuilder = new StringBuilder();
        int curNumberCount = String.valueOf(number).length();

        if (idGeneratorPOJO.getNumberCount() > curNumberCount) {
            int remain = idGeneratorPOJO.getNumberCount() - curNumberCount;
            for (int i = 0; i < remain; i++) {
                numberStringBuilder.append(0);
            }
        }
        numberStringBuilder.append(number);

        StringBuilder nStringBuilder = new StringBuilder();
        nStringBuilder.append("\\[");
        for (int i = 0; i < idGeneratorPOJO.getNumberCount(); i++) {
            nStringBuilder.append("n");
        }
        nStringBuilder.append("\\]");

        String tempRuleFormat = new String(idGeneratorPOJO.getIdGeneratorEntity().getRuleFormat().getBytes());
        if (idGeneratorPOJO.isHaveYear()) {
            tempRuleFormat = tempRuleFormat.replaceAll("\\[yyyy\\]", year);
        }
        if (idGeneratorPOJO.isHaveMonth()) {
            tempRuleFormat = tempRuleFormat.replaceAll("\\[mm\\]", month);
        }
        if (idGeneratorPOJO.isHaveDay()) {
            tempRuleFormat = tempRuleFormat.replaceAll("\\[dd\\]", day);
        }

        return tempRuleFormat.replaceAll(nStringBuilder.toString(), numberStringBuilder.toString());
    }
}
