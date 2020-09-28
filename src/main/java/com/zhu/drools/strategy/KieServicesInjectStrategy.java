package com.zhu.drools.strategy;


import com.zhu.drools.config.KieBeanService;
import org.kie.api.KieServices;

import java.lang.reflect.Field;

public class KieServicesInjectStrategy implements KieInjectStrategy<KieServices> {
    @Override
    public KieServices generator(Object bean,Field field){
        return KieBeanService.getKieServices();
    }
}
