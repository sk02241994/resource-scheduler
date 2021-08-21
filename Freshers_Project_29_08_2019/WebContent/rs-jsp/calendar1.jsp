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
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css">
        <link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/calendar.css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/manage_resource.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/display-error.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/common.js"></script>
        <script>
            var schedule= ${schedule};
            var userId = ${sessionScope.loggedInUser.rsUserId};
            var isAdmin = ${sessionScope.loggedInUser.admin};

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
            <div class="row justify-content-center">
                <h3 id="month-and-year"></h3>
            </div>
            <%@include file="notification.jsp"%>
            <div class="calendar-navigation">
                <form class="form-inline justify-content-center" >
                    <div class="form-group form-inline align-middle">
                            <div class="col-form-label">Jump To:</div>
                            <div class="col-sm-2">
                                <select name="month" id="month" onchange="jump();" class="form-control">
                                    <option value=0>January</option>
                                    <option value=1>February</option>
                                    <option value=2>March</option>
                                    <option value=3>April</option>
                                    <option value=4>May</option>
                                    <option value=5>June</option>
                                    <option value=6>July</option>
                                    <option value=7>August</option>
                                    <option value=8>September</option>
                                    <option value=9>October</option>
                                    <option value=10>November</option>
                                    <option value=11>December</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group form-inline align-middle">
                            <select id="year" class="form-control" onchange="jump()"></select>
                        </div>
                </form>
            </div>
            
            <div class="row mt-3">
                <div class="col-4 text-left">
                    <div class="previous">
                        <button type="button" class="btn btn-secondary" onclick="previous()">Previous</button>
                    </div>
                </div>
                <div class="col-4">
                        <form class="form-inline justify-content-center" >
                            <div class="form-group form-inline align-middle">
                                <div class="col-form-label">Jump To:</div>
                                <div class="col-sm-2">
                                    <select name="resource_filter" class="form-control" onchange="filterByResource(this.value);">
                                        <option value="">All</option>
                                        <c:forEach items="${resources}" var="resource">
                                            <option <c:if test="${reservation.resourceId eq resource.rsResourceId}">selected="selected"</c:if>
                                                    value="${resource.rsResourceId}">${resource.resourceName}</option>
                                            </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </form>
                </div>
                <div class="col-4 text-right">
                    <div class="next">
                        <button type="button" class="btn btn-secondary" onclick="next()">Next</button>
                    </div>
                </div>
            </div>

            <div class="calendar-table mt-5">
                <table class="table" style="table-layout: fixed;">
                    <thead>
                        <tr>
                            <th scope="col">Sunday</th>
                            <th scope="col">Monday</th>
                            <th scope="col">Tuesday</th>
                            <th scope="col">Wednesday</th>
                            <th scope="col">Thursday</th>
                            <th scope="col">Friday</th>
                            <th scope="col">Saturday</th>
                        </tr>
                    </thead>
                    <tbody id="calendar-body">
                        
                    </tbody>
                </table>
            </div>
        </div>

        <div id="display-all-day-data" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="displayRecordsTitle" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="displayRecordsTitle"></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>   
                    </div>
                    <div class="modal-body" id="displayRecordBody">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <div id="edit-field" class="edit-field"></div>
    </body>
    <script type="text/javascript" src="/ResourceScheduler/js/get-data-for-calendar.js"></script>
    <script type="text/javascript" src="/ResourceScheduler/js/calendar.js"></script>
</html>