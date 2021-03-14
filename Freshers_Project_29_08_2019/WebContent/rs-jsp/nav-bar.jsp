<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/nav-bar.css" />
  </head>
  
  <body>
    <nav>
      <ul>
      
        <c:choose>
          
          <c:when test="${sessionScope.login_servlet_user_id eq 1 }">
            <li><a href="ReservationServlet">Home</a></li>
            <li><a href="UserServlet">Manage User</a></li>
            <li><a href="ResourceServlet">Manage Resource</a></li>
            <li class="logout"><a href="LoginServlet?action=logout">Logout</a></li>
            <li><a href="ReservationServlet?form_action=rs-jsp/calendar.jsp">Calendar</a></li>
          </c:when>
          
          <c:otherwise>
            <li><a href="ReservationServlet">Home</a></li>
            <li><a href="ReservationServlet?form_action=rs-jsp/calendar.jsp">Calendar</a></li>
            <li class="logout"><a href="LoginServlet?action=logout">Logout</a></li>
          </c:otherwise>
          
        </c:choose>
        
      </ul>
    </nav>
  </body>
</html>