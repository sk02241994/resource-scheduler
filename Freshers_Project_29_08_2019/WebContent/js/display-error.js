
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
	var errorButton = document.createElement('button');
	var errorSpan = document.createElement('span');
	
	errorDiv.innerHTML = message.replace(/\n/g,"<br />");
	errorDiv.className = 'alert alert-danger alert-dismissible fade show text-center';
	
	errorButton.setAttribute('type', 'button');
	errorButton.setAttribute('data-dismiss', 'alert');
	errorButton.setAttribute('aria-label', 'Close');
	errorButton.className = 'close';
	
	errorSpan.setAttribute('aria-hidden', 'true');
	errorSpan.innerHTML = '&times;';

	errorButton.appendChild(errorSpan);
	errorDiv.appendChild(errorButton);
	
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
	if(errorList && errorList.length > 0) {
	    $('#edit-field').modal('show');
		displayNotice();
	}
}

function addNoticeFormModal(notices) {
    if(notices) {
        for (var i = 0; i < notices.length; i++) {
            addError(notices[i]);
        }
    }
}