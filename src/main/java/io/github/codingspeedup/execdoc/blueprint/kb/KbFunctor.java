package io.github.codingspeedup.execdoc.blueprint.kb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface KbFunctor {

    String value() default "";

    Class<?> T1() default NullPointerException.class;

    Class<?> T2() default NullPointerException.class;

    Class<?> T3() default NullPointerException.class;

}
