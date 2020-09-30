package com.zhu.drools.strategy;

import com.zhu.drools.config.DroolsConfiguration;

import java.lang.reflect.Field;

/**
 * The interface Kie inject strategy.
 *
 * @author heykb
 * @param <T> the type parameter
 */
public interface KieInjectStrategy<T> {
    /**
     * Generator diffrent KieBean and Inject bean field
     *
     * @param bean  the bean
     * @param field the field
     * @return the t
     */
    public T generator(Object bean, Field field, DroolsConfiguration droolsConfiguration);
}
