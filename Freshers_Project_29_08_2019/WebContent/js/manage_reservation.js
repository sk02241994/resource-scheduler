/**
 * 
 */

var startDate;
var startTime;
var endDate;
var endTime;


/**
 * Method for enabling and disabling the time input boxes if all day event is selected.
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
function getIdForDelete(resource_id){
	var id = resource_id;
	if(confirm('Do you really want to delete this ?'))
	  window.location='ReservationServlet?form_action=delete&reservation_id='+id;
}

/**
 * Checks if the format of input time matches with format of time.
 * 
 * @param time
 * @returns boolean
 */
function getTime(time){
	var timeFormat = /^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;
		
	if(time.value.match(timeFormat) || time.value.length != 0){
		document.getElementById("error-time").innerHTML = " ";
		return true;
	}else{
		document.getElementById("error-time").innerHTML = "Time cannot be empty";
		return false;
	}
}

/**
 * Checkes if the format of input date matches with format of date.
 * 
 * @param date
 * @returns boolean
 */
function getDate(date){
	var dateFormat = /[0-9]{2}[-]{1}[0-9]{2}[-]{1}[0-9]{4}/;
	
	if(date.value.match(dateFormat) || date.value.length != 0){
		document.getElementById("error-date").innerHTML = " ";
		return true;
	}else{
		document.getElementById("error-date").innerHTML = "Date cannot be empty";
		return false;
	}
}



/**
 * Method for validating the edit form and giving the error if the necessary requirements are not met.
 * 
 * @param formObj
 * @returns boolean
 */
function validateEditForm(formObj) {
	var error = 0;
	startDate = document.forms["edit_Reservation"]["start_date"];
	startTime = document.forms["edit_Reservation"]["start_time"];
	endDate = document.forms["edit_Reservation"]["end_date"];
	endTime = document.forms["edit_Reservation"]["end_time"];
	
	if (Date.parse(endDate.value + " " + endTime.value) < Date
			.parse(startDate.value + " " + startTime.value)) {
		document.getElementById("form-validation-editing").innerHTML = "End Date and time cannot be smaller" +
				" than start date and time";
		return false;
	}
	
	if((Math.round(Date.parse(endDate.value+" "+endTime.value) - Date.parse(startDate.value+" "+startTime.value))/1000)/60 < 10 ){
		document.getElementById("form-validation-editing").innerHTML = "Time difference must be greater than 10 minutes";
		return false;
	}

	if (!getTime(startTime) || !getTime(endTime)) {
		error++;
	}
	if (!getDate(startDate) || !getDate(endDate)) {
		error++;
	}
	
	if(error > 0){
		return false;
	}else{
		return true;
	}
}

/**
 * Method for validating the add form and giving the error if the necessary requirements are not met.
 * 
 * @param formObj
 * @returns boolean
 */
function validateAddForm(formObj){
	var error = 0;
	startDate = document.forms["add_Reservation"]["start_date"];
	startTime = document.forms["add_Reservation"]["start_time"];
	endDate = document.forms["add_Reservation"]["end_date"];
	endTime = document.forms["add_Reservation"]["end_time"];

	if(Date.parse(endDate.value+" "+endTime.value) < Date.parse(startDate.value+" "+startTime.value)){
		document.getElementById("form-validation-adding").innerHTML = "End Date and time cannot be smaller " +
				"than start date and time";
		return false;
	}
	
	
	if((Math.round(Date.parse(endDate.value+" "+endTime.value) - Date.parse(startDate.value+" "+startTime.value))/1000)/60 < 10 ){
		document.getElementById("form-validation-adding").innerHTML = "Time difference must be greater than 10 minutes";
		return false;
	}
	
	if(!getTime(startTime) || !getTime(endTime)){
		error++;
	}
	if(!getDate(startDate) || !getDate(endDate)){
		error++;
	}
	
	if(error > 0 ){
		return false
	}else{
		return true;
	}
}

/**
 * Will show notification bar after insert, update or delete operation is performed.
 */
function onSubmit(){
	document.getElementById('notification').style.display='block';
}


/**
 * if the form-validation-adding id's child has some data in it it will show add reservation form.
 */
function validateErrorInTime(){
	var errorMessageAdding = document.getElementById("form-validation-adding").children[2].innerHTML;
	if(errorMessageAdding != ""){
		document.getElementById('add-reservation').style.display='block'
	}
}