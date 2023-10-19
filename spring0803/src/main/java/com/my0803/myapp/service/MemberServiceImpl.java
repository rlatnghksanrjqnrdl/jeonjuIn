package com.my0803.myapp.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my0803.myapp.domain.MemberVo;
import com.my0803.myapp.persistance.MemberService_Mapper;

@Service
public class MemberServiceImpl implements MemberService {
	
	private MemberService_Mapper msm;
	
	@Autowired //���̹�Ƽ�� ��ü�� ���Թ޴´�.
	public MemberServiceImpl(SqlSession sqlSession) {
		this.msm=sqlSession.getMapper(MemberService_Mapper.class);
	}	
	
	
	@Override
	public int memberInsert(MemberVo mv) {
		
		//���̹�Ƽ������ ����ϴ� �޼ҵ带 �����س��� �������̽��� �θ���
		int value = msm.memberInsert(mv);
		
		return value;
	}

	@Override
	public MemberVo memberLogin(String memberId, String memberPwd) {
		MemberVo mv= null;
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("memberId", memberId);
		hm.put("memberPwd", memberPwd);
		
		//���̹�Ƽ���� ������ �غ�
		//���̹�Ƽ������ ����� �޼ҵ带 �����س��� �������̽�
		mv = msm.memberLogin(hm);
		
		
		return mv;
	}
	
	@Override   //�������̵�(������) �����ε�(�����̸��ٸ����Ҹ޼ҵ�) �Ѵ�
	public MemberVo memberLogin(String memberId) {
		MemberVo mv= null;
		mv = msm.memberLogin2(memberId);
		
		//System.out.println("����� ��й�ȣ"+mv.getMemberPwd());		
		
		return mv;
	}


	@Override
	public int memberIdCheck(String memberId) {
		
		int value=0;
		value = msm.memberIdCheck(memberId);
		return value;
	}


	@Override
	public ArrayList<MemberVo> memberList() {
		System.out.println("msm"+msm);
		ArrayList<MemberVo> alist = msm.memberList();
		return alist;
	}

}
