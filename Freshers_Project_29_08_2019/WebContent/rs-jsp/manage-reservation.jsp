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
<title>Reservation</title>
<link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/common.css" />
<link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/manage-reservation.css" />
<script type="text/javascript" src="/ResourceScheduler/js/manage_reservation.js"></script>
</head>
<body onload="validateErrorInTime()">

	<div class="navbar">
		<%@include file="nav-bar.jsp"%>
	</div>

	<button onclick="document.getElementById('add-field').style.display='block'" class="add-button">
	Add</button>
	<br/>
  
	<%@include file="notification.jsp"%>
	<table>

		<tr>

			<th>User Name</th>
			<th>Resource Name</th>
			<th>Start Date</th>
			<th>Start Time</th>
			<th>End Date</th>
			<th>End Time</th>
			<c:if test="${sessionScope.login_is_admin}">
				<th>Action</th>
			</c:if>
		</tr>

		<c:forEach items="${reservations}" var="reservation">

			<tr>

				<td><input type="hidden" value="${reservation.userId}"> <c:out value="${reservation.userName}" /></td>

				<td><input type="hidden" value="${reservation.resourceId}"> <c:out value="${reservation.resourceName}"/></td>

				<td><c:out value="${reservation.startDate}" /></td>

				<td><c:out value="${reservation.startTime}" /></td>

				<td><c:out value="${reservation.endDate}" /></td>

				<td><c:out value="${reservation.endTime}" /></td>

				<td><c:choose>
						<c:when test="${sessionScope.login_is_admin}">
							<input type="button" onclick="getId('${reservation.reservationId}')" value="Edit">
							<input type="button" onclick="getIdForDelete('${reservation.reservationId}')" value="Delete">
						</c:when>
					</c:choose></td>

			</tr>
		</c:forEach>
	</table>

	<br/>
  
  
  <c:if test="${not empty singleReservation }">
	  <div id="edit-field" class="edit-field">
	    
	    <form action="ReservationServlet" method="post" class="edit-form" onsubmit="return validateEditForm(this)"
	    name="edit_Reservation">
	    
	      <table>
	      
	      	<tr>
	      		<td colspan="2">
					
					<div class="cancel-container">
					<span onclick="document.getElementById('edit-field').style.display='none'" class="close">
					&times;</span>
					</div>
					
					 <input type="hidden" name="edit_reservation" value="edit_reservation"> 
					 <input type="hidden" name="reservation_id" value="${singleReservation.reservationId}" /> 
					 <input type="hidden" value="${sessionScope.login_servlet_user_id}" name="user_id">
					 
					<div id="form-validation-editing">
					<b id="error-date"></b> 
					<b id="error-time"></b> 
					<b>${error_message}</b> 
					<b>${error_message_empty}</b>
					</div>
				</td>
	      	</tr>
	      	
	      	<tr>
	      		<td><label>Resource Name: </label></td>
	      		<td>
	      			<select name="resource_name">
					<c:forEach items="${resources}" var="resource">
					<option <c:if test="${singleReservation.resourceId eq resource.rsResourceId}">
					selected="selected"</c:if>
					value="${resource.rsResourceId}">${resource.resourceName}</option>
					</c:forEach>
					</select>
				</td>
	      	</tr>
	      	
	      	<tr>
	      		<td>
	      		<input type="hidden" value="${sessionScope.login_servlet_user_id}" name="user_id">
	      		<label>Start Date<span class="required">*</span>: </label>
	      		</td>
	      		
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
		   			<input type="button" onclick="document.getElementById('edit-field').style.display='none'"
  					class="add-reservation-button" value="Cancel">
	      		</td>
	      	</tr>
	      	
	      </table>

	    </form>
	    
	  </div>
  </c:if>
  
  
  <div id="add-field" class="add-field">
  
	 <form action="ReservationServlet" method="post" class="edit-form" onsubmit="return validateAddForm(this)" 
	 name="add_Reservation">
	   
	   <table>
	   		<tr>
	   			<td colspan="2">
						<div class="cancel-container">
							<span onclick="document.getElementById('add-field').style.display='none'" 
							class="close">&times;</span><br />
						</div>

						<div id="form-validation-adding">
							<b id="error-date"></b> 
							<b id="error-time"></b> 
							<b>${error_message}</b> 
							<b>${error_message_empty}</b>
						</div>
						<input type="hidden" value="${sessionScope.login_servlet_user_id}" name="user_id">
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
	   			  <input type="date" name = "start_date" placeholder="Enter Date : DD-MM-YYYY"
	   			  onblur="getDate(this)" maxlength="10" value="${fn:escapeXml(param.start_date)}">
	   			</td>
	   		</tr>
	   		
	   		<tr>
	   			<td><label>Start Time<span class="required">*</span>: </label></td>
	   			<td>
	   			  <input type="time" name="start_time" placeholder="Enter Time : HH:MM 24HR" id="time-start"
	   			  onblur="getTime(this)" maxlength="5" value="${fn:escapeXml(param.start_time)}">
	   			</td>
	   		</tr>
	   		
	   		<tr>
	   			<td><label>End Date<span class="required">*</span>: </label></td>
	   			<td>
	   			  <input type="date" name="end_date" placeholder="Enter Date : DD-MM-YYYY"
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
				   <input type="button" onclick="document.getElementById('add-field').style.display='none'"
  					class="add-reservation-button" value="Cancel">
				</td>
			</tr>
			
	   </table>
	 </form>
  
  </div>
  
  <c:if test="${reservations.size() gt 10}">
    
    <button onclick="document.getElementById('add-reservation').style.display='block'"
    class="add-reservation-button">Add</button>

  </c:if>
  
  
 
</body>
</html>