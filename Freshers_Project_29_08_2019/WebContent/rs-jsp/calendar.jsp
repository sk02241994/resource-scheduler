<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.io.* "%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.altres.rs.model.Reservation" %>
<%@ page import="com.altres.rs.model.ReservationDetails" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Calendar</title>

<link rel="stylesheet" type="text/css"
	href="/ResourceScheduler/css/calendar.css" />
	<link rel="stylesheet" type="text/css"
	href="/ResourceScheduler/css/fontawesome.min.css" />
	
<script type="text/javascript">
var schedule= ${schedule}
var userId = ${sessionScope.login_servlet_user_id}
var isAdmin = ${sessionScope.login_is_admin}
</script>
</head>
<body>

  <div class="navbar">
    <%@include file="nav-bar.jsp" %>
  </div>
  
  <h3 id="month-and-year"></h3>
  
  <c:if test="${not empty success_message }">  
  	<div id="notification" class="notification">${success_message}
  		<span class="closeButton" onclick="this.parentElement.style.display='none';">&times;</span></div>
  	</c:if>
  	  	
  <div class="calendar-navigation">

  <form>
  
    <label for="month">Jump To: </label>
    <select name="month" id="month" onchange="jump()">
      <option value=0>January</option>
      <option value=1>February</option>
      <option value=2>March</option>
      <option value=3>April</option>
      <option value=4>May</option>
      <option value=5>June</option>
      <option value=6>July</option>
      <option value=7>August</option>
      <option value=8>September</option>
      <option value=9>October</option>
      <option value=10>November</option>
      <option value=11>December</option>
    </select>
    
    <label for="year"></label>
    <select id="year" onchange="jump()"></select>
  </form>
  
  <input type="button" onclick="previous()" value="Previous" class="previous">
  <label class="resource">Resources: 
   <select name="resource_filter" onchange="filterByResource(this.value);">
   			<option value="">All</option>
			<c:forEach items="${resources}" var="resource">
				<option <c:if test="${singleReservation.resourceId eq resource.rsResourceId}">selected="selected"</c:if>
					value="${resource.rsResourceId}">${resource.resourceName}</option>
			</c:forEach>
   </select> 
   </label>
   <input type="button" onclick="next()" value="Next" class="next"> 
   </div>
   <div class="calendar-table">
  <table>
    
    <thead>
      <tr>
        <th>Sunday</th>
        <th>Monday</th>
        <th>Tuesday</th>
        <th>Wednesday</th>
        <th>Thursday</th>
        <th>Friday</th>
        <th>Saturday</th>
      </tr>
    </thead>
    	
    <tbody id="calendar-body">
      
    </tbody>
  
  
  </table>
  </div>
  
	<div id="display-all-day-data">
		<div id="data">
		</div>
	</div>


