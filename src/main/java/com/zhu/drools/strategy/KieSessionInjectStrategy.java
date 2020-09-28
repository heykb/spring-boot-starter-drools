package com.zhu.drools.strategy;

import java.lang.reflect.Field;

public class KieSessionInjectStrategy implements KieInjectStrategy<Object> {
    @Override
    public Object generator(Object bean,Field field){
        return StrategyUtil.generatorKieSession(bean,field);
    }
}
