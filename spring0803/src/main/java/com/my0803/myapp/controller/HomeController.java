package com.my0803.myapp.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller   //객체생성요청 -bean으로 등록하는것과 같다
public class HomeController {
	
	//@Resource(name="db")   //객체참조변수 이름으로 찾는다
	//@Inject
	//@Autowired  //메모리공간안에 같은  타입객체를 찾는다	
	@Resource(name="db")
	DriverManagerDataSource dmds;   //선언한 변수에게 생성된 객체주입
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/index.do", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		//포워드방식
		return "home";
	}
	
	@RequestMapping(value = "/introduction.do", method = RequestMethod.GET)
	public String introduction() {
	
		System.out.println("dmds주소값이 있나요?"+dmds);
		
		//포워드방식
		return "introduction";
	}
	
	
}
