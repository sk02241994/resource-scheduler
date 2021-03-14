<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change Password</title>
<script type="text/javascript" src="/ResourceScheduler/js/validation.js"></script>
<link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/login.css" />
</head>
<body>

<div class="title"><h1>Resource Scheduler</h1></div>

<c:choose>

	<c:when test="${not empty success_message }">
	
	<div id="notification" class="notification">
		<div class="change-password">
			<span>${success_message}</span><br/>
			<input type="button" value="ok" onclick="goToLogin()">
		</div>
		</div>
	</c:when>
	
	<c:otherwise>
	<div class="change-password-center">
	
		<div class="form-validation">${errorMessageforchange} ${errorInPatternMatching} ${errorInPasswordMatching}</div>
		
		<form action="ChangePasswordServlet" method="post" onsubmit="return checkpassword(this);">
		
			<input type="text" name="uemail_address" value="${sessionScope.login_servlet_email}" readonly="readonly">
			
			 <input
				type="password" name="old_password" onblur="validatePassword(this);" placeholder="Enter Password" 
				maxlength="50">
				<b id="error-name"></b>
				
			<input type="password" name="new_password" onblur="validatePassword(this);" placeholder="Confirm Password"
			maxlength="50">
				
			<input type="submit" value="Change Password">
			
		</form>
		
	</div>
	
	</c:otherwise>
</c:choose>
	
</body>
</html>