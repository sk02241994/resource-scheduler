<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error</title>
<link rel="stylesheet" type="text/css"
	href="/ResourceScheduler/css/error.css" />
</head>
<body>

<div class="navbar">
   <%@include file="nav-bar.jsp" %>
</div>
<br/>

<b>${status_code}</b><br/>

<img alt="Error" src="/ResourceScheduler/rs-jsp/Error.png"><br/>

<b>${exception_message}<b><br/>

</body>
</html>