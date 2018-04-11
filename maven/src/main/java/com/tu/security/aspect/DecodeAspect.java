package com.tu.security.aspect;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tu.security.utils.SecurityUtil;
import com.tu.security.vo.AESInfo;

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
		String AESkey = session.getAttribute("AESkey").toString();
		System.out.println(attribute);
		
		for(Map.Entry<String, Object> entry : jsonMap.entrySet()) {
			String decrypt = decrypt(entry.getValue().toString(), AESkey);
			jsonMap.put(entry.getKey(), decrypt);
		}
		jsonMap.put("name", "tu");
		Object proceed = pjp.proceed();
		System.out.println("method after");
	}
	public static String decrypt( String input, String DL) {
        String[] values = input.split( DL );
        String indices = values[values.length - 1];
        int[] indexes = SecurityUtil.convert( indices );
        AESInfo securityInfo = new AESInfo( values, indexes );
        SecurityUtil aesUtil = new SecurityUtil( securityInfo.getKeySize(), securityInfo.getIterationCount() );
        return aesUtil.decrypt( securityInfo.getSalt(), securityInfo.getIv(), securityInfo.getPassPhrase(), securityInfo.getCipherText() );
    }
}
