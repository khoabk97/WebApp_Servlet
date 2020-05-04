<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home Page</title>
</head>
<body>
	<!-- gắn những phần jsp mà các trang đều theo khuôn mẫu -->
	<jsp:include page="_header.jsp"></jsp:include>
	<jsp:include page="_menu.jsp"></jsp:include>
	<h3>Home Page</h3>
	<span>Simple Web application using JSP,SERVLET & JDBC</span> 
	<jsp:include page="_footer.jsp"></jsp:include>
</body>
</html>