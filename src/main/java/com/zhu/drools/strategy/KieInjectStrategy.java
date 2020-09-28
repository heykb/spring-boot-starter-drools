package com.zhu.drools.strategy;

import java.lang.reflect.Field;

public interface KieInjectStrategy<T> {
    public T generator(Object bean, Field field);
}
