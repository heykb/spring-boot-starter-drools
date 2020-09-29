package com.zhu.drools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface K jar path.
 *
 * @author heykb
 */
@Target({ElementType.FIELD}) //声明应用在属性上
@Retention(RetentionPolicy.RUNTIME) //运行期生效
public @interface KJarPath {
    /**
     * Value string.
     *
     * @return the string
     */
    String value();

    /**
     * Enable scanner boolean.
     *
     * @return the boolean
     */
    boolean enableScanner() default false;

    /**
     * Scanner interval long.
     *
     * @return the long
     */
    long scannerInterval() default 1000L;
}
