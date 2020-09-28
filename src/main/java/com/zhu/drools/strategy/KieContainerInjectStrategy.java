package com.zhu.drools.strategy;

import org.kie.api.runtime.KieContainer;

import java.lang.reflect.Field;

public class KieContainerInjectStrategy implements KieInjectStrategy<KieContainer> {
    @Override
    public KieContainer generator(Object bean,Field field){
        return StrategyUtil.generatorKieContainer(bean,field);
    }
}
