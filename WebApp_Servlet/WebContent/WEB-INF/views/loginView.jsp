<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>
	<jsp:include page="_header.jsp"></jsp:include>
	<jsp:include page="_menu.jsp"></jsp:include>
	
	<h3>Login Page</h3>
	<!-- Dòng thông báo lỗi sẽ hiện ở đây -->
	<p style = "color:red">${errorString}</p>
	
	<form method="POST" action="${pageContext.request.contextPath}/login">
		<!-- Tạo table để sắp key+value thẳng hàng thẳng cột -->
		<table>
			<tr>
				<td>User Name</td>
				<%
					
				%>
				<td><input type="text" name="userName" value="${FINAL_USERNAME}" /></td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input type="password" name="password" value="${user.password}" /></td>
			</tr>
			<tr>
				<td>Remember me <input type="checkbox" name="rememberMe" value = "Y" /></td>
			</tr>
			
			<tr>
				<td colspan = "2">
					<input type="submit" value="Submit" />
					<a href = "${pageContext.request.contextPath}/login?logout=true" >Logout</a>
				</td>
			</tr>
		</table>	
	</form>
	
	<p style="color:blue">Hint: U/P: tom/tom001 or jerry/jerry001</p>
	
	<jsp:include page="_footer.jsp"></jsp:include>
</body>
</html>