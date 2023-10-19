package com.my0803.myapp.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.my0803.myapp.domain.MemberVo;
import com.my0803.myapp.service.MemberService;

@Controller   //��Ʈ�ѷ��뵵�� ��ü������û
@RequestMapping(value="/member")
public class MemberController {

	@Autowired  //��ü���Կ�û
	MemberService ms;
	
	@Autowired  //�� ��ϵ� ��ȣȭ ��� ��ü�� �����ش޶�� ��û
	private BCryptPasswordEncoder bCryptPasswordEncoder;
		
	@RequestMapping(value="/memberJoin.do")
	public String memberJoin() {			
		return "/member/memberJoin";
	}
	
	@RequestMapping(value="/memberJoinAction.do")
	public String memberJoinAction(MemberVo mv) {		//input��ü���� ���� MemberVo���ε�
	
		String birth = mv.getMemberYear()+mv.getMemberMonth()+mv.getMemberDay();
		mv.setMemberBirth(birth);
		
		String memberPwd2 = bCryptPasswordEncoder.encode(mv.getMemberPwd());
		mv.setMemberPwd(memberPwd2);
		
		int value = ms.memberInsert(mv);		
		
		return "redirect:/";  //���������� �ƴ� sendRedirect����̴�
	}	
	
	@RequestMapping(value="/memberLogin.do")
	public String memberLogin() {			
		return "/member/memberLogin";
	}
	
	// �α��ι�ư�� ������ ó���ϰ� ���� ���������� �̵��ϴ� 
	// �޼ҵ带 ��������
	// �α����� ���� �ʾ����� �ٽ� �α��� �������� ���Բ� ó���ϼ���
	//Daoó���ϴ� �κ��� ���ܵΰ�
	
	@RequestMapping(value="/memberLoginAction.do")
	public String memberLoginAction(
			@RequestParam("memberId") String memberId,
			@RequestParam("memberPwd") String memberPwd,		
			RedirectAttributes rttr,
			HttpServletRequest request			
			) {		
		
			//�������
			//MemberVo mv = ms.memberLogin(memberId, memberPwd);
			
			//��ȣ �񱳹�� - ���̵� �ش��ϴ� ��й�ȣ�� ������ͼ� ���Ͱ� ��
			MemberVo mv = ms.memberLogin(memberId);
						
			String path ="";
			if (mv !=null) {
					if(bCryptPasswordEncoder.matches(memberPwd, mv.getMemberPwd())) {
						//1ȸ�� ��Ŭ���� RedirectAttribute
						rttr.addAttribute("midx", mv.getMidx());	
						rttr.addAttribute("memberName", mv.getMemberName());	
					
					//	System.out.println("�����ּ�"+request.getSession().getAttribute("saveUrl"));
						
						if (request.getSession().getAttribute("saveUrl") != null) {
							path = 	(String)request.getSession().getAttribute("saveUrl").toString().substring(request.getContextPath().length()+1);					
						}else {
							path = "index.jsp";
						}		
					
					}					
							
			}else {		
				rttr.addFlashAttribute("msg","Ȯ������");
				path = "member/memberLogin.do";   //�ٽ� �α��� ��������
			}
			return "redirect:/"+path;
	 }
	
	@RequestMapping(value="/memberLogout.do")
		public String memberLogout(HttpSession session) {
				
		session.removeAttribute("midx");
		session.removeAttribute("memberName");
		session.invalidate();
		
		return "redirect:/";
	}
	
	@ResponseBody
	@RequestMapping(value="/memberIdCheck.do")
	public String memberIdCheck(String memberId) {	
	
		String str = null;
		int value = ms.memberIdCheck(memberId);
		
		str = "{\"value\" : \""+value+"\"}";	
		
		return str;
	}
	
	@RequestMapping(value="/memberList.do")
	public String memberList(Model model) {	
	
		ArrayList<MemberVo> alist =   ms.memberList();	
		model.addAttribute("alist", alist);
		
		return "/member/memberList";
	}
	
	
	
}
