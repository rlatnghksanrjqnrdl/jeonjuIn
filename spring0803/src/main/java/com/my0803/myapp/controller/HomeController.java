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
@Controller   //��ü������û -bean���� ����ϴ°Ͱ� ����
public class HomeController {
	
	//@Resource(name="db")   //��ü�������� �̸����� ã�´�
	//@Inject
	//@Autowired  //�޸𸮰����ȿ� ����  Ÿ�԰�ü�� ã�´�	
	@Resource(name="db")
	DriverManagerDataSource dmds;   //������ �������� ������ ��ü����
	
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
		
		//��������
		return "home";
	}
	
	@RequestMapping(value = "/introduction.do", method = RequestMethod.GET)
	public String introduction() {
	
		System.out.println("dmds�ּҰ��� �ֳ���?"+dmds);
		
		//��������
		return "introduction";
	}
	
	
}
