package com.zhu.drools.strategy;

import com.zhu.drools.config.DroolsConfiguration;

import java.lang.reflect.Field;

/**
 * The type Kie session inject strategy.
 * @author heykb
 */
public class KieSessionInjectStrategy implements KieInjectStrategy<Object> {
    @Override
    public Object generator(Object bean, Field field, DroolsConfiguration droolsConfiguration){
        return StrategyUtil.generatorKieSession(bean,field,droolsConfiguration);
    }
}
