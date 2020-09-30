package com.zhu.drools.strategy;


import com.zhu.drools.config.DroolsConfiguration;
import com.zhu.drools.config.KieBeanService;
import org.kie.api.KieServices;

import java.lang.reflect.Field;

/**
 * The type Kie services inject strategy.
 * @author heykb
 */
public class KieServicesInjectStrategy implements KieInjectStrategy<KieServices> {
    @Override
    public KieServices generator(Object bean, Field field, DroolsConfiguration droolsConfiguration){
        return KieBeanService.getKieServices();
    }
}
