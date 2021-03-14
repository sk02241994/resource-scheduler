<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<script type="text/javascript" src="/ResourceScheduler/js/validation.js"></script>
<link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/login.css" />
</head>
<body class="body">
	<div class="title"><h1>Resource Scheduler</h1></div>
	
	<div class="center">
	
		<div class="form-validation">${errorMessage} ${validateEmail} ${disableError}</div>
		
		<form action="LoginServlet" method="post">
		
			<input type="text" name="uemail_address" placeholder="Email" maxlength="50">		
			<b id="error-mail"></b><br /> 
			
			<input type="password" name="user_password" placeholder="Password" maxlength="50"><br />
			
			<input type="submit" value="Submit">
		</form>
	</div>

</body>
</html>