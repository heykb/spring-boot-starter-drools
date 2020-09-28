package com.zhu.drools.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class DroolsAutoConfiguration {
    @Bean
    public KieBeanPostProcessor kieBeanPostProcessor(){
        return new KieBeanPostProcessor();
    }
}
