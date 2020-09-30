package com.zhu.drools.strategy;

import com.zhu.drools.config.DroolsConfiguration;
import org.kie.api.KieBase;

import java.lang.reflect.Field;

/**
 * The type Kie base inject strategy.
 * @author heykb
 */
public class KieBaseInjectStrategy implements KieInjectStrategy<KieBase> {
    @Override
    public KieBase generator(Object bean, Field field, DroolsConfiguration droolsConfiguration){
        return StrategyUtil.generatorKieBase(bean,field,droolsConfiguration);
    }
}
