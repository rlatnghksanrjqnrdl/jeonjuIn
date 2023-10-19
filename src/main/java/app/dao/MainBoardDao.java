package app.dao;
//mainboardDao finisch?
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.dbconn.DbConn;
import app.domain.MainBoardVo;
import app.domain.MemberVo;
import app.domain.SearchCriteria;

public class MainBoardDao {
	
	private Connection conn;  //멤버변수는 선언만해도 자동초기화됨
	private PreparedStatement pstmt;
	
	public MainBoardDao() {
		DbConn dbconn = new DbConn();
		this.conn = dbconn.getConnection();
	}
	
	public int BoardInsert(MainBoardVo mbv) {
		
		int exec = 0;
		
		String sql = "INSERT INTO mainboard0803(boardtype,SUBJECT,CONTENTS,midx,filename)\r\n"
				+ "VALUES(?,?,?,?,?)";

		
		try{
		conn.setAutoCommit(false);
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, mbv.getBoardtype());
		pstmt.setString(2, mbv.getSubject());
		pstmt.setString(3, mbv.getContents());
		pstmt.setInt(4, mbv.getMidx());
		pstmt.setString(5, mbv.getFilename());
		pstmt.executeUpdate();

		
		conn.commit();
		
		}catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return exec;	
	}
	
	public int boardTotalCount(SearchCriteria scri){
		int value=0;  // 결과값이 0인지 아닌지
		
		String str="";
		if(!scri.getKeyword().equals("")) {
			str=" and "+scri.getSearchType()+" like concat('%','"+scri.getKeyword()+"','%')";
		}
		
		String sql="select count(*) as cnt from mainboard0803 where delyn='N'"+str;
		ResultSet rs = null;
		try{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs.next()){
				value =	rs.getInt("cnt");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				pstmt.close();
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return value;
	}
	
	public MainBoardVo boardSelectOne(int bidx) {
		MainBoardVo mbv = null;
		MemberVo mv = null;
		String sql="select * from mainboard0803 a join memberb on a.midx=b.midx where bidx=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//향상된 구문 클래스스를 꺼낸다
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				//bv생성하고 결과값 옮겨담기
				mbv = new MainBoardVo();
				mv = new MemberVo();
				mbv.setSubject(rs.getString("subject"));
				mbv.setContents(rs.getString("contents"));
				mv.setMemberId(rs.getString("memberId"));
				mv.setNickname(rs.getString("nickname"));
				mbv.setBidx(rs.getInt("bidx"));
				mbv.setViewcnt(rs.getInt("viewcnt"));
				
			}
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				pstmt.close();
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		
		return mbv;
	}
	
	public ArrayList<MainBoardVo>  boardSelectAll(SearchCriteria scri){
		//무한배열클래스 객체생성해서 데이터를 담을 준비를 한다
		ArrayList<MainBoardVo> alist =new ArrayList<MainBoardVo>();
		ResultSet rs = null;
		
		
		String str="";
		if(!scri.getKeyword().equals("")) {
			str=" and "+scri.getSearchType()+" like concat('%','"+scri.getKeyword()+"','%')";
		}
		String sql="select a.boardtype,a.bidx,a.subject,a.viewcnt,a.writeday,b.memberid,b.nickname\r\n"
				+ "from mainboard a join member b on a.midx=b.midx \r\n"
				+ "where delyn='N'\r\n"
				+ str
				+ "order by bidx DESC limit ?,?";
		try{
			//1.창고(컬렉션)를 만든다
			//2.쿼리를 실행해서 데이터를 전용객체에 담아온다 
			//3.전용객체에 있는 데이터를 회원객체(MemberVo)에 옮겨담는다 
			//4.회원객체를 창고에 집어넣는다	
			
			//구문(쿼리)객체
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,(scri.getPage()-1)*scri.getPerPageNum());
			pstmt.setInt(2,scri.getPerPageNum());
			
			//DB에 있는 값을 담아오는 전용객체
			rs = pstmt.executeQuery();
			//rs.next()는 다음값이 있는지 확인하는 메소드 있으면true
			while(rs.next()){
				MainBoardVo mbv = new MainBoardVo();
				MemberVo mv = new MemberVo();
				//rs에서 midx값 꺼내서 mv에 옮겨담는다
				mbv.setBoardtype(rs.getString("boardtype"));
				mbv.setBidx( rs.getInt("Bidx") ); 
				mbv.setSubject( rs.getString("Subject") );
				mbv.setViewcnt( rs.getInt("viewcnt"));
				mbv.setWriteday( rs.getString("writeday"));
				mv.setMemberId(rs.getString("memberId"));
				mv.setNickname(rs.getString("nickname"));
				alist.add(mbv);
			}		
			
		}catch(Exception e){
			e.printStackTrace();		
		}finally{
			try{
				rs.close();
				pstmt.close();
			//	conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return alist;
	}
	
	public int BoardCntUpdate(int bidx) {
		
		int exec = 0;
		
		String sql = "UPDATE mainboard0803 set \r\n"
				+ "viewcnt =viewcnt+1\r\n"
				+ "where bidx=?";
		
		try{
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,bidx);
		exec = pstmt.executeUpdate();

		}catch(Exception e){
			e.printStackTrace();
		}	
			return exec;		
	}
	
public int BoardModify(MainBoardVo bv) {
		
		int exec = 0;
		
		
		String sql = "UPDATE mainboard0803 set \r\n"
				+ "subject =?, \r\n"
				+ "contents =?, \r\n"
				+ "modifyday = now(), \r\n"
				+ "ip =? \r\n"
				+ "where bidx = ?";
		
		try{
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,bv.getSubject());
		pstmt.setString(2,bv.getContents());
		pstmt.setString(3,bv.getIp());
		pstmt.setInt(4,bv.getBidx());
		
		exec = pstmt.executeUpdate();
		//수정이 되면 1이 리턴
		}catch(Exception e){
			e.printStackTrace();
		}	
			return exec;			
	}
	
	public int boardDelete(int bidx) {
	
	int exec = 0;
	
	
	String sql ="UPDATE mainboard0803 set \r\n"
			+ "delyn='Y', modifyday=now() \r\n"
			+ "where bidx=?";
	
	try {
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,bidx);
		exec = pstmt.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return exec;
}
	
}
