package com.my0803.myapp.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import com.my0803.myapp.domain.MemberVo;

//���̹�Ƽ������ ����� �޼ҵ带 �����س�����
public interface MemberService_Mapper {
	
	public int memberInsert(MemberVo mv);
	
	public MemberVo memberLogin(HashMap hm);
	
	public MemberVo memberLogin2(String memberId);
	
	public int memberIdCheck(String memberId);
	
	public ArrayList<MemberVo> memberList();
	
	
}
