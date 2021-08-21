
<c:if test="${not empty success_message }">
	<c:forEach items="${success_message}" var="successMessage">
		<div class="alert alert-success alert-dismissible fade show text-center" role="alert">
            ${successMessage}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
		</div>
	</c:forEach>
</c:if>

<c:if test="${not empty error_message }">
	<c:forEach items="${error_message}" var="errorMessage">
		<div class="alert alert-danger alert-dismissible fade show text-center" role="alert">
            ${errorMessage}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
		</div>
	</c:forEach>
</c:if>
