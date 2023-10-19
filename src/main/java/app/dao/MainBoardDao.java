package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.dbconn.DbConn;
import app.domain.BoardVo;
import app.domain.MainBoardVo;
import app.domain.SearchCriteria;

public class MainBoardDao {
	
	private Connection conn;  //멤버변수는 선언만해도 자동초기화됨
	private PreparedStatement pstmt;
	
	public MainBoardDao() {
		DbConn dbconn = new DbConn();
		this.conn = dbconn.getConnection();
	}
	
	public ArrayList<MainBoardVo>  boardSelectAll(SearchCriteria scri){
		//무한배열클래스 객체생성해서 데이터를 담을 준비를 한다
		ArrayList<MainBoardVo> alist =new ArrayList<MainBoardVo>();
		ResultSet rs = null;
		
		
		String str="";
		if(!scri.getKeyword().equals("")) {
			str=" and "+scri.getSearchType()+" like concat('%','"+scri.getKeyword()+"','%')";
		}
		String sql="select boardtype,bidx,subject,viewcnt,writeday\r\n"
				+ "from mainboard \r\n"
				+ "where delyn='N'\r\n"
				+ str
				+ "order by originbidx DESC,depth limit ?,?";
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
				MainBoardVo bv = new MainBoardVo();
				//rs에서 midx값 꺼내서 mv에 옮겨담는다
				bv.setBoardtype(rs.getString("boardtype"));
				bv.setBidx( rs.getInt("Bidx") ); 
				bv.setSubject( rs.getString("Subject") );
				bv.setViewcnt( rs.getInt("viewcnt"));
				bv.setWriteday( rs.getString("writeday"));
				alist.add(bv);
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
	
	public int BoardInsert(MainBoardVo bv) {
		
		int exec = 0;
		
		String sql = "INSERT INTO mainboard0803(boardtype,SUBJECT,CONTENTS,WRITER,midx,pwd,filename)\r\n"
				+ "VALUES(Null,0,0,?,?,?,?,?,?)";
		String sql2 = "UPDATE board0803 set \r\n"
				+ "originbidx =(SELECT a.bidx from(select max(bidx) as bidx from board0803 )a)\r\n"
				+ "where bidx=(SELECT a.bidx from(select max(bidx) as bidx from board0803 )a)";
		
		try{
		conn.setAutoCommit(false);
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, bv.getSubject());
		pstmt.setString(2, bv.getContents());
		pstmt.setString(3, bv.getWriter());
		pstmt.setInt(4, bv.getMidx());
		pstmt.setString(5, bv.getPwd());
		pstmt.setString(6, bv.getFilename());
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(sql2);
		exec = pstmt.executeUpdate();
		
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
}
