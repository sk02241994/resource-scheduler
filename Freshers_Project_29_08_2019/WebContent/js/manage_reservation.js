/**
 * 
 */

var startDate;
var startTime;
var endDate;
var endTime;

/**
 * Method for enabling and disabling the time input boxes if all day event is
 * selected.
 * 
 * @param checkAllDay
 */
function enableDisableTextBox(checkAllDay){
	if (checkAllDay.checked == true){
		document.getElementById("time-start").value = "09:00";
		document.getElementById("time-start").readOnly = true;
		document.getElementById("time-end").value = "17:00";
		document.getElementById("time-end").readOnly = true;
	}
	else{
		document.getElementById("time-start").readOnly = false;
		document.getElementById("time-start").value = "";
		document.getElementById("time-end").readOnly = false;
		document.getElementById("time-end").value = "";
	}
}

/**
 * Will request the servlet for editing the details of perticular id.
 * 
 * @param reservation_id
 */
function getId(reservation_id){
	var id = reservation_id;
	window.location='ReservationServlet?form_action=edit&reservation_id='+id;
	
}

/**
 * Will request the servlet for deleting a particular id.
 * 
 * @param resource_id
 */
function getIdForDelete(resourceId){
	if(confirm('Do you really want to delete this ?'))
	  window.location='ReservationServlet?form_action=delete&reservation_id='+resourceId;
}

//Method to check if the time is in valid format.
function isValidTime(time){
    return !/^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/.test(time)
}

// Method to check if the date is entered in correct format.
function isValidDate(date){
    return !/[0-9]{4}[-]{1}[0-9]{2}[-]{1}[0-9]{2}/.test(date);
}

/**
 * Method for validating the edit form and giving the error if the necessary
 * requirements are not met.
 * 
 * @param formObj
 * @returns boolean
 */
function validateEditForm(formObj) {
    clearNotice();

    var resourceName = formObj.resource_name.value
    startDate = formObj.start_date.value;
    startTime = formObj.start_time.value;
    endDate = formObj.end_date.value;
    endTime = formObj.end_time.value;

    if(resourceName.length == 0) {
        addError('Please select a resource.');
    }

    if(startDate.trim().length == 0) {
        addError('Please enter start date.');
    }
    
    if(startTime.trim().length == 0) {
        addError('Please enter start time.');
    }
    
    if(endDate.trim().length == 0) {
        addError('Please enter end date.');
    }
    
    if(endTime.trim().length == 0) {
        addError('Please enter end time.');
    }

    if(startDate.trim().length != 0 && isValidDate(startDate)) {
        addError('Please enter valid start date.');
    }
    if(startTime.trim().length != 0 && isValidTime(startTime)) {
        addError('Please enter valid start time.');
    }
    if(endDate.trim().length != 0 && isValidDate(endDate)) {
        addError('Please enter valid end date.');
    }
    if(endTime.trim().length != 0 && isValidTime(endTime)) {
        addError('Please enter valid end time.');
    }
    
    if (Date.parse(endDate.value + " " + endTime.value) < Date.parse(startDate.value + " " + startTime.value)) {
        addError('End Date and time cannot be before start date and time.');
    }
    
    if(((Math.round(Date.parse(endDate.value+" "+endTime.value) - Date.parse(startDate.value+" "+startTime.value))/1000)/60 < 10 ) && error == 0){
        addError('Time difference must be greater than 10 minutes.');
    }

    if (hasErrorNotice()) {
        displayNotice();
        return false;
    }
    disableButton();
    return true;
}

