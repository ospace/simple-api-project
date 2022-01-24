package com.tistory.ospace.annotation.timelog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableAspectJAutoProxy(proxyTargetClass = false)
@Import(TimeLogConfiguration.class)
public @interface EnableTimeLog {
//	boolean proxyTargetClass() default false;
}
