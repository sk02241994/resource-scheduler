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

<link rel="stylesheet" type="text/css"
	href="/ResourceScheduler/css/manage-user.css" />
<script type="text/javascript" src="/ResourceScheduler/js/manage_user.js"></script>
</head>
<body onload="validateErrorForm()">

	<div class="navbar">
		<%@include file="nav-bar.jsp"%>
	</div>

	<button
		onclick="document.getElementById('add-user').style.display='block'"
		class="add-user-button">Add</button>
		
		
	<c:if test="${not empty success_message }">  
  	<div id="notification" class="notification">${success_message}
  		<span class="closeButton" onclick="this.parentElement.style.display='none';">&times;</span></div>
  	</c:if>

	<div>
		<div class="left">
			<table>
				<tr>
					<th>Name</th>
					<th>Email</th>
					<th>Designation</th>
					<th>Department</th>
					<th>Status</th>
					<th>Action</th>
				</tr>
				<c:forEach items="${user}" var="users">
					<tr>

						<td><c:out value="${users.firstname} ${users.lastname}" /></td>
						<td><c:out value="${users.email_address}" /></td>
						<td><c:out value="${users.designation}" /></td>
						<td><c:out value="${users.department}" /></td>
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

			<div id="edit-user" class="edit-user">
				<form action="UserServlet" method="post" class="user-form" onsubmit="return validateForm()">
				
				<table>
				
					<tr>
						<td colspan="2">
							<input type="hidden" name="edit" value="edit_user" />

								<div class="cancel-container">
									<span onclick="document.getElementById('edit-user').style.display='none'" 
									class="close"> &times;</span><br />
								</div>

								<div class="error-user">
									<b id="error-user"></b>
									<b>${error_message }</b>
								</div>
							</td>
					</tr>
					
					<tr>
						<td><label>First Name<span class="required">*</span>: </label></td>
						<td>
							<input type="text" name="first_name" placeholder="First Name" value="${singleUser.firstname}"
								onblur="validateFirstName(this)" maxlength="50"><br /> 
								<b id="error-firstname"></b>
							</td>
					</tr>
					
					<tr>
						<td><label>Last Name<span class="required">*</span>: </label><br/></td>
						<td>
							<input type="text" name="last_name" placeholder="Last Name" value="${singleUser.lastname}"
								onblur="validateLastName(this)" maxlength="50"><br /> <b id="error-lastname"></b>
							</td>
					</tr>
					
					<tr>
						<td><label>Email Address<span class="required">*</span>: </label><br/></td>
						<td><input type="text" name="email" placeholder="Email Address" 
							value="${singleUser.email_address}"
								readonly="readonly" maxlength="50"><br /></td>
					</tr>
					
					<tr>
						<td><label>Designation<span class="required">*</span>: </label><br/></td>
						<td>
							<select name="designation">

								<option disabled="disabled">Designation</option>

								<option <c:if test="${singleUser.department eq 'Manager'}">selected="selected"</c:if>>
									Manager</option>

								<option <c:if test="${singleUser.department eq 'HR'}">selected="selected"</c:if>>
								HR</option>

								<option <c:if test="${singleUser.department eq 'SW Dev'}">selected="selected"</c:if>>
								SW Dev</option>

							</select>

							</td>
					</tr>
					
					<tr>
						<td><label>Address: </label><br/></td>
						<td>
							<textarea rows="5" cols="40%" name="address" placeholder="Address" maxlength="255" 
							wrap="hard">${singleUser.address}</textarea>
						</td>
					</tr>
					
					<tr>
						<td><label>Department<span class="required">*</span>: </label><br/></td>
						<td>
						<select name="department">

								<option disabled="disabled">Department</option>

								<option <c:if test="${singleUser.department eq 'IT'}">selected="selected"</c:if>>
									IT</option>

								<option <c:if test="${singleUser.department eq 'Database'}">selected="selected"</c:if>>
									Database</option>

								<option <c:if test="${singleUser.department eq 'Technical'}">selected="selected"</c:if>>
									Technical</option>

						</select>
						</td>					
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
							onclick="document.getElementById('edit-user').style.display='none'" value="Cancel">

							</td>
					</tr>
					
				</table>

					
					

				</form>
			</div>

		</c:if>



		<div id="add-user" class="add-user">
			<form action="UserServlet" method="post" class="user-form" onsubmit="return validateAddForm(this)" 
			name="add_user">

				<table>
					<tr>
						<td colspan="2">
						
							<div class="cancel-container">
								<span onclick="document.getElementById('add-user').style.display='none'" class="close">
								&times;</span> <br />
							</div>
							<div class="error-user">
								<b id="error-user"></b>
								<b>${error_message }</b>
							</div>
							
						</td>
					</tr>
					
					<tr>
						<td><label>First Name<span class="required">*</span>: </label><br/></td>
						<td><input type="text" name="first_name" placeholder="First Name" onblur="validateFirstName(this)"
							maxlength="50"><br /> <b id="error-firstname"></b>
						</td>
					</tr>
					
					<tr>
						<td><label>Last Name<span class="required">*</span>: </label><br/></td>
						<td><input type="text" name="last_name" placeholder="Last Name" onblur="validateLastName(this)"
							maxlength="50"><br /><b id="error-lastname"></b>
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
						<td><label>Designation<span class="required">*</span>: </label><br/></td>
						<td><select name="designation">

								<option disabled="disabled" selected="selected">Designation</option>
								<option>Manager</option>
								<option>HR</option>
								<option>SW Dev</option>

						</select></td>
					</tr>
					
					<tr>
						<td><label>Address: </label><br/></td>
						<td><textarea rows="5" cols="40%" name="address" placeholder="Address" 
							maxlength="100" wrap="hard"></textarea>
						</td>
					</tr>
					
					<tr>
						<td><label>Department<span class="required">*</span>: </label><br/></td>
						<td>
							<select name="department">

								<option disabled="disabled" selected="selected" value="">Department</option>
								<option>IT</option>
								<option>Database</option>
								<option>Technical</option>

							</select>
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
							<input type="button" onclick="document.getElementById('add-user').style.display='none'" 
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