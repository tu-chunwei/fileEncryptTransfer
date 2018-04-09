package com.tu.security.aspect;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * service 前置数据解密
 * @author TU
 *
 */
@Component
public class DecodeAspect {
	
	public void around(ProceedingJoinPoint pjp, Map<String, Object> jsonMap) throws Throwable{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		String attribute = (String) session.getAttribute("name");
		System.out.println(attribute);
		/*for(Map.Entry<String, Object> entry : jsonMap.entrySet()) {
			entry.getValue();
			jsonMap.put(entry.getKey(), value);
		}*/
		
		System.out.println("method before"+jsonMap.get("name"));
		jsonMap.put("name", "tu");
		Object proceed = pjp.proceed();
		System.out.println("method after");
	}
	
}
