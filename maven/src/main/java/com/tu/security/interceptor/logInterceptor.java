package com.tu.security.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class logInterceptor implements InvocationHandler{
	
	private Object target;
	
	private void beforeMethod() {
		System.out.println("开始");
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		beforeMethod();
		method.invoke(target, args);
		return null;
	}

}
