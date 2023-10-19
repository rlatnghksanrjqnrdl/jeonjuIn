package com.my0803.myapp.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import com.my0803.myapp.domain.BoardVo;
import com.my0803.myapp.domain.SearchCriteria;

public interface BoardService_Mapper {

	public int boardInsert(BoardVo bv);
	
	public int boardOriginBidxUpdate(int bidx);
	
	public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri);
	
	public int boardTotalCount(SearchCriteria scri);
	
	public BoardVo boardSelectOne(int bidx);
	
	public int boardCntUpdate(int bidx);
	
	public int boardModify(BoardVo bv);
	
	public int boardDelete(HashMap hm);
	
	public int boardReplyUpdate(BoardVo bv);
	
	public int boardReplyInsert(BoardVo bv);
}
