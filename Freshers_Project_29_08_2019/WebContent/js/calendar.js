/**
 * Script for calendar.jsp will create a calendar and call forms for edit and
 * validations of dates will be done.
 */


// For show 10 years in the drop down.
var min = new Date().getFullYear(),
    max = min + 10,
    select = document.getElementById('year');

// Used to get the months value.
for (var i = min; i<=max; i++){
    var opt = document.createElement('option');
    opt.value = i;
    opt.innerHTML = i;
    select.appendChild(opt);
}

// Will get todays date month and year.
var today = new Date();
var currentMonth = today.getMonth();
var currentYear = today.getFullYear();

// Will get selected year and month from jsp page.
var selectYear = document.getElementById("year");
var selectMonth = document.getElementById("month");

// if the selected year and month is null will show current month and year
if(selectYear === null && selectMonth === null){
	selectYear = currentYear;
	selectMonth = currentMonth;
}


// Array of months.
var months = ["January", "February", "March", "April", "May", "June", "July", "August", "September",
	"October", "November", "December"];

var monthAndYear = document.getElementById("month-and-year");

// Method for showing the calendar of current month and year
showCalendar(currentMonth, currentYear);

// Going to next month when the button for next month is clicked or will go to
// next year if at the end of current year.
function next(){
	currentYear = (currentMonth === 11) ? currentYear + 1 : currentYear;
	currentMonth = (currentMonth + 1) % 12;
	showCalendar(currentMonth, currentYear);
}

// Going to previous month when the button for previous month is clicked or will
// go to previous year if at the start
// of current year.
function previous(){
	currentYear = (currentMonth === 0) ? currentYear - 1 : currentYear;
	currentMonth = (currentMonth === 0) ? 11 : currentMonth - 1;
	showCalendar(currentMonth, currentYear)
}

// Will jump to selected month or year
function jump() {
	currentYear = parseInt(selectYear.value);
	currentMonth = parseInt(selectMonth.value);
	showCalendar(currentMonth, currentYear)
}

// Method will show the calendar and we will be able to view all the
// registration in the current date when we click
// that day.
function showCalendar(month, year){

	var firstDay = (new Date(year, month)).getDay();
	var lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0); 
	var daysInMonth = 32 - new Date(year, month, 32).getDate();
	
	
	var tbl = document.getElementById("calendar-body");
	
	tbl.innerHTML = " ";
	
	monthAndYear.innerHTML = months[month]+" "+year;
	selectYear.value = year;
	selectMonth.value = month;
	
	
	var date = 1;
	var colorNode = 0;
	for(var i = 0; i < 6; i++){
		
		var row = document.createElement("tr");
		for(var j = 0; j < 7; j++){
			if(i === 0 && j < firstDay){
				var cell = document.createElement("td");
				var cellText = document.createTextNode("");
				cell.appendChild(cellText);
				row.appendChild(cell);
			}
			else if (date > daysInMonth){
				var cell = document.createElement("td");
				var cellText = document.createTextNode("");
				cell.appendChild(cellText);
				row.appendChild(cell);
			}
			else{
				var cell = document.createElement("td");
				var cellText = document.createTextNode(date);
				var cellDiv = document.createElement("div");

				var cellDateRow = document.createElement('div');
				cellDateRow.className = "row";
				
				var addNewEntry = document.createElement("i");
				addNewEntry.setAttribute("class", "bi bi-calendar-plus-fill addico ml-1 mb-1");
				addNewEntry.setAttribute("data-toggle", "modal");
				addNewEntry.setAttribute("data-target", "#edit-field");
				addNewEntry.setAttribute("onclick", "clearModal();setDate();");
				addNewEntry.setAttribute("id", "addico");
				addNewEntry.setAttribute("onclick", "clearModal();addNewEntry("+date+","+month+","+year+", event);");
				cellDiv.setAttribute("id", date);
				cell.setAttribute("onclick", "displayAllDayData("+date+","+ month+","+ year+")");
				cell.className = "w-15";

				cellDateRow.appendChild(cellText);
				cellDateRow.appendChild(addNewEntry);

				cell.appendChild(cellDateRow);

				cell.appendChild(cellDiv);
				row.appendChild(cell);
				tbl.appendChild(row);
				displayData(date, month, year);
				if(date === today.getDate() && year == today.getFullYear() && month === today.getMonth()){					
					document.getElementById(date).parentElement.style.backgroundColor = '#88b5d4';
				}
				date++;
			}
		}
		
	}
}

