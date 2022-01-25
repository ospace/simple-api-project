package com.tistory.ospace.annotation.timelog;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.IntroductionAdvisor;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.core.annotation.AnnotationUtils;

import com.tistory.ospace.common.DataUtils;

//@Configuration
public class TimeLogConfiguration extends AbstractPointcutAdvisor implements IntroductionAdvisor/*, BeanFactoryAware*/ {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeLogConfiguration.class);
	
	private static final long serialVersionUID = -4528767584639351076L;
	private Advice      advice;
	private Pointcut    pointcut;
	
	class AnnotationPointcut implements Pointcut {
		private final MethodMatcher methodMatcher;
		private final ClassFilter   classFilter;
		private Class<? extends Annotation> annotationClazz = null;
		
		public AnnotationPointcut(Class<? extends Annotation> annotationClazz) {
			this.annotationClazz = annotationClazz;
			this.methodMatcher = new AnnotationMethodMatcher(annotationClazz) {
			    @Override
		    	public boolean matches(Method method, Class<?> targetClass) {
			    	return hasAnnotation(targetClass) || super.matches(method, targetClass);
			    }
			};
			this.classFilter = new AnnotationClassFilter(annotationClazz, true) {
				@Override
				public boolean matches(Class<?> clazz) {
					return super.matches(clazz) || null != DataUtils.findFirst(clazz.getMethods(), it->hasAnnotation(it));
				}
			};
		}
		
		boolean hasAnnotation(Method method) {
			return null != AnnotationUtils.findAnnotation(method, this.annotationClazz);
		}
		
		boolean hasAnnotation(Class<?> clazz) {
			return null != AnnotationUtils.findAnnotation(clazz, this.annotationClazz);
		}


		@Override
		public ClassFilter getClassFilter() {
			return classFilter;
		}

		@Override
		public MethodMatcher getMethodMatcher() {
			return methodMatcher;
		}
	}
	
	@PostConstruct
	private void init() {
		Pointcut pointcut1 = new AnnotationPointcut(TimeLog.class);
		ComposablePointcut compPointcut = new ComposablePointcut(pointcut1);
		this.pointcut = compPointcut;
		
		TimeLogInterceptor interceptor = new TimeLogInterceptor();
		
		this.advice = interceptor;
	}
	
	@Override
	public Advice getAdvice() {
		return this.advice;
	}

	@Override
	public Class<?>[] getInterfaces() {
		return new Class[] {};
	}

	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

	@Override
	public ClassFilter getClassFilter() {
		return pointcut.getClassFilter();
	}

	@Override
	public void validateInterfaces() throws IllegalArgumentException {
	}

}
