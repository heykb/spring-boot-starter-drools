package com.zhu.drools.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

public class DroolsAutoConfiguration {
    @ConfigurationProperties(prefix = "drools")
    @Bean
    DroolsConfiguration droolsConfiguration(){
        return new DroolsConfiguration();
    }
    @Bean
    public KieBeanPostProcessor kieBeanPostProcessor(DroolsConfiguration droolsConfiguration){
        return new KieBeanPostProcessor(droolsConfiguration());
    }
}
