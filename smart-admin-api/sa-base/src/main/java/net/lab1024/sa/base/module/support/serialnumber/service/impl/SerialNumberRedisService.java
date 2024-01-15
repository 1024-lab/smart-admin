package net.lab1024.sa.base.module.support.serialnumber.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.exception.BusinessException;
import net.lab1024.sa.base.constant.RedisKeyConst;
import net.lab1024.sa.base.module.support.redis.RedisService;
import net.lab1024.sa.base.module.support.serialnumber.domain.SerialNumberEntity;
import net.lab1024.sa.base.module.support.serialnumber.domain.SerialNumberGenerateResultBO;
import net.lab1024.sa.base.module.support.serialnumber.domain.SerialNumberInfoBO;
import net.lab1024.sa.base.module.support.serialnumber.domain.SerialNumberLastGenerateBO;
import net.lab1024.sa.base.module.support.serialnumber.service.SerialNumberBaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 单据序列号 基于redis锁实现
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-25 21:46:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
public class SerialNumberRedisService extends SerialNumberBaseService {

    private static final int MAX_GET_LOCK_COUNT = 5;

    private static final long SLEEP_MILLISECONDS = 200L;

    @Resource
    private RedisService redisService;

    @Override
    public void initLastGenerateData(List<SerialNumberEntity> serialNumberEntityList) {
        if (serialNumberEntityList == null) {
            return;
        }

        //删除之前的
        redisService.delete(RedisKeyConst.Support.SERIAL_NUMBER_LAST_INFO);

        for (SerialNumberEntity serialNumberEntity : serialNumberEntityList) {
            SerialNumberLastGenerateBO lastGenerateBO = SerialNumberLastGenerateBO
                    .builder()
                    .serialNumberId(serialNumberEntity.getSerialNumberId())
                    .lastNumber(serialNumberEntity.getLastNumber())
                    .lastTime(serialNumberEntity.getLastTime())
                    .build();

            redisService.mset(RedisKeyConst.Support.SERIAL_NUMBER_LAST_INFO,
                    String.valueOf(serialNumberEntity.getSerialNumberId()),
                    lastGenerateBO
            );
        }
    }

    @Override
    public List<String> generateSerialNumberList(SerialNumberInfoBO serialNumberInfo, int count) {
        SerialNumberGenerateResultBO serialNumberGenerateResult = null;
        String lockKey = RedisKeyConst.Support.SERIAL_NUMBER + serialNumberInfo.getSerialNumberId();
        try {
            boolean lock = false;
            for (int i = 0; i < MAX_GET_LOCK_COUNT; i++) {
                try {
                    lock = redisService.getLock(lockKey, 60 * 1000L);
                    if (lock) {
                        break;
                    }
                    Thread.sleep(SLEEP_MILLISECONDS);
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (!lock) {
                throw new BusinessException("SerialNumber 尝试5次，未能生成单号");
            }
            // 获取上次的生成结果
            SerialNumberLastGenerateBO lastGenerateBO = (SerialNumberLastGenerateBO) redisService.mget(
                    RedisKeyConst.Support.SERIAL_NUMBER_LAST_INFO,
                    String.valueOf(serialNumberInfo.getSerialNumberId()));

            // 生成
            serialNumberGenerateResult = super.loopNumberList(lastGenerateBO, serialNumberInfo, count);

            // 将生成信息保存的内存和数据库
            lastGenerateBO.setLastNumber(serialNumberGenerateResult.getLastNumber());
            lastGenerateBO.setLastTime(serialNumberGenerateResult.getLastTime());
            serialNumberDao.updateLastNumberAndTime(serialNumberInfo.getSerialNumberId(),
                    serialNumberGenerateResult.getLastNumber(),
                    serialNumberGenerateResult.getLastTime());

            redisService.mset(RedisKeyConst.Support.SERIAL_NUMBER_LAST_INFO,
                    String.valueOf(serialNumberInfo.getSerialNumberId()), lastGenerateBO);

            // 把生成过程保存到数据库里
            super.saveRecord(serialNumberGenerateResult);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            redisService.unLock(lockKey);
        }

        return formatNumberList(serialNumberGenerateResult, serialNumberInfo);
    }
}
