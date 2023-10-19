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

@Controller   //컨트롤러용도의 객체생성요청
@RequestMapping(value="/member")
public class MemberController {

	@Autowired  //객체주입요청
	MemberService ms;
	
	@Autowired  //빈에 등록된 암호화 모듈 객체를 주입해달라고 요청
	private BCryptPasswordEncoder bCryptPasswordEncoder;
		
	@RequestMapping(value="/memberJoin.do")
	public String memberJoin() {			
		return "/member/memberJoin";
	}
	
	@RequestMapping(value="/memberJoinAction.do")
	public String memberJoinAction(MemberVo mv) {		//input객체들의 값을 MemberVo바인딩
	
		String birth = mv.getMemberYear()+mv.getMemberMonth()+mv.getMemberDay();
		mv.setMemberBirth(birth);
		
		String memberPwd2 = bCryptPasswordEncoder.encode(mv.getMemberPwd());
		mv.setMemberPwd(memberPwd2);
		
		int value = ms.memberInsert(mv);		
		
		return "redirect:/";  //포워드방식이 아닌 sendRedirect방식이다
	}	
	
	@RequestMapping(value="/memberLogin.do")
	public String memberLogin() {			
		return "/member/memberLogin";
	}
	
	// 로그인버튼을 누르면 처리하고 메인 페이지으로 이동하는 
	// 메소드를 만들어보세요
	// 로그인이 되지 않았으면 다시 로그인 페이지로 가게끔 처리하세요
	//Dao처리하는 부분은 남겨두고
	
	@RequestMapping(value="/memberLoginAction.do")
	public String memberLoginAction(
			@RequestParam("memberId") String memberId,
			@RequestParam("memberPwd") String memberPwd,		
			RedirectAttributes rttr,
			HttpServletRequest request			
			) {		
		
			//기존방식
			//MemberVo mv = ms.memberLogin(memberId, memberPwd);
			
			//암호 비교방식 - 아이디에 해당하는 비밀번호를 가지고와서 날것과 비교
			MemberVo mv = ms.memberLogin(memberId);
						
			String path ="";
			if (mv !=null) {
					if(bCryptPasswordEncoder.matches(memberPwd, mv.getMemberPwd())) {
						//1회용 모델클래스 RedirectAttribute
						rttr.addAttribute("midx", mv.getMidx());	
						rttr.addAttribute("memberName", mv.getMemberName());	
					
					//	System.out.println("저장주소"+request.getSession().getAttribute("saveUrl"));
						
						if (request.getSession().getAttribute("saveUrl") != null) {
							path = 	(String)request.getSession().getAttribute("saveUrl").toString().substring(request.getContextPath().length()+1);					
						}else {
							path = "index.jsp";
						}		
					
					}					
							
			}else {		
				rttr.addFlashAttribute("msg","확인해줘");
				path = "member/memberLogin.do";   //다시 로그인 페이지로
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
