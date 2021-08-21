<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Reservation</title>
        <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
        <meta http-equiv="Pragma" content="no-cache" />
        <meta http-equiv="Expires" content="0" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/clockpicker/0.0.7/bootstrap-clockpicker.css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/clockpicker/0.0.7/jquery-clockpicker.min.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/manage_reservation.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/display-error.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/common.js"></script>
        <script>
        executeEvent(window, 'load', function(){
            addNoticeFormModal(${error_message_modal});
            displayNoticeOnModal();
            displayData(${reservation});
        });
        </script>
    </head>
    <body>
        <%@include file="nav-bar.jsp"%>
        <div class="container">
            <div class="col-md-12 text-center gap-3 d-grid mt-3 mb-3">
                <input type="button" data-toggle="modal" data-target="#edit-field" onclick="clearModal();setDate();" class="btn btn-primary btn-sm" value="Add"/>
            </div>
            <%@include file="notification.jsp"%>

            <table class="table">
                <thead>
                    <tr>
                        <th class="text-center" scope="col">User Name</th>
                        <th class="text-center" scope="col">Resource Name</th>
                        <th class="text-center" scope="col">Start Date</th>
                        <th class="text-center" scope="col">Start Time</th>
                        <th class="text-center" scope="col">End Date</th>
                        <th class="text-center" scope="col">End Time</th>
                        <c:if test="${sessionScope.loggedInUser.admin}">
                            <th class="text-center" scope="col">Action</th>
                        </c:if>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${reservations}" var="reservationRecord">
                        <tr>
                            <th scope="row" class="text-center align-middle"><c:out value="${reservationRecord.userName}" /></th>
                            <td class="text-center align-middle"><c:out value="${reservationRecord.resourceName}" /></td>
                            <td class="text-center align-middle"><c:out value="${reservationRecord.startDate}" /></td>
                            <td class="text-center align-middle"><c:out value="${reservationRecord.startTime}" /></td>
                            <td class="text-center align-middle"><c:out value="${reservationRecord.endDate}" /></td>
                            <td class="text-center align-middle"><c:out value="${reservationRecord.endTime}" /></td>
                            <c:if test="${sessionScope.loggedInUser.admin}">
                                <td class="text-center align-middle">
                                    <input data-toggle="modal" data-target="#edit-field" class="btn btn-primary btn-sm" type="button" onclick="getId('${reservationRecord.reservationId}');" value="Edit">
                                    <input data-toggle="modal" data-target="#edit-field" class="btn btn-primary btn-sm" type="button" onclick="getIdForDelete('${reservationRecord.reservationId}');" value="Delete">
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="modal fade" id="edit-field" tabindex="-1" role="dialog" aria-labelledby="formTitle" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="formTitle">Edit Reservation</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <%@include file="display-error.jsp"%>
                        <div class="modal-body">
                            <form action="ReservationServlet" method="post" id="edit-form">
                                <input type="hidden" name="edit_reservation" value="edit_reservation">
                                <input type="hidden" name="reservation_id" id="reservationId" /> 
                                <input type="hidden" id="userId" name="user_id" />

                                <div class="form-group">
                                    <lebel class="control-label" for="resourceName">Resource Name*:</lebel>
                                    <select name="resource_name" class="form-control" id="resourceName">
                                        <c:forEach items="${resources}" var="resource">
                                            <option value="${resource.rsResourceId}">${resource.resourceName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label class="control-label" for="name">Start Date:</label>
                                    <input name="start_date" placeholder="mm/dd/yyyy" type="text" id="startDate" class="form-control datepicker">
                                </div>
                                <div class="form-group clockpicker">
                                    <label class="control-label" for="name">Start Time:</label>
                                    <input type="text" name="start_time" id="startTime" class="form-control">
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>

                                <div class="form-group">
                                    <label class="control-label" for="name">End Date:</label>
                                    <input name="end_date" placeholder="mm/dd/yyyy" type="text" id="endDate" class="form-control datepicker">
                                </div>
                                <div class="form-group clockpicker">
                                    <label class="control-label" for="name">End Time:</label>
                                    <input type="text" name="end_time" id="endTime" class="form-control">
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>

                                <div class="form-group form-check form-check-inline">
                                    <input type="checkbox" id="isAllDay" name="is_all_day" class="form-check-input" onclick="enableDisableTextBox(this)"/> 
                                    <label class="form-check-label">All Day</label>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary" onclick="return validateEditForm();">Save changes</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
        $(function () {
            $('.datepicker').datepicker({
                daysOfWeekDisabled: [0, 6],
                today: 'Today',
            });
            $('.datepicker').datepicker("setDate", new Date());
            var input = $('.clockpicker').clockpicker({
                autoclose: true,
                placement: 'top',
                beforeShow: function(e){
                    if(readOnlyClock) {
                        e.stopPropagation();
                    }
                }
            });
         });
        </script>
    </body>
</html>