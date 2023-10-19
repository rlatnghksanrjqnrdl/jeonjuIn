<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="app.domain.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
//ArrayList<BoardVo> list = (ArrayList<BoardVo>)request.getAttribute("alist");
//PageMaker pm =(PageMaker)request.getAttribute("pm");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>게시글목록</title>
<link href="./css/board.css" type="text/css" rel="stylesheet">
<style type="text/css">
a{
  text-decoration-line: none;
}
.img{
width:20px;
height:20px;
}
.nav{

}
</style>
</head>
<body>
<h1>게시판 목록</h1>
<form name="frm" action="${pageContext.request.contextPath}/board/boardList.do" method="post">
<table border=0 style="width:600px">
<tr>
<td style="width:500px"></td>
<td>
<select name="searchType">
<option value="subject">제목</option>
<option value="writer">작성자</option>
</select>
</td>
<td>
<input type="text" name="keyword" size=10/>
</td>
<td>
<input type="submit" name="sbt" value="검색"/>
</td>
</tr>
</table>
</form>
<table border=1 style="width:600px">	
<thead>
		<tr>
		<th>글번호</th>
		<th>제목</th>
		<th>작성자</th>
		<th>조회수</th>
		<th>작성일</th>
		</tr>
</thead>
<tbody>	
		<%// for(BoardVo bv : list) {%>	
		<c:forEach var="bv" items="${alist}">
		<tr>
		<td><%//=bv.getBidx() %>${bv.bidx}</td>
		<td class="subject">
		<% //for(int i=1;i<=bv.getLevel_(); i++){
		//	out.print("&nbsp;&nbsp;");
		//		if(i==bv.getLevel_()){
		//			out.print("ㄴ");
		//		}
		
		//}%>
		<c:forEach var="i" begin="1" end="${bv.level_}" step="1">
		&nbsp;&nbsp;
		<c:if test="${i==bv.level_}">
		ㄴ
		</c:if>
		</c:forEach>
		<a href="${pageContext.request.contextPath}/board/boardContents.do?bidx=<%//=bv.getBidx() %>${bv.bidx}">
		<%//=bv.getSubject() %>
		${bv.subject}
		</a>
		
		</td>
		<td><%//=bv.getWriter() %>${bv.writer}</td>
		<td><%//=bv.getViewcnt() %>${bv.viewcnt}</td>
		<td><%//=bv.getWriteday() %>${bv.writeday}</td>
		</tr>
		</c:forEach>
		<%// } %>
</tbody>		
</table>
<%
//String parameter="";
//if(!pm.getScri().getKeyword().equals("")){
//	parameter="&searchType="+pm.getScri().getSearchType()+"&keyword="+pm.getScri().getKeyword()+"";
//}
%>

<c:set var="keyword" value="${pm.scri.keyword}"/>
<c:set var="parm" value="&searchType=${pm.scri.searchType}&keyword=${pm.scri.keyword}"/>
<a href="<%//=request.getContextPath()%>${pageContext.request.contextPath}/board/boardWrite.do">글쓰기</a>

<table border=0 style="width:600px;text-align:center;" class="nav">
<tr>
<td style="width:100px;text-align:right;">
<%// if(pm.isPrev()==true){%>
<c:if test="${pm.prev==true}">
<a href="<%//=request.getContextPath()%>${pageContext.request.contextPath}/board/boardList.do?page=<%//=pm.getStartPage()-1%>${pm.startPage-1}${parm}<%//=parameter%>">
<img src="/mvcstudy0803_t/img/leftarrow_80844.png" class="img" alt="left"/>
</a>
<%//} %>
</c:if>
</td>
<td>
<%
//for(int i =pm.getStartPage(); i<=pm.getEndPage(); i++){
%>
<c:forEach var="i" begin="${pm.startPage}" end="${pm.endPage}" step="1">
<a href="<%//=request.getContextPath()%>${pageContext.request.contextPath}/board/boardList.do?page=<%//=i%>${i}${parm}<%//=parameter%>"><%//=i%>${i}</a>&nbsp;
<%//}%>
</c:forEach>
</td>
<td style="width:100px;text-align:left;">
<%// if(pm.isNext()==true &&pm.getEndPage()>0){%>
<c:if test="${pm.next==true&&pm.endPage>0}">
<a href="<%//=request.getContextPath()%>${pageContext.request.contextPath}/board/boardList.do?page=<%//=pm.getEndPage()+1%>${pm.endPage+1}${parm}<%//=parameter%>">
<img src="/mvcstudy0803_t/img/rightarrow2_80915.png" class="img" alt="right"/>
</a>
<%//} %>
</c:if>
</td>

</table>

</body>
</html>