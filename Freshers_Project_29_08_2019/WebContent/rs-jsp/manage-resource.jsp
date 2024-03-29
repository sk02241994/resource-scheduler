<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.* "%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.altres.rs.model.Resource"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Resource</title>
<link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/common.css" />
<link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/manage-resource.css" />
<script type="text/javascript" src="/ResourceScheduler/js/manage_resource.js"></script>
<script type="text/javascript" src="/ResourceScheduler/js/display-error.js"></script>
<script type="text/javascript" src="/ResourceScheduler/js/common.js"></script>
</head>
<script> 
executeEvent(window, 'load', function(){
	displayModalWindow(${not empty user.rsUserId}, ${user.rsUserId});
	addNoticeFormModal(${error_message_modal});
	displayNoticeOnModal();
});
</script>
<body onload="displayModalWindow(${not empty form.rsResourceId}, ${form.rsResourceId});">
    <div class="navbar">
        <%@include file="nav-bar.jsp"%>
    </div>
    <button onclick="displayModalWindow(true, 0);" class="add-button">Add</button>
    <c:if test="${not empty resourcesDeleteError }">
        <div id="error-delete">${resourcesDeleteError}
            <span class="closeButton" onclick="this.parentElement.style.display='none';">&times;</span>
        </div>
    </c:if>
    <%@include file="notification.jsp"%>
    <div>
        <div class="left">
            <table>
                <tr>
                    <th></th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                <c:forEach items="${resources}" var="resource">
                    <tr>
                        <td><input type="hidden" value="${resource.rsResourceId}"></td>
                        <td><c:out value="${resource.resourceName}" /></td>
                        <td><c:out value="${resource.resourceDescription}" /></td>
                        <td><c:choose>
                                <c:when test="${resource.isEnabled()}">Active</c:when>
                                <c:otherwise>Inactive</c:otherwise>
                            </c:choose></td>
                        <td><input type="button" onclick="getResource('${resource.rsResourceId}');" value="Edit">
                            <input type="button" onclick="deleteResource('${resource.rsResourceId}');" value="Delete"></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div id="edit-field" class="edit-field">
            <form action="ResourceServlet" method="post" class="edit-form" name="resource_form"
                onsubmit="return validateResource(this)" id="edit-form">
                <table>
                    <tr>
                        <td colspan="2">
                            <div class="cancel-container">
                                <span onclick="document.getElementById('edit-field').style.display='none'" class="close">
                                    &times; </span>
                            </div> <%@include file="display-error.jsp"%> <input type="hidden"
                            name="resource_id" value="${form.rsResourceId}">
                        </td>
                    </tr>
                    <tr>
                        <td><label>Resource Name<span class="required">*</span>:
                        </label></td>
                        <td><input type="text" name="resource_name" value="${form.resourceName}" maxlength="50"></td>
                    </tr>
                    <tr>
                        <td><label>Resource Description: </label></td>
                        <td><textarea row="5" cols="40%" name="description" maxlength="255" wrap="hard">${form.resourceDescription}</textarea>
                        </td>
                    </tr>
                    <tr>
                        <td><label>Enable</label></td>
                        <td><input type="checkbox" name="isenabled"
                            <c:if test="${form.isEnabled()}">checked="checked"</c:if>></td>
                    </tr>
                    <tr>
                        <td><label>Time Limit</label></td>
                        <td><fmt:parseNumber var="hours" integerOnly="true" type="number"
                                value="${form.timeLimit div 60}" /> <input type="text" name="timeLimitHours"
                            placeholder="Hours" value="${hours eq 0 ? '' : hours}" onkeyup="this.value=onlyNumber(this)" />
                            <c:set value="${form.timeLimit mod 60}" var="minutes" /> <input type="text"
                            name="timeLimitMinutes" placeholder="Minutes" value="${minutes eq 0 ? '' : minutes}"
                            onkeyup="this.value=onlyNumber(this)" /></td>
                    </tr>
                    <tr>
                        <td><label>Is Allowed Multiple Reservations</label></td>
                        <td><input type="checkbox" name="isAllowedMultiple"
                            <c:if test="${form.isAllowedMultiple()}">checked="checked"</c:if>></td>
                    </tr>
                    <tr>
                        <td><label>Max User Bookings Allowed</label></td>
                        <td><input type="text" name="max_user_booking" value="${form.maxUserBooking}" onkeyup="this.value=onlyNumber(this)"></td>
                    </tr>
                    <tr>
                        <td><label>Is Allow Employees On Probation</label></td>
                        <td><input type="checkbox" name="isAllowEmpOnProbation"
                            <c:if test="${form.isPermanentEmployee()}">checked="checked"</c:if>></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="Save" class="button"> 
                            <input type="button" class="button" onclick="document.getElementById('edit-field').style.display='none'" value="Cancel">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</body>
</html>
