package net.lab1024.sa.base.module.support.serialnumber.service.impl;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.util.SmartDateFormatterEnum;
import net.lab1024.sa.base.common.util.SmartEnumUtil;
import net.lab1024.sa.base.common.util.SmartLocalDateUtil;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.constant.RedisKeyConst;
import net.lab1024.sa.base.module.support.redis.RedisService;
import net.lab1024.sa.base.module.support.serialnumber.constant.SerialNumberRuleTypeEnum;
import net.lab1024.sa.base.module.support.serialnumber.domain.SerialNumberEntity;
import net.lab1024.sa.base.module.support.serialnumber.domain.SerialNumberGenerateResultBO;
import net.lab1024.sa.base.module.support.serialnumber.domain.SerialNumberInfoBO;
import net.lab1024.sa.base.module.support.serialnumber.service.SerialNumberBaseService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 单据序列号 基于redis key-value  increase 实现
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2025-08-03 22:46:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
public class SerialNumberRedisService extends SerialNumberBaseService {

    @Resource
    private RedisService redisService;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void initLastGenerateData(List<SerialNumberEntity> serialNumberEntityList) {
        if (serialNumberEntityList == null) {
            return;
        }

        // 设置redis的上次值
        for (SerialNumberEntity serialNumberEntity : serialNumberEntityList) {
            if (serialNumberEntity.getLastTime() == null) {
                continue;
            }

            String redisKey = generateRedisKeyByDate(serialNumberEntity.getSerialNumberId(),
                    SmartEnumUtil.getEnumByName(serialNumberEntity.getRuleType().toUpperCase(), SerialNumberRuleTypeEnum.class),
                    serialNumberEntity.getLastTime().toLocalDate());

            Object o = redisTemplate.opsForValue().get(redisKey);
            if (o == null) {
                redisTemplate.opsForValue().set(redisKey, serialNumberEntity.getLastNumber());
            }
        }
    }

    /**
     * 每天凌晨一点进行检测；
     * 检测单位数量为3; 3天前、3月前、3年前
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void tryDeleteUnusedRedisKey() {
        for (SerialNumberInfoBO serialNumberInfoBO : serialNumberMap.values()) {
            SerialNumberRuleTypeEnum typeEnum = serialNumberInfoBO.getSerialNumberRuleTypeEnum();
            String dateStr = "";
            switch (typeEnum) {
                case DAY -> {
                    dateStr = SmartLocalDateUtil.format(LocalDate.now().minusDays(3), SmartDateFormatterEnum.YMD);
                }
                case MONTH -> {
                    dateStr = SmartLocalDateUtil.format(LocalDate.now().minusMonths(3), SmartDateFormatterEnum.YM);
                }
                case YEAR -> {
                    dateStr = String.valueOf(LocalDate.now().minusYears(3));
                }
            }
            if (SmartStringUtil.isNotEmpty(dateStr)) {
                String redisKey = RedisKeyConst.Support.SERIAL_NUMBER + serialNumberInfoBO.getSerialNumberId() + ":" + dateStr;
                redisService.delete(redisKey);
            }
        }
    }

    @Override
    public List<String> generateSerialNumberList(SerialNumberInfoBO serialNumberInfo, int count) {
        // 根据步长，计算 redis 增加值
        ArrayList<Integer> list = new ArrayList<>(count);
        int redisIncrease = 0;
        for (int i = 0; i < count; i++) {
            int stepIncrease = 1;
            Integer stepRandomRange = serialNumberInfo.getStepRandomRange();
            if (stepRandomRange > 1) {
                stepIncrease = RandomUtil.getSecureRandom().nextInt(1, serialNumberInfo.getStepRandomRange() + 1);
            }
            redisIncrease += stepIncrease;
            list.add(stepIncrease);
        }
        try {
            String redisKey = generateRedisKeyByDate(serialNumberInfo.getSerialNumberId(), serialNumberInfo.getSerialNumberRuleTypeEnum(), LocalDate.now());
            Long increaseResult = redisTemplate.opsForValue().increment(redisKey, redisIncrease);

            ArrayList<Long> numberList = new ArrayList<>(count);
            Long number = increaseResult;
            for (Integer i : list) {
                number = number - i;
                numberList.add((number + 1));
            }

            Collections.reverse(numberList);

            SerialNumberGenerateResultBO serialNumberGenerateResult = SerialNumberGenerateResultBO
                    .builder()
                    .serialNumberId(serialNumberInfo.getSerialNumberId())
                    .lastNumber(increaseResult)
                    .lastTime(LocalDateTime.now())
                    .numberList(numberList)
                    .isReset(false)
                    .build();


            // 将生成信息保存的内存和数据库
            serialNumberDao.updateLastNumberAndTime(serialNumberInfo.getSerialNumberId(),
                    serialNumberGenerateResult.getLastNumber(),
                    serialNumberGenerateResult.getLastTime());

            // 把生成过程保存到数据库里
            super.saveRecord(serialNumberGenerateResult);
            return formatNumberList(serialNumberGenerateResult, serialNumberInfo);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    private String generateRedisKeyByDate(Integer serialNumberId, SerialNumberRuleTypeEnum serialNumberRuleTypeEnum, LocalDate localDate) {
        return switch (serialNumberRuleTypeEnum) {
            case DAY -> {
                String dayStr = SmartLocalDateUtil.format(localDate, SmartDateFormatterEnum.YMD);
                yield RedisKeyConst.Support.SERIAL_NUMBER + serialNumberId + ":" + dayStr;
            }
            case MONTH -> {
                String monthStr = SmartLocalDateUtil.format(localDate, SmartDateFormatterEnum.YM);
                yield RedisKeyConst.Support.SERIAL_NUMBER + serialNumberId + ":" + monthStr;
            }
            case YEAR -> {
                String yearStr = String.valueOf(localDate.getYear());
                yield RedisKeyConst.Support.SERIAL_NUMBER + serialNumberId + ":" + yearStr;
            }
            case NONE -> RedisKeyConst.Support.SERIAL_NUMBER + serialNumberId;
        };
    }
}
