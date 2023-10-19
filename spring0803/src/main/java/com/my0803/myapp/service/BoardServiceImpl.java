package com.my0803.myapp.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my0803.myapp.domain.BoardVo;
import com.my0803.myapp.domain.SearchCriteria;
import com.my0803.myapp.persistance.BoardService_Mapper;

@Service
public class BoardServiceImpl implements BoardService{

	private BoardService_Mapper bsm;
	
	@Autowired
	public BoardServiceImpl(SqlSession sqlSession) {		
		this.bsm = 	sqlSession.getMapper(BoardService_Mapper.class);		
	}	
	
	@Override
	public int boardInsert(BoardVo bv) {
		
		int value = bsm.boardInsert(bv);
		int bidx = bv.getBidx();    //���̹�Ƽ������ ��ƿ� ��bidx
		//System.out.println("�Է��Ŀ� bidx����"+bidx);
		bsm.boardOriginBidxUpdate(bidx);
		
		return value;
	}

	@Override
	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri) {
		
		int value = (scri.getPage()-1)*15;		
		scri.setPage(value);		
		ArrayList<BoardVo> alist = bsm.boardSelectAll(scri);
		
		return alist;
	}

	@Override
	public int boardTotalCount(SearchCriteria scri) {
		
		int value = bsm.boardTotalCount(scri);
		
		return value;
	}

	@Override
	public BoardVo boardSelectOne(int bidx) {
		BoardVo bv = null;
		
		bv= bsm.boardSelectOne(bidx);
		
		return bv;
	}

	@Override
	public int boardCntUpdate(int bidx) {
		int value = bsm.boardCntUpdate(bidx);
		return value;
	}

	@Override
	public int boardModify(BoardVo bv) {		
		int value = bsm.boardModify(bv);
		System.out.println("value:"+value);
		return value;
	}

	@Override
	public int boardDelete(int bidx, String pwd) {
		
		String bidxs = bidx+"";
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("bidx", bidxs);
		hm.put("pwd", pwd);
		
		int value = bsm.boardDelete(hm);
		
		return value;
	}

	@Override
	public int boardReply(BoardVo bv) {
		
		bsm.boardReplyUpdate(bv);
		int value = bsm.boardReplyInsert(bv);  // 0 or 1
		//int bidx = bv.getBidx();    //���̹�Ƽ������ ��ƿ� ��bidx
				
		return value;
	}

	
	
	
	
}
