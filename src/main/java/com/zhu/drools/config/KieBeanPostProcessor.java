package com.zhu.drools.config;


import com.zhu.drools.annotation.KBase;
import com.zhu.drools.annotation.KContainer;
import com.zhu.drools.annotation.KServices;
import com.zhu.drools.annotation.KSession;
import com.zhu.drools.strategy.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;



public class KieBeanPostProcessor  implements BeanPostProcessor {


    private Map<Class<?>, KieInjectStrategy> strategyMap = new HashMap(){
        {
            put(KContainer.class,new KieContainerInjectStrategy());
            put(KBase.class,new KieBaseInjectStrategy());
            put(KSession.class,new KieSessionInjectStrategy());
            put(KServices.class,new KieServicesInjectStrategy());
        }
    };

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        try{
            for(Field field:fields){
                field.setAccessible(true);
                KieInjectStrategy strategy = null;
                if(field.isAnnotationPresent(KContainer.class)){
                    strategy = strategyMap.get(KContainer.class);
                    field.set(bean,strategy.generator(bean,field));
                }
                if(field.isAnnotationPresent(KBase.class)){
                    strategy = strategyMap.get(KBase.class);
                    field.set(bean,strategy.generator(bean,field));
                }
                if(field.isAnnotationPresent(KSession.class)){
                    strategy = strategyMap.get(KSession.class);
                    field.set(bean,strategy.generator(bean,field));
                }
                if(field.isAnnotationPresent(KServices.class)){
                    strategy = strategyMap.get(KServices.class);
                    field.set(bean,strategy.generator(bean,field));
                }

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return bean;
    }

}
