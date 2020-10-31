package com.example.demo.service;

import com.example.demo.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataStatisticsComponent {
    @Autowired
    ArticleService articleService;

    // 每天执行一次，统计pv，插入到表里
    @Scheduled(cron = "1 0 0 * * ?")
    public void pvStatisticsPerDay(){
        articleService.pvStatisticsPerDay();
    }
}
