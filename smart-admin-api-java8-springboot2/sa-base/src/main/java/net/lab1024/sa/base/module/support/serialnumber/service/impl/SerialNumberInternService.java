package net.lab1024.sa.base.module.support.serialnumber.service.impl;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import net.lab1024.sa.base.module.support.serialnumber.domain.SerialNumberEntity;
import net.lab1024.sa.base.module.support.serialnumber.domain.SerialNumberGenerateResultBO;
import net.lab1024.sa.base.module.support.serialnumber.domain.SerialNumberInfoBO;
import net.lab1024.sa.base.module.support.serialnumber.domain.SerialNumberLastGenerateBO;
import net.lab1024.sa.base.module.support.serialnumber.service.SerialNumberBaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单据序列号 基于内存锁实现
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-25 21:46:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class SerialNumberInternService extends SerialNumberBaseService {

    /**
     * 按照 serialNumberId 进行锁
     */
    private static final Interner<Integer> POOL = Interners.newStrongInterner();


    private ConcurrentHashMap<Integer, SerialNumberLastGenerateBO> serialNumberLastGenerateMap = new ConcurrentHashMap<>();

    @Override
    public void initLastGenerateData(List<SerialNumberEntity> serialNumberEntityList) {
        if (serialNumberEntityList == null) {
            return;
        }

        for (SerialNumberEntity serialNumberEntity : serialNumberEntityList) {
            SerialNumberLastGenerateBO lastGenerateBO = SerialNumberLastGenerateBO
                    .builder()
                    .serialNumberId(serialNumberEntity.getSerialNumberId())
                    .lastNumber(serialNumberEntity.getLastNumber())
                    .lastTime(serialNumberEntity.getLastTime())
                    .build();
            serialNumberLastGenerateMap.put(serialNumberEntity.getSerialNumberId(), lastGenerateBO);
        }
    }

    @Override
    public List<String> generateSerialNumberList(SerialNumberInfoBO serialNumberInfo, int count) {
        SerialNumberGenerateResultBO serialNumberGenerateResult = null;
        synchronized (POOL.intern(serialNumberInfo.getSerialNumberId())) {

            // 获取上次的生成结果
            SerialNumberLastGenerateBO lastGenerateBO = serialNumberLastGenerateMap.get(serialNumberInfo.getSerialNumberId());

            // 生成
            serialNumberGenerateResult = super.loopNumberList(lastGenerateBO, serialNumberInfo, count);

            // 将生成信息保存的内存和数据库
            lastGenerateBO.setLastNumber(serialNumberGenerateResult.getLastNumber());
            lastGenerateBO.setLastTime(serialNumberGenerateResult.getLastTime());
            serialNumberDao.updateLastNumberAndTime(serialNumberInfo.getSerialNumberId(),
                    serialNumberGenerateResult.getLastNumber(),
                    serialNumberGenerateResult.getLastTime());

            // 把生成过程保存到数据库里
            super.saveRecord(serialNumberGenerateResult);
        }

        return formatNumberList(serialNumberGenerateResult, serialNumberInfo);
    }


}
