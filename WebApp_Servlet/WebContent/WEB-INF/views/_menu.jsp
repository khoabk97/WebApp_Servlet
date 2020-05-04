<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!--  Dùng JSTL viết gọn link hoặc có thể dùng JSP Scriplet thông thường  -->
<%-- <%= request.getContextPath() %> --%>

<div align="center" style=" height: 20px; padding:2px">
<table  border="1" cellpadding = "5" cellspacing="1" style= "text-align:center">
	<tr>
		<td><a href="${pageContext.request.contextPath}/">Home</a></td>
		<td><a href="${pageContext.request.contextPath}/login">Login</a></td>
		<td><a href="${pageContext.request.contextPath}/userInfo">User Info</a></td>
	</tr>
</table>
</div>



