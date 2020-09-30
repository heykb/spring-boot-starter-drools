package com.zhu.drools.strategy;

import com.zhu.drools.config.DroolsConfiguration;
import org.kie.api.runtime.KieContainer;

import java.lang.reflect.Field;


/**
 * The type Kie container inject strategy.
 * @author heykb
 */
public class KieContainerInjectStrategy implements KieInjectStrategy<KieContainer> {

    @Override
    public KieContainer generator(Object bean, Field field, DroolsConfiguration droolsConfiguration){
        return StrategyUtil.generatorKieContainer(bean,field,droolsConfiguration);
    }
}
