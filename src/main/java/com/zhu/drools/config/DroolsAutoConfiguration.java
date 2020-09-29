package com.zhu.drools.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * The type Drools auto configuration.
 */
public class DroolsAutoConfiguration {
    /**
     * Drools configuration drools configuration.
     *
     * @return the drools configuration
     */
    @ConfigurationProperties(prefix = "drools")
    @Bean
    DroolsConfiguration droolsConfiguration(){
        return new DroolsConfiguration();
    }

    /**
     * Kie bean post processor kie bean post processor.
     *
     * @return the kie bean post processor
     */
    @Bean
    public KieBeanPostProcessor kieBeanPostProcessor(){
        return new KieBeanPostProcessor(droolsConfiguration());
    }
}
