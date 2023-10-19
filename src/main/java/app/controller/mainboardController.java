package app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import app.dao.MainBoardDao;
import app.domain.MainBoardVo;
import app.domain.PageMaker;
import app.domain.SearchCriteria;

// HttpServlet를 상속받았기때문에 클래스가 인터넷페이지가 된다
@WebServlet("/BoardController")
public class mainboardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String location; 
	public mainboardController(String location){
		this.location = location;
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//문자열 비교
		if (location.equals("boardList.do")) {	
			
			String searchType =request.getParameter("searchType");
			if(searchType==null) {
				searchType="subject";
			}// 실행문이 1개일시 {} 생략가능
			String keyword =request.getParameter("keyword");
			if(keyword==null) {
				keyword="";
			}
			
			
			String page =request.getParameter("page");
			if(page==null) {
				page="1";
			}
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(Integer.parseInt(page));
			scri.setSearchType(searchType);
			scri.setKeyword(keyword);
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);
			
			//1. DB에 있는 board0803 게시물 전체를 가지고온다
			MainBoardDao bd =new MainBoardDao();
			ArrayList<MainBoardVo> alist =bd.boardSelectAll(scri);
			int cnt = bd.boardTotalCount(scri); //전체게시물수
			pm.setTotalCount(cnt);
			
			
			
			request.setAttribute("alist", alist);
			request.setAttribute("pm", pm);
			
			 String path ="/board/boardList.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);	
		
		}else if(location.equals("boardWrite.do")) {
			
			
			 String path ="/board/boardWrite.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);
			
		}else if(location.equals("boardWriteAction.do")) {
			
			String savePath="D:\\dev0803\\mvcstudy0803_t\\src\\main\\webapp\\img"; //저장되는 위치
			int sizeLimit = 15*1024*1024; //1024=1kbyte  1024*1024 =mbyte
			String dataTy ="UTF-8";
			//파일이름 중복정책
			DefaultFileRenamePolicy drp = new DefaultFileRenamePolicy();
			
			MultipartRequest multi = new MultipartRequest(request,savePath,sizeLimit,dataTy,drp);
														//전달받은값
			
			
			
			//1.넘긴값을 받는다
			//String subject =request.getParameter("subject");
			String subject = multi.getParameter("subject");
			String contents = multi.getParameter("contents");
			String writer = multi.getParameter("writer");
			String pwd = multi.getParameter("pwd");
			//파일 넘겨받기
			Enumeration files = multi.getFileNames();
			//파일 객체를 꺼낸다
			String file = (String)files.nextElement();
			//파일의 이름을 추출한다(실제로 저장되는 파일이름)
			String fileName = multi.getFilesystemName(file);
			//원래 파일 이름
			String originFileName = multi.getOriginalFileName(file);
			
			int midx = 0;
			HttpSession session = request.getSession();
			midx = (int)session.getAttribute("midx");
			
			
			//2. 받은값을 입력한다
			MainBoardVo bv = new MainBoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setFilename(fileName);
			bv.setPwd(pwd);
			bv.setMidx(midx);
			
			MainBoardDao bd =new MainBoardDao();
			int value = bd.BoardInsert(bv);
			
			//3. 처리가 끝났으면 새롭게 이동한다
			if(value ==0) {
				String path =request.getContextPath()+"/board/boardWrite.do";
				response.sendRedirect(path);
			}else {
				String path =request.getContextPath()+"/board/boardList.do";
				response.sendRedirect(path);
			}
			
			
		}
		else if(location.equals("boardContents.do")) {
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			
			
			MainBoardDao bd= new MainBoardDao();
			int exec = bd.BoardCntUpdate(bidx_int);
			MainBoardVo bv = bd.boardSelectOne(bidx_int);
			
			request.setAttribute("bv",bv);
			
			
			String path ="/board/boardContents.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);
			 
		}else if(location.equals("boardModifyAction.do")) {
			//1.수정데이터 받고
			String bidx = request.getParameter("bidx");
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String pwd = request.getParameter("pwd");
			String ip= InetAddress.getLocalHost().getHostAddress();
			
			//2. 받은값을 입력한다
			MainBoardVo bv = new MainBoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPwd(pwd); 
			bv.setIp(ip);
			
			MainBoardDao bd =new MainBoardDao();
			int value = bd.BoardModify(bv);
			
			PrintWriter out =response.getWriter();
			//3. 처리가 끝났으면 새롭게 이동한다
			if(value ==0) {	//수정이 안되었으면 수정페이지 다시
				
				out.println("<script>alert('비밀번호가 일치하지않습니다.');</script>");
				
				String path =request.getContextPath()+"/board/boardModify.do?bidx="+bidx;
				response.sendRedirect(path);
				
			}else {	//수정했으면 내용페이지
				String path =request.getContextPath()+"/board/boardContents.do?bidx="+bidx;
				response.sendRedirect(path);
			
			}
			
			
			
		}else if(location.equals("boardModify.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			
			MainBoardDao bd= new MainBoardDao();
			MainBoardVo bv = bd.boardSelectOne(bidx_int);
			
			request.setAttribute("bv",bv);
			
			String path ="/board/boardModify.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);
			 
		}else if(location.equals("boardDelete.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			
			MainBoardDao bd= new MainBoardDao();
			MainBoardVo bv = bd.boardSelectOne(bidx_int);
			
			request.setAttribute("bv",bv);
		
			String path ="/board/boardDelete.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다, 같은 지역이므로 공유해서 꺼낼쓸수 있다.
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);
			 
		}else if(location.equals("boardDeleteAction.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			String pwd = request.getParameter("pwd");
			
			//처리하는 메소드를 만들어야 함
			
			
			
			
			MainBoardDao bd =new MainBoardDao();
			int value = bd.boardDelete(bidx_int, pwd);
			
			//처리가 되면 1 아니면 0이 나옴
			if(value!=0) {
				String path =request.getContextPath()+"/board/boardList.do";
				response.sendRedirect(path);
				
			}else {	//삭제가 안됬으면 다시 딜리트페이지
				String path =request.getContextPath()+"/board/boardDelete.do?bidx="+bidx;
				response.sendRedirect(path);
			}
			
			
		
		}else if(location.equals("boardReply.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			String originbidx = request.getParameter("originbidx");
			int originbidx_int = Integer.parseInt(originbidx);
			String depth = request.getParameter("depth");
			int depth_int = Integer.parseInt(depth);
			String level_ = request.getParameter("level_");
			int level_int = Integer.parseInt(level_);
			
			
			
			MainBoardVo bv= new MainBoardVo();
			bv.setBidx(bidx_int);
			bv.setOriginbidx(originbidx_int);
			bv.setDepth(depth_int);
			bv.setLevel_(level_int);
			
			request.setAttribute("bv",bv);
			
		
			String path ="/board/boardReply.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);
			 
			 
		}else if(location.equals("boardReplyAction.do")) {
			
			
			
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			String originbidx = request.getParameter("originbidx");
			int originbidx_int = Integer.parseInt(originbidx);
			String depth = request.getParameter("depth");
			int depth_int = Integer.parseInt(depth);
			String level_ = request.getParameter("level_");
			int level_int = Integer.parseInt(level_);
			String subject =request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String pwd = request.getParameter("pwd");
			String ip= InetAddress.getLocalHost().getHostAddress();
			
			int midx = 0;
			HttpSession session = request.getSession();
			midx = (int)session.getAttribute("midx");
			
			//2. 받은값을 입력한다
			MainBoardVo bv = new MainBoardVo();
			bv.setBidx(bidx_int);
			bv.setOriginbidx(originbidx_int);
			bv.setDepth(depth_int);
			bv.setLevel_(level_int);
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPwd(pwd);
			bv.setIp(ip);
			bv.setMidx(midx);
			//bv.setFilename(fileName);
			
			int value =0 ;
			MainBoardDao bd =new MainBoardDao();
			value = bd.boardReply(bv);
//			System.out.println("val="+value);
			//3. 처리가 끝났으면 새롭게 이동한다
			if(value !=0) {	//실패
				String path =request.getContextPath()+"/board/boardList.do";
				response.sendRedirect(path);
			}else {	//성공
				String path =request.getContextPath()+"/board/boardReply.do?bidx="+bidx+"&originbidx="+originbidx+"&depth="+depth+"&level_="+level_;
				response.sendRedirect(path);
			}
			
			
			
			
		}
		
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
