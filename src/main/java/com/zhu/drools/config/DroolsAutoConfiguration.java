package com.zhu.drools.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * The type Drools auto configuration.
 * @author heykb
 */
@Configuration
public class DroolsAutoConfiguration {

    /**
     * Drools configuration drools configuration.
     *
     * @return the drools configuration
     *
     */
    @Bean
    @ConfigurationProperties(prefix = "drools")
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
