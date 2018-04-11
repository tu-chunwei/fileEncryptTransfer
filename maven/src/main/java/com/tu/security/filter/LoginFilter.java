package com.tu.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * 登录过滤器，使未登录用户自动跳转到登录页面
 * @author TU
 *
 */
public class LoginFilter implements Filter{

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		HttpSession session = request.getSession();
		String AESkey = (String) session.getAttribute("AESkey");
		if(AESkey == null || "".equals(AESkey)) {
			//跳转到登录页面
			response.sendRedirect("login.jsp");
		}else {
			//继续此次请求
			chain.doFilter(arg0, arg1);
		}
	}

}
