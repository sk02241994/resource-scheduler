<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.altres.utils.Gender" %>
<!DOCTYPE html>
<html>
    <head> 
        <title>User</title> 
        <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
        <meta http-equiv="Pragma" content="no-cache" />
        <meta http-equiv="Expires" content="0" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/common.css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/manage_user.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/display-error.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/common.js"></script>
        <script>
            executeEvent(window, 'load', function(){
                addNoticeFormModal(${error_message_modal});
                displayNoticeOnModal();
                displayData(${user});
            });
        </script>
    </head>
    <body>
        <%@include file="nav-bar.jsp"%>

        <div class="container">
            <div class="col-md-12 text-center gap-3 d-grid mt-3 mb-3">
                <input type="button" data-toggle="modal" data-target="#edit-field" onclick="clearModal()" class="btn btn-primary btn-sm" value="Add" /%>
            </div>
            <%@include file="notification.jsp"%>
            <table class="table">
                <thead>
                    <tr>
                        <th class="text-center" scope="col">Name</th>
                        <th class="text-center" scope="col">Email</th>
                        <th class="text-center" scope="col">Status</th>
                        <th class="text-center" scope="col">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${users}" var="userRecord">
                        <tr>
                            <th scope="row" class="text-center align-middle"><c:out value="${userRecord.name}" /></th>
                            <td class="text-center align-middle"><c:out value="${userRecord.email_address}" /></td>
                            <td class="text-center align-middle"><c:out value="${userRecord.isEnabled() ? 'Active' : 'Inactive'}" /></td>
                            <td class="text-center align-middle"><input data-toggle="modal" data-target="#edit-field" class="btn btn-primary btn-sm" type="button" onclick="getUser('${userRecord.rsUserId}');" value="Edit"></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="modal fade" id="edit-field" tabindex="-1" role="dialog" aria-labelledby="formTitle" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content"> 
                        <div class="modal-header">
                            <h5 class="modal-title" id="formTitle">Edit User</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <%@include file="display-error.jsp"%>
                        <div class="modal-body"> 

                            <form action="UserServlet" method="post" id="edit-form">
                                <input type="hidden" name="userId" id="userId"/>
                                <div class="form-group">
                                    <label class="control-label" for="name">Name<span class="required">*</span>:</label>
                                    <div>
                                        <input type="text" class="form-control input-lg" id="name" name="name" maxlength="50"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label" for="email">Email Address<span class="required">*</span>:</label>
                                    <div>
                                        <input type="text" class="form-control input-lg" id="email" name="email" maxlength="50"/>
                                    </div>
                                </div>
                                <div class="form-group form-check form-check-inline">
                                    <input type="checkbox" id="isenabled" name="isenabled" class="form-check-input" /> 
                                    <label class="form-check-label">Enable</label>
                                </div>

                                <div class="form-group form-check form-check-inline">
                                    <input type="checkbox" id="isadmin" name="isadmin" class="form-check-input" /> 
                                    <label class="form-check-label">Is Admin</label>
                                </div>

                                <div class="form-group form-check form-check-inline">
                                    <input type="checkbox" id="isPermanentEmployee" name="isPermanentEmployee" class="form-check-input" /> 
                                    <label class="form-check-label">Is Permanent Employee</label>
                                </div>
                                <br/>
                                <div class="form-check form-check-inline">
                                  <input class="form-check-input" type="radio" name="gender" id="gender" value="M">
                                  <label class="form-check-label" for="flexRadioDefault1">
                                    Male
                                  </label>
                                </div>
                                <div class="form-check form-check-inline">
                                  <input class="form-check-input" type="radio" name="gender" id="gender" value="F">
                                  <label class="form-check-label" for="flexRadioDefault2">
                                    Female
                                  </label>
                                </div>

                            </form>

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary" onclick="return validateForm();">Save changes</button>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </body>
</html>
