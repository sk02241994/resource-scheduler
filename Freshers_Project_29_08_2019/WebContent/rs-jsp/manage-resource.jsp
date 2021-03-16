<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.* "%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.altres.rs.model.Resource" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Resource</title>
<link rel="stylesheet" type="text/css"
	href="/ResourceScheduler/css/manage-resource.css" />
<script type="text/javascript" src="/ResourceScheduler/js/manage_resource.js"></script>
</head>
<body onload="formValidationAdding()">

  <div class="navbar">
    <%@include file="nav-bar.jsp"%>
  </div>

  <button onclick="document.getElementById('add-resource').style.display='block'"
  class="add-resource-button">Add</button>
	
	<c:if test="${not empty resourcesDeleteError }">  
		<div id="error-delete">${resourcesDeleteError}</div>
		<span class="closeButton" onclick="this.parentElement.style.display='none';">&times;</span></div>
	</c:if>
	
	<c:if test="${not empty success_message }">  
  	<div id="notification" class="notification">${success_message}
  		<span class="closeButton" onclick="this.parentElement.style.display='none';">&times;</span></div>
  	</c:if>
	
  <div>
    
    <div class="left">
      <table>
      
        <tr>
          <th> </th>
          <th>Name</th>
          <th>Description</th>
          <th>Status</th>
          <th>Action</th>
        </tr>
        
        <c:forEach items="${resources}" var="resource">
          
          <tr>
            <td><input type="hidden" value="${resource.rsResourceId}"></td>
            
            <td><c:out value="${resource.resourceName}"/></td>
            
            <td><c:out value="${resource.resourceDescription}"/></td>
            
            <td><c:choose>
				<c:when test="${resource.isEnabled()}">Active</c:when>
				<c:otherwise>Inactive</c:otherwise>
				</c:choose>
			</td>
			
			<td>
			<input type="button" onclick="getId('${resource.rsResourceId}')" value="Edit">
				
			<input type="button" onclick="getIdForDelete('${resource.rsResourceId}')" value="Delete">
			</td>
          
          </tr>
          
        </c:forEach>
        
      </table>
    </div>
    
    
    <c:if test="${not empty singleResource}">
      <div id="edit-resource" class="edit-resource">
      
        <form action="ResourceServlet" method="post" class="resource-form" name="resource_form"
        onsubmit="return validateResource(this)">
        
        <table>
        	
        	<tr>
        		<td colspan="2">
        			<input type="hidden" name="edit_resource" value="edit_resource" />

					<div class="cancel-container">
					<span onclick="document.getElementById('edit-resource').style.display='none'" class="close">
					&times; </span>
					</div>

					<div id="error-resource-validation">
					<b id="error-resource"></b> <b>${error_message}</b>
					</div>
					
					<div class="error-resource"><b id="error-resource"></b></div>
          			<input type="hidden" name="resource_id" value="${singleResource.rsResourceId}">
				</td>
        	</tr>
        	
        	<tr>
        		<td><label>Resource Name<span class="required">*</span>: </label></td>
        		<td>
        			<input type="text" name="resource_name" value="${singleResource.resourceName}" maxlength="50">
        		</td>
        	</tr>
        	
        	<tr>
        		<td><label>Resource Description: </label></td>
        		<td>
        			<textarea  row="5" cols="40%" name="description" placeholder="Description"
        			maxlength="255" wrap="hard">${singleResource.resourceDescription}</textarea>
        		</td>
        	</tr>
        	
        	<tr>
        		<td><label>Enable</label></td>
        		<td>
        			<input type="checkbox" name="isenabled"
		    		<c:if test="${singleResource.isEnabled()}">checked="checked"</c:if>>
        		</td>
        	</tr>
			
			<tr>
				<td><label>Time Limit</label></td>
				<fmt:parseNumber var="hours" integerOnly="true" type="number" value="${singleResource.timeLimit div 60}" />
				<td><input type="text" name="timeLimitHours" placeholder="Hours" value="${hours eq 0 ? '' : hours}"/>
				<c:set value="${singleResource.timeLimit mod 60}" var="minutes" />
				<input type="text" name="timeLimitMinutes" placeholder="Minutes" 
				value="${minutes eq 0 ? '' : minutes}"/>
				</td>
			</tr>
			
			<tr>
				<td colspan="2">
					<input type="submit" value="Save"> 
					<input type="button"
					onclick="document.getElementById('edit-resource').style.display='none'" value="Cancel">
				</td>
			</tr>
			        	
        </table>

          
          
        </form>
      
      </div>
    </c:if>
    
    
    <div id="add-resource" class="add-resource">
      
      <form action="ResourceServlet" method="post" class="resource-form" name="resource_form" 
      onsubmit="return validateResource(this)">
        
        <table>
        	<tr>
        		<td colspan="2">
        			<div class="cancel-container">
					<span onclick="document.getElementById('add-resource').style.display='none'" class="close">&times;
					</span><br />
					</div>
					<div id="error-resource-validation">
					<b id="error-resource"></b> <b>${error_message}</b>
					</div>
				</td>
        	</tr>
        	
        	<tr>
        		<td><label>Resource Name<span class="required">*</span>: </label><br/></td>
        		<td>
        			<input type="text" name="resource_name" placeholder="Resource Name" maxlength="50" 
        			value="${fn:escapeXml(param.resource_name)}"><br />
        		</td>
        	</tr>
        	
        	<tr>
        		<td><label>Resource Description: </label><br/></td>
        		<td>
        			<textarea row="50" cols="40%" name="description" placeholder="Description"
        			maxlength="100" wrap="hard">${fn:escapeXml(param.description)}</textarea>
        		</td>
        	</tr>
        	
			<tr>
				<td><label>Enable</label></td>
				<td><input type="checkbox" name="isenabled" checked="checked">
				</td>
			</tr>        
			
			<tr>
				<td><label>Time Limit</label></td>
				<td><input type="text" name="timeLimitHours" placeholder="Hours"/>
				<input type="text" name="timeLimitMinutes" placeholder="Minutes"/>
				</td>
			</tr>
			
			<tr>
				<td colspan="2">
					<input type="submit" value="Add">
        			<input type="button" onclick="document.getElementById('add-resource').style.display='none'"
        			value="Cancel">
				</td>
			</tr>
        </table>

      </form>
    </div>
  
  </div>

</body>
</html>