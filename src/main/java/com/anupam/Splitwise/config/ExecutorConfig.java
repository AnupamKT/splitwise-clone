package com.anupam.Splitwise.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Getter
public class ExecutorConfig {

    @Value("${splitwise.concurrent.thread}")
    private int concurrentThreads;

    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(concurrentThreads);
    }
}
