<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Info</title>
</head>
<body>
	<jsp:include page="_header.jsp"></jsp:include>
	<jsp:include page="_menu.jsp"></jsp:include>
	
	<div>
	<h3>Welcome "${user.userName}" logined to my WebApp!</h3>
	<b>Your information:</b><br/>
	<table border="1" cellpadding = "5" cellspacing="1" style= "text-align:center">
	<tr>
		<th><b>User Name</b></th>
		<th><b>Gender</b></th>
	</tr>
	<tr>
		<td>${user.userName}</td>
		<td>${user.gender}</td>
	</tr>
	</table>
	
	<a href="${pageContext.request.contextPath}/productList">Go to Product List Page</a>	
	</div>
	<jsp:include page="_footer.jsp"></jsp:include>
</body>
</html>