// Method to display all the registration in that day. When the add icon is
// clicked user will be shown the add form
// else user will be shown current dates all the registrations.
function displayAllDayData(date, month, year){
    if(document.getElementById('edit-field').style.display === 'block'){
        document.getElementById('display-all-day-data').style.display='none';
    }else{
        if(document.getElementById(date).innerHTML != ""){
            $('#display-all-day-data').modal('show');
            document.getElementById('displayRecordBody').innerHTML = '';
        }
    }
    document.getElementById('displayRecordsTitle').innerHTML = "<div class='header'><h2>"+date+" "+months[month]+" "+year+"</h2></div>";
    $('#displayRecordBody').html(setDisplayContent(date, month, year));
}

function setDisplayContent(date, month, year){
    var dispMonth = (1 + month);
    var displayHtml = "<ul class='list-group list-group-flush'>";
    var dayContent = schedule[(dispMonth > 9 ? dispMonth : '0' + dispMonth) + '/' + (date > 9 ? date : '0' + date) + '/' + year];
    if(dayContent) {
        dayContent.forEach(function(value, index){
            displayHtml += "<li class='list-group-item'>";
            displayHtml += "<div class='row'>";
            displayHtml += "<div class='col-md-9'>"
            displayHtml += value.startTime + ": " + value.resourceName + ": " + value.userName;
            displayHtml += "</div>"
            displayHtml += "<div class='col-md-3'>"
            displayHtml += getIconsForActions(value);
            displayHtml += "</div>"
            displayHtml += "</div>"
            displayHtml += "</li>";
        });
    }
	displayHtml += "</ul>";
	return displayHtml;
}

function getIconsForActions(value){
    var iconActions = "";
    if(userId == value.userId || isAdmin) {
        iconActions += "<i class='bi bi-trash dis-icon' onclick='getIdForDelete(" + value.reservationId + ");'></i>";
        iconActions += "<i class='bi bi-pencil dis-icon ml-2' data-toggle='modal' data-target='#edit-field' ";
        iconActions += "onclick='getId(" + value.reservationId + ");'></i>";
    }
    iconActions += "<i class='bi bi-exclamation-circle-fill dis-icon ml-2' data-toggle='tooltip' data-placement='right'";
    iconActions += "data-delay='{\"show\":\"10\", \"hide\":\"3000\"}'"
    iconActions += "title='Start Date: " + value.startDate + " " + value.startTime + "\n";
    iconActions += "End Date: " + value.endDate + " " + value.endTime + "\n";
    iconActions += "User name: " + value.userName + "\n";
    iconActions += "Resource Name: " + value.resourceName + "\n";
    
    iconActions += "'></i>";
    return iconActions;
}

// Used to close the modal for display-all-day-data Id.
function closePopUp() {
	document.getElementById('display-all-day-data').style.display='none';
}

// Method to check if the time is in valid format.
function isValidTime(time){
	return !/^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/.test(time)
}

// Method to check if the date is entered in correct format.
function isValidDate(date){
	return !/^(0[1-9]|1[0-2])\/(0[1-9]|1\d|2\d|3[01])\/(19|20)\d{2}$/.test(date);
}

// Method to check if the date and time is in valid format and if all the
// details are filled in properly else
// will return false and render the user unable to submit the form.
function validateEditForm() {
    var formObj = document.getElementById('edit-form');
    clearNotice();

    var resourceName = formObj.resource_name.value
    startDate = formObj.start_date.value;
	startTime = formObj.start_time.value;
	endDate = formObj.end_date.value;
	endTime = formObj.end_time.value;

	formObj.edit_reservation.value = 'edit_calendar_reservation';
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
	formObj.submit();
    return true;
}

// Method for all day event that will disable the time input boxed and set time
// from 9 AM to 5 PM.
function enableDisableTextBox(checkAllDay){
    if (checkAllDay.checked == true){
        $('#startTime').val('09:00');
        $('#endTime').val('17:00');
    }
    else{
        $('#startTime').val('');
        $('#endTime').val('');
    }
}

// Method to call for form to add new entry.
function addNewEntry(date, month, year, event) {
    clearNotice();
    event.stopPropagation();
    $('#edit-field').modal('show');
    $('.datepicker').datepicker("setDate", new Date(year,month,date));
}


// Convert the date in yyyy-MM-dd format so that it can be shown in start and
// end date of add new entry form.
function convert(str) {
	  var date = new Date(str),
	    mnth = ("0" + (date.getMonth() + 1)).slice(-2),
	    day = ("0" + date.getDate()).slice(-2);
	  return [date.getFullYear(), mnth, day].join("-");
	}

var selectedResourceId;
function filterByResource(resource){
	selectedResourceId = resource;
	showCalendar(currentMonth, currentYear);
}