
function getId(reservationId){
    clearNotice();
    enableButton();
    $('#display-all-day-data').modal('hide');
    $.ajax({
        url: 'ReservationServlet',
        type: 'GET',
        dataType: 'json',
        data: {form_action: 'edit', reservation_id: reservationId, form_type: 'calendar'},
        contentType: 'application/json',
        success: function(data){
            displayDataOnEdit(data);
        }
    });
    
}

function displayDataOnEdit(data) {
    if(data){
        $('#edit-form #reservationId').val(data.reservationId);
        $('#edit-form #userId').val(data.userId);
        $('#edit-form #resourceName option[value='+ data.resourceId +']').attr("selected", "selected");
        $('#edit-form #startDate').val(data.startDate);
        $('#startDate').datepicker("setDate", data.startDate);
        $('#edit-form #startTime').val(data.startTime);
        $('#edit-form #endDate').val(data.endDate);
        $('#endDate').datepicker("setDate", data.endDate);
        $('#edit-form #endTime').val(data.endTime);
    }
}

// Method to request for delete a particular reservation by passing it's id.
function getIdForDelete(id) {
	if(confirm('Do you really want to delete this ?'))
		window.location='ReservationServlet?form_type=calendar&form_action=delete&reservation_id='+ id + '&form_type=calendar';
}

// Method to display all the reservations on the particular date in the calendar. 
// This method also groups the data in accordance to the start date of the reservations.
// This method also allows to update and delete reservation.
function displayData(date, month, year) {

	for (key in schedule) {
        if (schedule.hasOwnProperty(key)) {
            var dateKey = new Date(key);
            if (schedule[key].length + "\n") {
                for (var i = 0; i < schedule[key].length; i++) {
                    if (dateKey.getMonth() == month && dateKey.getFullYear() == year) {
                        var startTime = new Date(schedule[key][i].startDate).getDate();
                        var endTime = new Date(schedule[key][i].endDate).getDate();
                        var reservationId = date + '0' + schedule[key][i].reservationId;
                        if((selectedResourceId == schedule[key][i].resourceId) || selectedResourceId == undefined || selectedResourceId == ''){
                            if (date >= startTime && date <= endTime) {
                                var dataDisplay = '<div class=\'row ml-0 d-inline-block text-truncate\' style=\'max-width: 99%;\' id =' + reservationId +'>'
                                + schedule[key][i].startTime + ": "
                                + schedule[key][i].resourceName + "; "
                                + schedule[key][i].userName
                                + "</row><br>";

                                var dataExisting = document.getElementById(date).innerHTML;
                                dataExisting = dataExisting + dataDisplay;
                                var count = 0;
                                document.getElementById(date).innerHTML = dataExisting
                                if (userId == schedule[key][i].userId || isAdmin) {
                                    document.getElementById(reservationId).style.background = '#8DD6C2';
                                } else {
                                    document.getElementById(reservationId).style.background = '#95C6E8';
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}