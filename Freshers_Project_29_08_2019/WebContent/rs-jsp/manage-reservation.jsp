<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.* "%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.altres.rs.model.Reservation"%>
<%@ page import="com.altres.rs.model.ReservationDetails"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reservation</title>
<link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/common.css" />
<link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/manage-reservation.css" />
<script type="text/javascript" src="/ResourceScheduler/js/manage_reservation.js"></script>
<script type="text/javascript" src="/ResourceScheduler/js/display-error.js"></script>
<script type="text/javascript" src="/ResourceScheduler/js/common.js"></script>
</head>
<script> 

executeEvent(window, 'load', function(){
	displayModalWindow(${not empty reservation.reservationId}, ${reservation.reservationId});
	addNoticeFormModal(${error_message_modal});
	displayNoticeOnModal();
});
</script>
<body>
    <div class="navbar">
        <%@include file="nav-bar.jsp"%>
    </div>
    <button onclick="displayModalWindow(true, 0);" class="add-button">Add</button>
    <%@include file="notification.jsp"%>
    <table>
        <tr>
            <th>User Name</th>
            <th>Resource Name</th>
            <th>Start Date</th>
            <th>Start Time</th>
            <th>End Date</th>
            <th>End Time</th>
            <c:if test="${sessionScope.loggedInUser.admin}">
                <th>Action</th>
            </c:if>
        </tr>
        <c:forEach items="${reservations}" var="reservationRecord">
            <tr>
                <td><input type="hidden" value="${reservationRecord.userId}"> <c:out
                        value="${reservationRecord.userName}" /></td>
                <td><input type="hidden" value="${reservationRecord.resourceId}"> <c:out
                        value="${reservationRecord.resourceName}" /></td>
                <td><c:out value="${reservationRecord.startDate}" /></td>
                <td><c:out value="${reservationRecord.startTime}" /></td>
                <td><c:out value="${reservationRecord.endDate}" /></td>
                <td><c:out value="${reservationRecord.endTime}" /></td>
                <td><c:choose>
                        <c:when test="${sessionScope.loggedInUser.admin}">
                            <input type="button" onclick="getId('${reservationRecord.reservationId}')" value="Edit">
                            <input type="button" onclick="getIdForDelete('${reservationRecord.reservationId}')"
                                value="Delete">
                        </c:when>
                    </c:choose></td>
            </tr>
        </c:forEach>
    </table>
    <br />
    <div id="edit-field" class="edit-field">
        <form action="ReservationServlet" method="post" class="edit-form" onsubmit="return validateEditForm(this)"
            name="edit_Reservation" id="edit-form">
            <table>
                <tr>
                    <td colspan="2">
                        <div class="cancel-container">
                            <span onclick="document.getElementById('edit-field').style.display='none'" class="close">
                                &times;</span>
                        </div> 
                        <%@include file="display-error.jsp"%> 
                        <input type="hidden" name="edit_reservation" value="edit_reservation"> 
                        <input type="hidden" name="reservation_id" value="${reservation.reservationId}" /> 
                        <input type="hidden" value="${reservation.userId}" name="user_id" />
                    </td>
                </tr>
                <tr>
                    <td><label>Resource Name<span class="required">*</span>:: </label></td>
                    <td><select name="resource_name">
                            <c:forEach items="${resources}" var="resource">
                                <option
                                    <c:if test="${reservation.resourceId eq resource.rsResourceId}">
					selected="selected"</c:if>
                                    value="${resource.rsResourceId}">${resource.resourceName}</option>
                            </c:forEach>
                    </select></td>
                </tr>
                <tr>
                    <td>
                        <label>Start Date<span class="required">*</span>:
                    </label></td>
                    <td><input type="date" name="start_date" placeholder="Enter Date : DD-MM-YYYY"
                        value="${reservation.startDate}" maxlength="10"></td>
                </tr>
                <tr>
                    <td><label>Start Time<span class="required">*</span>:
                    </label></td>
                    <td><input type="time" name="start_time" placeholder="Enter Time : HH:MM 24HR" id="time-start"
                        value="${reservation.startTime }" maxlength="5"></td>
                </tr>
                <tr>
                    <td><label>End Date<span class="required">*</span>:
                    </label></td>
                    <td><input type="date" name="end_date" placeholder="Enter Date : DD-MM-YYYY"
                        value="${reservation.endDate }" maxlength="10"></td>
                </tr>
                <tr>
                    <td><label>End Time<span class="required">*</span>:
                    </label></td>
                    <td><input type="time" name="end_time" placeholder="Enter Time : HH:MM 24HR" id="time-end"
                        value="${reservation.endTime }" maxlength="5"></td>
                </tr>
                <tr>
                    <td><label>All Day</label></td>
                    <td><input type="checkbox" name="is_all_day" onclick="enableDisableTextBox(this)"></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" value="Save"> <input type="button"
                        onclick="document.getElementById('edit-field').style.display='none'"
                        class="add-reservation-button" value="Cancel"></td>
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