<c:if test="${not empty singleReservation || not empty error_message}">
	  <div id="edit-reservation" class="edit-reservation">
	    
	    <form action="ReservationServlet" method="post" class="reservation-form" onsubmit="return validateEditForm(this)"
	    name="edit_Reservation">
	    
	    <table>
	    	<tr>
	    		<td colspan="2">
	    			<div class="cancel-container">
	          		<span onclick="document.getElementById('edit-reservation').style.display='none'"
	          		class="close">&times;</span>
	       			</div>
	       			
	       			<input type="hidden" name="edit_reservation" value="edit_calendar_reservation">
	      			<input type="hidden" name="reservation_id" value="${singleReservation.reservationId}"/>
	      			<input type="hidden" value="${singleReservation.userId}" name="user_id">
	      			
	      			<div id="form-validation-editing" style="${not empty error_message || not empty error_message_empty ? 'display:block' : ''}">
		   			<b id="error-date"></b>
		   			<b id="error-time"></b>
		   			<c:if test="${not empty error_message || not empty error_message_empty}">
		   			<b>${error_message}</b>
		   			<b>${error_message_empty}</b>
		   			</c:if>
	   				</div>	
	    		</td>
	    	</tr>
	    	
	    	<tr>
	    		<td><label>Resource Name: </label></td>
	    		<td>
	    			<select name="resource_name">
		     		<c:forEach items="${resources}" var="resource" >
		       		<option
		       		<c:if test="${singleReservation.resourceId eq resource.rsResourceId}">selected="selected"</c:if>
		       		value="${resource.rsResourceId}">${resource.resourceName}</option>
		     		</c:forEach>
		   			</select>
	    		</td>
	    	</tr>
	    	
	    	<tr>
	    		<td><label>Start Date<span class="required">*</span>: </label></td>
	    		<td>
	    			<input type="date" name = "start_date" placeholder="Enter Date : DD-MM-YYYY" 
		   			value="${singleReservation.startDate}" onblur="getDate(this)" maxlength="10">
	    		</td>
	    	</tr>
	    	
	    	<tr>
	    		<td><label>Start Time<span class="required">*</span>: </label></td>
	    		<td>
	    			<input type="time" name="start_time" placeholder="Enter Time : HH:MM 24HR" id="time-start"
		   			value="${singleReservation.startTime }" onblur="getTime(this)" maxlength="5">
	    		</td>
	    	</tr>
	    	
	    	<tr>
	    		<td><label>End Date<span class="required">*</span>: </label></td>
	    		<td>
	    			<input type="date" name="end_date" placeholder="Enter Date : DD-MM-YYYY"
		   			value="${singleReservation.endDate }" onblur="getDate(this)" maxlength="10">
	    		</td>
	    	</tr>
	    	
	    	<tr>
	    		<td><label>End Time<span class="required">*</span>: </label></td>
	    		<td>
	    			<input type="time" name="end_time" placeholder="Enter Time : HH:MM 24HR" id="time-end"
		   			value="${singleReservation.endTime }" onblur="getTime(this)" maxlength="5">
	    		</td>
	    	</tr>
	    	
	    	<tr>
	    		<td><label>All Day</label></td>
	    		<td><input type="checkbox" name="is_all_day" onclick="enableDisableTextBox(this)">
				</td>
	    	</tr>
	    	
	    	<tr>
	    	<td colspan="2">
	    		<input type="submit" value="Save">
		   		<input type="button" onclick="document.getElementById('edit-reservation').style.display='none'"
  				class="add-reservation-button" value="Cancel"> 		   
	    	</td>
	    	</tr>
	    </table>

	    </form>
	    
	  </div>
  </c:if>
  
  
  
  <div id="add-reservation" class="add-reservation">
  
	 <form action="ReservationServlet" method="post" class="reservation-form" onsubmit="return validateAddForm(this)" 
	 name="add_Reservation">
	   
	   <table>
	   		<tr>
	   			<td colspan="2">
						<div class="cancel-container">
							<span onclick="document.getElementById('add-reservation').style.display='none'" 
							class="close">&times;</span><br />
						</div>

						<div id="form-validation-adding" style="${not empty error_message || not empty error_message_empty ? 'display:block' : ''}">
							<b id="error-date"></b> 
							<b id="error-time"></b> 
							<b>${error_message}</b> 
							<b>${error_message_empty}</b>
						</div>
						<input type="hidden" value="${sessionScope.login_servlet_user_id}" name="user_id">
						<input type="hidden" value="add_form_calendar" name="edit_reservation">
				</td>
	   		</tr>
	   		
	   		<tr>
	   			<td><label>Resource Name: </label></td>
	   			<td>
	   				<select name="resource_name">
	     			<c:forEach items="${resources}" var="resource" >
	     			<option value="${resource.rsResourceId}">${resource.resourceName}</option>
	     			</c:forEach>
	   				</select>
	   			</td>
	   		</tr>
	   		
	   		<tr>
	   			<td><label>Start Date<span class="required">*</span>: </label></td>
	   			<td>
	   			  <input type="date" name="start_date" id="start_date" placeholder="Enter Date : DD-MM-YYYY"
	   			  onblur="getDate(this)" maxlength="10" value="${fn:escapeXml(param.start_date)}">
	   			</td>
	   		</tr>
	   		
	   		<tr>
	   			<td><label>Start Time<span class="required">*</span>: </label></td>
	   			<td>
	   			  <input type="time" name="start_time" name="start_time" placeholder="Enter Time : HH:MM 24HR" id="time-start"
	   			  onblur="getTime(this)" maxlength="5" value="${fn:escapeXml(param.start_time)}">
	   			</td>
	   		</tr>
	   		
	   		<tr>
	   			<td><label>End Date<span class="required">*</span>: </label></td>
	   			<td>
	   			  <input type="date" name="end_date" id="end_date" placeholder="Enter Date : DD-MM-YYYY"
	   			  onblur="getDate(this)" maxlength="10" value="${fn:escapeXml(param.end_date)}">
	   			</td>
	   		</tr>
			
			<tr>
				<td><label>End Time<span class="required">*</span>: </label></td>
				<td>
				  <input type="time" name="end_time" placeholder="Enter Time : HH:MM 24HR" id="time-end"
	   			  onblur="getTime(this)" maxlength="5" value="${fn:escapeXml(param.end_time)}">
				</td>
			</tr>
			
			<tr>
				<td><label>All Day</label></td>
				<td>
					<input type="checkbox" name="is_all_day" onclick="enableDisableTextBox(this)">
				</td>	
			</tr>
			
			<tr> 
				<td colspan="2">
					<input type="submit" value="Book">
				   <input type="button" onclick="document.getElementById('add-reservation').style.display='none'"
  					class="add-reservation-button" value="Cancel">
				</td>
			</tr>
			
	   </table>
	 </form>
  
  </div>
  
  
  
  

</body>
<script type="text/javascript" src="/ResourceScheduler/js/get-data-for-calendar.js"></script>
<script type="text/javascript" src="/ResourceScheduler/js/calendar.js"></script>

</html>