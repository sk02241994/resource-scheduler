/**
 * Method to display modal window
 * 
 * @param isEditButton
 * @param userId
 * @returns
 */
function displayModalWindow(isEditButton, userId) {
    clearNotice();
    enableButton();
	if(!isEditButton){
		document.getElementById('edit-field').style.display = 'none';
	} else {
		var fields = ['text', 'textarea', 'hidden', 'date', 'time', 'select-one'];
		if (userId == "0") {
			var form = document.getElementById("edit-form");
			Array.from(form.elements).forEach(input => {
				if(fields.includes(input.type)) {
					input.value = '';
					input.readOnly = false;
				}
				if (input.type == 'checkbox') {
					input.removeAttribute('checked');
				}
			});
			document.getElementById('edit-field').style.display = 'block';
		} else {
			document.getElementById('edit-field').style.display = 'block';
		}
	}
}

function goToPage(url) {
	window.location = url;
}

var executeEvent = function (element, event, func) {
	if (element.addEventListener) {
			element.addEventListener(event, function(e) { runEvents(func, e); }, false);
	    } else {
	    	element.attachEvent("on"+event, function(e) { runEvents(func, e); });
	    }
};

function runEvents (func, e) {
	if(func) {
		func.call(this, e);
	}
}

function disableButton(){
    var button = document.getElementsByClassName('button');
    for(var i = 0; i < button.length; i++) {
        button[i].disabled = true;
    }
}

function enableButton(){
    var button = document.getElementsByClassName('button');
    for(var i = 0; i < button.length; i++) {
        button[i].disabled = false;
    }
}