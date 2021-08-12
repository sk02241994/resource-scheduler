
<c:if test="${not empty success_message }">
	<c:forEach items="${success_message}" var="successMessage">
		<div id="success-notification" class="success-notification">${successMessage}
			<span class="closeButton" onclick="this.parentElement.style.display='none';">&times;</span>
		</div>
	</c:forEach>
</c:if>

<c:if test="${not empty error_message }">
	<c:forEach items="${error_message}" var="errorMessage">
		<div id="error-notification" class="error-notification">${errorMessage}
			<span class="closeButton" onclick="this.parentElement.style.display='none';">&times;</span>
		</div>
	</c:forEach>
</c:if>