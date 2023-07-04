package com.codermy.myspringsecurityplus.admin.task;

import com.codermy.myspringsecurityplus.admin.service.GenerationService;
import com.codermy.myspringsecurityplus.log.service.MyLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @Author: Xavier.wu
 * @CreateTime: 2023-04-11  17:49
 * @Description: TODO
 * @Version: 1.0
 */
@Component
public class GenerationDataClearTask {

    private static Logger logger = LoggerFactory.getLogger(GenerationDataClearTask.class);

    @Autowired
    private GenerationService generationService;

    @Autowired
    private MyLogService logService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void handler() {
        String now = LocalDate.now().plusDays(-3L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        generationService.batchRemove(now);
        logger.info("成功删除历史数据");

        logService.delAllByInfo();
        logger.info("成功删除日志");

    }

}
