package com.my0803.myapp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//로그인 체크 인증 인터셉터 클래스 
public class AuthInterceptor  extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler
			) throws Exception{
		
		HttpSession session = request.getSession();
		
		boolean tf =false;
		if (session.getAttribute("midx") == null) {
			
			System.out.println(request);
			//세션에 이동할려고 하는 주소를 담는다
			saveUrl(request);
			
			String location =request.getContextPath()+"/member/memberLogin.do";
			response.sendRedirect(location);			
			return false;			
		} else {
			return true;			
		}		
		//return true;
	}
	
	//이동하려고 하는 주소를 세션에 담는 메소드
	public void saveUrl(HttpServletRequest req) {
		
		String uri = req.getRequestURI();   //전체경로주소
		String query =req.getQueryString();  // 파라미터
		
		if (query ==null || query.equals("null")) {
			query="";
		}else {
			query = "?" +query;
		}
		System.out.println(uri+query);
		
		if (req.getMethod().equals("GET")) {   // 대소문자 중요!!!
			req.getSession().setAttribute("saveUrl", uri+query);
		}		
	}
	
	
	
	
}
