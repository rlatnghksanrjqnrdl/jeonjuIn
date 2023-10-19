<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="app.domain.MainBoardVo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
//BoardVo bv = (BoardVo)request.getAttribute("bv");
if(session.getAttribute("midx")==null){
	out.println("<script>alert('로그인하셔야합니다.');location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
}
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>글내용보기</title>
<link href="./css/board.css" type="text/css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
//자동실행영역
$(document).ready(function(){
	$.boardCommentList();
	
	$("#save").on("click",function(){
// 		alert("클릭");
		
		let cwriter = $("#cwriter").val();
		let ccontents = $("#ccontents").val();
		let bidx = "<%//=bv.getBidx()%>${bv.bidx}";
		let midx = "<%=session.getAttribute("midx")%>";
		
		$.ajax({
			type : "post",
			url : "<%//=request.getContextPath()%>${pageContext.request.contextPath}/comment/commentWrite.do",
			dataType : "json",
			data:{
				"bidx":bidx,
				"midx":midx,
				"cwriter":cwriter,
				"ccontents":ccontents
			},
			cache : false,
			success : function(data){
// 				alert("통신성공");
// 				alert(data.value);
				if(data.value==1){
					alert("등륵완료");
				}
				$.boardCommentList();
				$("#cwriter").val("");
				$("#ccontents").val("");
			},
			error : function(){
				alert("통신오류 실패");
				
			}		
			
		});
	});
	
});

$.boardCommentList= function(){
	
	$.ajax({
		type : "get",
		url : "<%//=request.getContextPath()%>${pageContext.request.contextPath}/comment/commentList.do",
		dataType : "json",
		cache : false,
		success : function(data){
//			alert("통신성공");
			
				commentList(data);
				//$.each(data, function(index){
				//   alert(index);	
			     //  alert(data[index].cidx);		
			     // })			
		},
		error : function(){
			alert("통신오류 실패");
			
		}		
	});
	
}

function commentList(data){
	
	var str="";
	str="<tr><th>번호</th><th>작성자</th><th>내용</th><th>등록일</th><th></th></tr>";
	
	var delBtn="";
	var loginMidx="<%=session.getAttribute("midx")%>";
	
	
	
	$(data).each(function(){
		
		if(loginMidx==this.midx){
			delBtn ="<input type='button' value='삭제' id='btn' onclick='del("+this.cidx+")'>";
		}else{
			delBtn="";
		}
		
		str=str+"<tr><td>"+this.cidx+"</td><td>"+this.cwriter+"</td><td>"+this.ccontents+"</td><td>"+this.cwriteday+"</td><td>"+delBtn+"</td></tr>";
	});
	
	$("#tbl").html("<table border=1 style='width:600px;'>"+str+"</table>");
	return;
}
function del(cidx){
	$.ajax({
		type : "get",
		url : "<%//=request.getContextPath()%>${pageContext.request.contextPath}/comment/commentRemove.do?cidx="+cidx,
		dataType : "json",
		cache : false,
		success : function(data){
// 			alert("통신성공");
// 			alert(data.value);
			if(data.value==1){
				alert("삭제완료");
			}
			$.boardCommentList();
			//	commentList(data);
				//$.each(data, function(index){
				//   alert(index);	
			     //  alert(data[index].cidx);		
			     // })			
		},
		error : function(){
			alert("통신오류 실패");
			
		}		
	});
	return;
}
</script>
<style type="text/css">
.saveBtn{
text-align: center;
}
</style>
</head>
<body>
<h1>게시판 글내용</h1>
		<table border=1 style="width:600px;">
		<tr>
		<th>제목</th>
		<td>
		<%//=bv.getSubject() %>${bv.subject}		
		</td>
		<th>조회수</th>
		<td>
		<%//=bv.getViewcnt() %>${bv.viewcnt}
		</td>
		</tr>
		<tr>
		<th style="height:200px;">내용</th>
		<td colspan=3>
		<%//=bv.getContents() %>${bv.contents}	
		</td>
		</tr>
		<tr>
		<th>작성자</th>
		<td colspan=3>
		<%//=bv.getWriter() %>${bv.writer}
		</td>
		</tr>
		<tr>
		<th></th>
		<td style="text-align:right;">
		<button type="button" onclick="location.href='<%//=request.getContextPath()%>${pageContext.request.contextPath}/board/boardModify.do?bidx=<%//=bv.getBidx()%>${bv.bidx}'">수정</button>
		<button type="button" onclick="location.href='<%//=request.getContextPath()%>${pageContext.request.contextPath}/board/boardDelete.do?bidx=<%//=bv.getBidx()%>${bv.bidx}'">삭제</button>
		<button type="button" onclick="location.href='<%//=request.getContextPath()%>${pageContext.request.contextPath}/board/boardReply.do?bidx=<%//=bv.getBidx()%>${bv.bidx}&originbidx=<%//=bv.getOriginbidx()%>${bv.originbidx}&depth=<%//=bv.getDepth()%>${bv.depth}&level_=<%//=bv.getLevel_()%>${bv.level_}'">답변</button>
		<button type="button" onclick="location.href='<%//=request.getContextPath()%>${pageContext.request.contextPath}/board/boardList.do'">목록</button>			
		</td>
		</tr>
				
</table>
<table border=1 style="width:600px;">
<tr>
<th>작성자</th>
<td><input type="text" name="cwriter" id="cwriter" size="20"></td>
<td rowspan=2 class="saveBtn"><input type="button" value="저장" name="btn" id="save"></td>
</tr>
<tr>
<th>내용</th>
<td><textarea name="ccontents" cols="50" id="ccontents" rows="3" placeholder="내용을 입력해주세요"></textarea></td>
</tr>
</table>

<div id="tbl">
.

</div>

</body>
</html>