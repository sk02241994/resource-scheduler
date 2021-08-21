<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.altres.utils.Gender" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<!-- <link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/common.css" />
<link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/manage-user.css" /> -->
<script type="text/javascript" src="/ResourceScheduler/js/manage_user.js"></script>
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
<body>
    <div class="container">
    <!-- <div class="navbar"> -->
        <%@include file="nav-bar.jsp"%>
    <!-- </div> -->
    <button onclick="displayModalWindow(true, 0);" class="add-button">Add</button>
    <%@include file="notification.jsp"%>
    <div>
        <div class="left">
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${users}" var="userRecord">
                        <tr>
                            <td><c:out value="${userRecord.name}" /></td>
                            <td><c:out value="${userRecord.email_address}" /></td>
                            <td><c:out value="${userRecord.isEnabled() ? 'Active' : 'Inactive'}" /></td>
                            <td><input type="button" onclick="getUser('${userRecord.rsUserId}');" value="Edit"></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div id="edit-field" class="edit-field">
            <form action="UserServlet" method="post" class="edit-form" onsubmit="return validateForm(this)"
                id="edit-form">
                <table>
                    <tr>
                        <td colspan="2"><input type="hidden" name="userId" value="${user.rsUserId}" />
                            <div class="cancel-container">
                                <span onclick="document.getElementById('edit-field').style.display='none'" class="close">
                                    &times;</span><br />
                            </div>
                            <%@include file="display-error.jsp"%>
                    </tr>
                    <tr>
                        <td><label>Name<span class="required">*</span>:</label></td>
                        <td><input type="text" name="name" value="${user.name}" maxlength="50"><br /></td>
                    </tr>
                    <tr>
                        <td><label>Email Address<span class="required">*</span>:
                        </label><br /></td>
                        <td><input type="text" name="email" value="${user.email_address}" readonly="readonly"
                            maxlength="50"><br /></td>
                    </tr>
                    <tr>
                        <td><label>Enable</label></td>
                        <td><input type="checkbox" name="isenabled"
                            <c:if test="${user.isEnabled()}">checked="checked"</c:if>></td>
                    </tr>
                    <tr>
                        <td><label>Is Admin</label></td>
                        <td><input type="checkbox" name="isadmin"
                            <c:if test="${user.isAdmin()}">checked="checked"</c:if>></td>
                    </tr>
                    <tr>
                        <td><label>Gender</label></td>
                        <td>
                            <input type="radio" name="gender" value="M" <c:if test="${user.gender eq Gender.M}">checked</c:if>> Male </input>
                            <input type="radio" name="gender" value="F" <c:if test="${user.gender eq Gender.F}">checked</c:if>> Female </input>
                        </td>
                    </tr>
                    <tr>
                        <td><label>Is Permanent Employee</label></td>
                        <td><input type="checkbox" name="isPermanentEmployee"
                            <c:if test="${user.isPermanentEmployee()}">checked="checked"</c:if>></td>
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
    </div>
</body>
</html>
