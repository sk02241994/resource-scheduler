<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Resources</title>
        <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
        <meta http-equiv="Pragma" content="no-cache" />
        <meta http-equiv="Expires" content="0" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/common.css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/manage_resource.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/display-error.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/common.js"></script>
        <script>
            executeEvent(window, 'load', function(){
                addNoticeFormModal(${error_message_modal});
                displayNoticeOnModal();
                displayData(${form});
            });
        </script>
    </head>
    <body>
        <%@include file="nav-bar.jsp"%>
        <div class="container">
            <div class="col-md-12 text-center gap-3 d-grid mt-3 mb-3">
                <input type="button" data-toggle="modal" data-target="#edit-field" onclick="clearModal()" class="btn btn-primary btn-sm" value="Add"/>
            </div>
            <%@include file="notification.jsp"%>
            <table class="table">
                <thead>
                    <tr>
                        <th class="text-center" scope="col">Name</th>
                        <th class="text-center" scope="col">Description</th>
                        <th class="text-center" scope="col">Status</th>
                        <th class="text-center" scope="col">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${resources}" var="resource">
                        <tr>
                            <th scope="row" class="text-center align-middle"><c:out value="${resource.resourceName}" /></th>
                            <td class="text-center align-middle"><c:out value="${resource.resourceDescription}" /></td>
                            <td class="text-center align-middle"><c:out value="${resource.isEnabled() ? 'Active' : 'Inactive'}" /></td>
                            <td class="text-center align-middle">
                                    <input data-toggle="modal" data-target="#edit-field" class="btn btn-primary btn-sm" type="button" onclick="getResource('${resource.rsResourceId}');" value="Edit">
                                    <input data-toggle="modal" data-target="#edit-field" class="btn btn-primary btn-sm" type="button" onclick="deleteResource('${resource.rsResourceId}');" value="Delete">
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="modal fade" id="edit-field" tabindex="-1" role="dialog" aria-labelledby="formTitle" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="formTitle">Edit Resources</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <%@include file="display-error.jsp"%>
                        <div class="modal-body">
                            <form action="ResourceServlet" method="post" id="edit-form">
                                    <input type="hidden" id="resourceId" name="resource_id" id="resourceId"/>

                                    <div class="form-group">
                                        <lebel class="control-label" for="name">Resource Name<span class="required">*</span>:</lebel>
                                        <div>
                                            <input type="text" class="form-control input-lg" id="resourceName" name="resource_name" maxlength="50">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="description" class="control-label">Description:</label>
                                        <div>
                                            <textarea name="description" class="form-control input-lg" id="description" rows="3"></textarea>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label">Time Limit:</label>
                                        <div class="row">
                                            <div class="col-md-5">
                                                <input type="text" class="form-control" id="timeLimitHours" name="timeLimitHours" placeholder="Hours" onkeyup="this.value=onlyNumber(this);"/>
                                            </div>
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="basic-addon1">-</span>
                                            </div>
                                            <div class="col-md-5">
                                                <input type="text" class="form-control" id="timeLimitMinutes" name="timeLimitMinutes" placeholder="Minutes" onkeyup="this.value=onlyNumber(this);"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <lebel class="control-label" for="name">Max User Bookings Allowed:</lebel>
                                        <div>
                                            <input type="text" class="form-control input-lg" id="maxUserBooking" name="max_user_booking" maxlength="50">
                                        </div>
                                    </div>

                                    <div class="form-group form-check form-check-inline">
                                        <input type="checkbox" id="isenabled" name="isenabled" class="form-check-input" /> 
                                        <label class="form-check-label">Enable</label>
                                    </div>

                                    <div class="form-group form-check form-check-inline">
                                        <input type="checkbox" id="isAllowedMultiple" name="isAllowedMultiple" class="form-check-input" /> 
                                        <label class="form-check-label">Is Allowed Multiple Reservations</label>
                                    </div>

                                    <div class="form-group form-check form-check-inline">
                                        <input type="checkbox" id="isAllowEmpOnProbation" name="isAllowEmpOnProbation" class="form-check-input" /> 
                                        <label class="form-check-label">Is Allow Employees On Probation</label>
                                    </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary" onclick="return validateResource();">Save changes</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>