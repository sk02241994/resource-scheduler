/**
 * 
 */

// Method for toggling the display of details of reservation.
function show(id){
		debugger;
	if(id[1].style.display === "block"){
		id[1].style.display = "none";
	}else{
		id[1].style.display="block";
	}
}

// Method to request for details of a reservation of a particular reservation id. 
function getData(id){
	window.location='ReservationServlet?form_type=calendar&form_action=edit&reservation_id='+id;	
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
	var category = {};
	var fistDay = new Date(today.getFullYear(), today.getMonth(), 1);
	var lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0); 

	var groupby = "startDate";

	for (var i = 0; i < schedule.length; i++) {
		if (!category[schedule[i][groupby]])
			category[schedule[i][groupby]] = [];
		if (selectedResourceId == schedule[i]["resourceId"]
				|| (selectedResourceId == undefined 
				|| selectedResourceId.trim().length == 0))
		category[schedule[i][groupby]].push(schedule[i]);
	}

	for (key in category) {
		if (category.hasOwnProperty(key)) {
			var dateKey = new Date(key);
			if (category[key].length + "\n") {
				for (var i = 0; i < category[key].length; i++) {
					if (dateKey.getMonth() == month && dateKey.getFullYear() == year) {
						
						var startTime = new Date(category[key][i].startDate).getDate();
						var endTime = new Date(category[key][i].endDate).getDate();
						var reservationId = date+'0'+category[key][i].reservationId ;
						
						if (date >= startTime && date <= endTime) {
							
							var dataDisplay = '<span id ='+reservationId +'>'
									+ category[key][i].startTime + ": "
									+ category[key][i].resourceName + "; "
									+ category[key][i].userName;
							
							var dataExisting = document.getElementById(date).innerHTML;
							dataExisting = dataExisting + dataDisplay;
							var count = 0;
							if (userId == category[key][i].userId || isAdmin) {
								
								document.getElementById(date).innerHTML = dataExisting + 
								"<i id='delico' class='bi bi-trash dis-icon' " +
								" onclick='getIdForDelete("+category[key][i].reservationId+")'></i>"+
								"<i id='editico' class='bi bi-pencil dis-icon' " +
								"onclick='getData("+category[key][i].reservationId+")'></i>" +
								
								"<i class='bi bi-exclamation-circle-fill dis-icon' data-toggle='tooltip' data-placement='right'" +
								" title='Start Date: "+category[key][i].startDate + " " + category[key][i].startTime+"" +
										"\nEnd Date: " + category[key][i].endDate + " " + category[key][i].endTime +
										"\nUser Name: " + category[key][i].userName +
										"\nResource Name: " + category[key][i].resourceName +
										"'></i>"
								
								/*"<i id='editico' class='bi bi-exclamation-circle-fill'" +
								"data-toggle='tooltip'"+
								//"onclick='show(tooltip"+reservationId+")'></i>" +
								"title='"+
								"<div id=\'tooltip"+reservationId+"\' class=\'tooltip\'>Start: "+category[key][i].startDate+
								" "+category[key][i].startTime+
								"<br/>End: "+category[key][i].endDate+" "+category[key][i].endTime+
								"<br/>User Name: "+category[key][i].userName+
								"<br/>Resource Name: "+category[key][i].resourceName+
								"</div>" + "</span><br/>"
								"'</i>";*/
								
								document.getElementById(reservationId).style.background = '#8DD6C2';
							} else {
								document.getElementById(date).innerHTML = dataExisting +
										"<img id='editico' src='/ResourceScheduler/js/information.png'" +
										"onclick='show(tooltip"+reservationId+")'>"+
										"<div id='tooltip"+reservationId+"' class='tooltip'>Start: "+category[key][i].startDate+
										" "+category[key][i].startTime+
										"<br/>End Time: "+category[key][i].endDate+" "+category[key][i].endTime+
										"<br/>User Name: "+category[key][i].userName+
										"<br/>Resource Name: "+category[key][i].resourceName+
										"</div></span><br/>";
								document.getElementById(reservationId).style.background = '#95C6E8';
							}
							
						}
						
					}
				}
			}
		}
	}
}