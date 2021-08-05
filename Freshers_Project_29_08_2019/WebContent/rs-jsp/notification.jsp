<c:if test="${not empty success_message }">
	<div id="notification" class="notification">${success_message}
		<span class="closeButton" onclick="this.parentElement.style.display='none';">&times;</span>
	</div>
</c:if>