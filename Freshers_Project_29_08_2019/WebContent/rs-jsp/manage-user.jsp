<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.* "%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.altres.rs.model.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User</title>

<link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/common.css" />
<link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/manage-user.css" />
<script type="text/javascript" src="/ResourceScheduler/js/manage_user.js"></script>
</head>
<body onload="validateErrorForm()">

	<div class="navbar">
		<%@include file="nav-bar.jsp"%>
	</div>

	<button
		onclick="document.getElementById('add-field').style.display='block'"
		class="add-button">Add</button>
		
		
	<%@include file="notification.jsp"%>

	<div>
		<div class="left">
			<table>
				<tr>
					<th>Name</th>
					<th>Email</th>
					<th>Status</th>
					<th>Action</th>
				</tr>
				<c:forEach items="${user}" var="users">
					<tr>

						<td><c:out value="${users.name}" /></td>
						<td><c:out value="${users.email_address}" /></td>
						<td>
						  <c:choose>
								<c:when test="${users.isEnabled()}">Active</c:when>
								<c:otherwise>Inactive</c:otherwise>
						  </c:choose>
						 </td>
						<td><input type="button"
							onclick="getEmail('${users.email_address}')" value="Edit"></td>
					</tr>
				</c:forEach>
			</table>
		</div>




		<c:if test="${not empty singleUser }">

			<div id="edit-field" class="edit-field">
				<form action="UserServlet" method="post" class="edit-form" onsubmit="return validateForm()">
				
				<table>
				
					<tr>
						<td colspan="2">
							<input type="hidden" name="edit" value="edit_user" />

								<div class="cancel-container">
									<span onclick="document.getElementById('edit-field').style.display='none'" 
									class="close"> &times;</span><br />
								</div>

								<div class="error-user">
									<b id="error-user"></b>
									<b>${error_message }</b>
								</div>
							</td>
					</tr>
					
					<tr>
						<td><label>Name<span class="required">*</span>: </label></td>
						<td>
							<input type="text" name="name" placeholder="Name" value="${singleUser.name}"
								onblur="validateFirstName(this)" maxlength="50"><br /> 
								<b id="error-firstname"></b>
							</td>
					</tr>
					
					<tr>
						<td><label>Email Address<span class="required">*</span>: </label><br/></td>
						<td><input type="text" name="email" placeholder="Email Address" 
							value="${singleUser.email_address}"
								readonly="readonly" maxlength="50"><br /></td>
					</tr>
					
					<tr>
						<td><label>Enable</label></td>
						<td>
						  <input type="checkbox" name="isenabled"
						  <c:if test="${singleUser.isEnabled()}">checked="checked"</c:if>>
						</td>
					</tr>
					
					<tr>
						<td><label>Is Admin</label></td>
						<td>
						  <input type="checkbox" name="isadmin"
						  <c:if test="${singleUser.isAdmin()}">checked="checked"</c:if>>
						</td>
					</tr>
					
					<tr>
						<td colspan="2">
							<input type="submit" value="Save"> 
							<input type="button"
							onclick="document.getElementById('edit-field').style.display='none'" value="Cancel">

							</td>
					</tr>
					
				</table>

					
					

				</form>
			</div>

		</c:if>



		<div id="add-field" class="add-field">
			<form action="UserServlet" method="post" class="edit-form" onsubmit="return validateAddForm(this)" 
			name="add_user">

				<table>
					<tr>
						<td colspan="2">
						
							<div class="cancel-container">
								<span onclick="document.getElementById('add-field').style.display='none'" class="close">
								&times;</span> <br />
							</div>
							<div class="error-user">
								<b id="error-user"></b>
								<b>${error_message }</b>
							</div>
							
						</td>
					</tr>
					
					<tr>
						<td><label>Name<span class="required">*</span>: </label><br/></td>
						<td><input type="text" name="name" placeholder="Name" onblur="validateFirstName(this)"
							maxlength="50"><br /> <b id="error-firstname"></b>
						</td>
					</tr>
					
					<tr>
						<td><label>Email Address<span class="required">*</span>: </label><br/></td>
						<td><input type="text" name="email" placeholder="Email Address" onblur="validateEmail(this)" 
							maxlength="50"><br />
							<b id="error-mail"></b>
						</td>
					</tr>
					
					<tr>
						<td><label>Enable</label></td>
						<td>
							<input type="checkbox" name="isenabled" checked="checked">
						</td>
					</tr>
					
					<tr>
						<td><label>Is Admin</label></td>
						<td>
							<input type="checkbox" name="isadmin">
						</td>
					</tr>
					
					<tr>
						<td colspan="2"><input type="submit" value="Add">
							<input type="button" onclick="document.getElementById('add-field').style.display='none'" 
							value="Cancel">
						</td>
					</tr>
					
				</table>

			</form>
		</div>
	</div>


<c:if test="${user.size() gt 10 }">
	<button
		onclick="document.getElementById('add-user').style.display='block'"
		class="add-user-button">Add</button>
</c:if>

</body>
</html>