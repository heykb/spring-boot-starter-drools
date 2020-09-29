package com.zhu.drools.strategy;

import java.lang.reflect.Field;

/**
 * The interface Kie inject strategy.
 *
 * @param <T> the type parameter
 */
public interface KieInjectStrategy<T> {
    /**
     * Generator t.
     *
     * @param bean  the bean
     * @param field the field
     * @return the t
     */
    public T generator(Object bean, Field field);
}
