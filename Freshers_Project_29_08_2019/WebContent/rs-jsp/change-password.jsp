<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Change Password</title>
        <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
        <meta http-equiv="Pragma" content="no-cache" />
        <meta http-equiv="Expires" content="0" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="/ResourceScheduler/css/login.css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/validation.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/display-error.js"></script>
        <script type="text/javascript" src="/ResourceScheduler/js/common.js"></script>
        <script>
        </script>
    </head>
    <body>
        <section class="form my-4 mx-5">
            <div class="container">
                <div class="row no-gutters">
                    <div class="col-lg-5">
                            <img src="https://images.unsplash.com/photo-1587316205943-b15dc52a12e0?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=634&q=80" class="img-fluid" alt="">
                    </div>
                    <div class="col-lg-7 px-5 py-5">
                        <h1 class="font-weight-bold py-3">Change password</h1>
                        <c:choose>
                            <c:when test="${not empty success_message }">
                                <div class="form-row">
                                    <div class="col-lg-7">
                                        <%@include file="notification.jsp"%>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="col-lg-7">
                                        <button type="submit" class="btn1 btn-primary" onclick="goToLogin();">OK</button>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <%@include file="display-error.jsp"%>
                                <form action="ChangePasswordServlet" method="post" onsubmit="return validateChangePassword(this);">
                                    <div class="form-row">
                                        <div class="col-lg-7">
                                            <input type="text" name="uemail_address" value="${sessionScope.loggedInUser.email_address}" class="form-control my-3 p-4" maxlength="50" readonly>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col-lg-7">
                                            <input type="password" name="old_password" class="form-control my-3 p-4" placeholder="Enter Password" maxlength="50" id="">
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col-lg-7">
                                            <input type="password" name="new_password" class="form-control my-3 p-4" placeholder="Confirm Password" maxlength="50" id="">
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="col-lg-7">
                                            <button type="submit" class="btn1 btn-primary">Change Password</button>
                                        </div>
                                    </div>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </section>
    </body>
</html>