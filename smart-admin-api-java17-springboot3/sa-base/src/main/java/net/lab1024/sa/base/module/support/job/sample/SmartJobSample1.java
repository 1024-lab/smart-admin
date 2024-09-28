package net.lab1024.sa.base.module.support.job.sample;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.module.support.job.core.SmartJob;
import org.springframework.stereotype.Service;

/**
 * 定时任务 示例1
 *
 * @author huke
 * @date 2024/6/17 21:30
 */
@Slf4j
@Service
public class SmartJobSample1 implements SmartJob {

    /**
     * 定时任务示例
     *
     * @param param 可选参数 任务不需要时不用管
     * @return
     */
    @Override
    public String run(String param) {
        // 写点什么业务逻辑
        return "执行完毕,随便说点什么吧";
    }

}
