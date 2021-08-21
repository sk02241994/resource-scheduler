<nav class="nav navbar-expand-lg navbar-dark bg-dark">
    <ul class="nav navbar-nav">
         <c:choose>
            <c:when test="${sessionScope.loggedInUser.admin}">
                <li class="nav-item"><a class="nav-link" href="ReservationServlet">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="UserServlet">Manage User</a></li>
                <li class="nav-item"><a class="nav-link" href="ResourceServlet">Manage Resource</a></li>
                <li class="nav-item"><a class="nav-link" href="ReservationServlet?form_type=calendar">Calendar</a></li>
            </c:when>
            <c:otherwise>
                <li class="nav-item"><a class="nav-link" href="ReservationServlet">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="ReservationServlet?form_type=calendar">Calendar</a></li>
            </c:otherwise>
        </c:choose>
    </ul>
    <ul class="nav navbar-nav ml-auto">
        <li class="nav-item"><a class="nav-link" href="LoginServlet?action=logout">Logout</a></li>
    </ul>
</nav>

