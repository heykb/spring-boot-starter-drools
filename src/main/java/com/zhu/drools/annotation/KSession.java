package com.zhu.drools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface K session.
 *
 * @author heykb
 */
@Target({ElementType.FIELD}) //声明应用在属性上
@Retention(RetentionPolicy.RUNTIME) //运行期生效
public @interface KSession {
    /**
     * Value string.
     *
     * @return the string
     */
    String value() default "";
}
