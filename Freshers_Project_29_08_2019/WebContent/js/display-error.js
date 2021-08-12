
function addError (errorMessage) {
	errorList.push(errorMessage);
}

function displayNotice(){
	errorList.forEach(element => {
		showNotice(element);
	});
}

function showNotice(message) {
	var errorDiv = document.createElement('div');
	var errorAnchor = document.createElement('a');
	
	errorDiv.innerHTML = message.replace(/\n/g,"<br />");
	errorDiv.className = 'error-notice';
	
	errorAnchor.innerHTML = '&times;';
	errorAnchor.setAttribute('onclick', 'this.parentNode.style.display = "none"');
	errorAnchor.setAttribute('href', '#');
	errorAnchor.className = 'close-error';
	
	errorDiv.appendChild(errorAnchor);
	
	var div = document.getElementById('errors');
	div.appendChild(errorDiv);
	div.style.display = 'block';
}

function hasErrorNotice(){
	return errorList.length > 0;
}

function clearNotice(){
	document.getElementById('errors').innerHTML = '';
	errorList = [];
}

function displayNoticeOnModal(){
	if(errorList.length > 0) {
		document.getElementById('edit-field').style.display = 'block';
		displayNotice();
	}
}

function addNoticeFormModal(notices) {
	for (var i = 0; i < notices.length; i++) {
		addError(notices[i]);
	}
}