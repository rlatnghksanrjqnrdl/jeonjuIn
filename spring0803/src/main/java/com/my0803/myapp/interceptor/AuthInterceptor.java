package com.my0803.myapp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//�α��� üũ ���� ���ͼ��� Ŭ���� 
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
			//���ǿ� �̵��ҷ��� �ϴ� �ּҸ� ��´�
			saveUrl(request);
			
			String location =request.getContextPath()+"/member/memberLogin.do";
			response.sendRedirect(location);			
			return false;			
		} else {
			return true;			
		}		
		//return true;
	}
	
	//�̵��Ϸ��� �ϴ� �ּҸ� ���ǿ� ��� �޼ҵ�
	public void saveUrl(HttpServletRequest req) {
		
		String uri = req.getRequestURI();   //��ü����ּ�
		String query =req.getQueryString();  // �Ķ����
		
		if (query ==null || query.equals("null")) {
			query="";
		}else {
			query = "?" +query;
		}
		System.out.println(uri+query);
		
		if (req.getMethod().equals("GET")) {   // ��ҹ��� �߿�!!!
			req.getSession().setAttribute("saveUrl", uri+query);
		}		
	}
	
	
	
	
}
