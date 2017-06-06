<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Login</title>
</head>
<body>
	<form action="./Login" >
		<h1>Welcome to TalkyTalky</h1>
		<h3>username 
		<input type="text" name="username" />
		</h3>
		
		<h3>password
		<input type="password" name="password" />
		</h3>
		<input type="submit" value="login"/>
	</form> 
	
	<form action="./register.jsp">
		<input type="submit" value="register" />
	</form>
</body>
</html>