package com.tistory.ospace.annotation.timelog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.IntroductionInterceptor;

public class TimeLogInterceptor implements IntroductionInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeLogInterceptor.class);
	private static final Pattern RE_CLASS = Pattern.compile(".+\\.([a-zA-Z]+\\.[a-zA-Z]+)\\(\\S*\\)");
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String args = "";
		for(Object arg : invocation.getArguments()) {
			if(!args.isEmpty()) args = args.concat(",");
			args = args.concat(null == arg ? "null" : arg.toString());
		}
		
		String className = invocation.getStaticPart().toString();
		Matcher matcher = RE_CLASS.matcher(className);
		if (matcher.matches()) {
			className = matcher.group(1) + "()";
		}
		
		LOGGER.info("{} BEGIN: arguments[{} ea]", className, invocation.getArguments().length);
		
		long begin = System.currentTimeMillis();
		Object ret = invocation.proceed();
		long end = System.currentTimeMillis();
		
		LOGGER.info("{} END: runtime [{} msec]", className, (end-begin));
		
		return ret;
	}

	@Override
	public boolean implementsInterface(Class<?> arg0) {
		return false;
	}
}
