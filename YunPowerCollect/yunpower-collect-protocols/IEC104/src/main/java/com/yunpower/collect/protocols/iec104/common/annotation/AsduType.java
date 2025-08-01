package com.yunpower.collect.protocols.iec104.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Asdu type
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AsduType {

    /**
     * Value string
     */
    String value() default "";

    /**
     * Type id int
     */
    int typeId() default 0;

    /**
     * Is prior boolean
     */
    boolean isPrior() default false;

    /**
     * Builder name string
     */
    String builderName() default "";
}
