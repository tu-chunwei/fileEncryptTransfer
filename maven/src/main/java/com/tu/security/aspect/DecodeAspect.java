package com.tu.security.aspect;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/*@Aspect
@Component //初始化
*/
public class DecodeAspect {
	
	/*@Pointcut("execution(com.tu.*.controller.*(java.util.Map))")
	private void decodePointCut(Map<String, Object> map) {
		
	}
	@Around(value="decodePointCut(map)",argNames="map")
	public void dealDecode(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("1");
		pjp.proceed();
		System.out.println("2");
	}*/
	
}
