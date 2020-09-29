package com.zhu.drools.strategy;

import org.kie.api.KieBase;

import java.lang.reflect.Field;

/**
 * The type Kie base inject strategy.
 */
public class KieBaseInjectStrategy implements KieInjectStrategy<KieBase> {
    @Override
    public KieBase generator(Object bean,Field field){
        return StrategyUtil.generatorKieBase(bean,field);
    }
}
