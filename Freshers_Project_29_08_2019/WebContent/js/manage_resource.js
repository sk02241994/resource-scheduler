
/**
 * Method will request for editing by passing the id of resource.
 * 
 * @param resource_id
 */
function getResource(resourceId) {
	clearNotice();
    enableButton();
    $.ajax({
        url: 'ResourceServlet',
        type: 'GET',
        dataType: 'json',
        data: {form_action: 'edit', resourceId: resourceId},
        contentType: 'application/json',
        success: function(data){
            displayData(data);
        }
    });

}

function displayData(data){
    $('#edit-form #resourceId').val(data.rsResourceId);
    $('#edit-form #resourceName').val(data.resourceName);
    $('#edit-form #description').val(data.resourceDescription);

    var hours = (data.timeLimit / 60);
    var rHours = Math.floor(hours);
    $('#edit-form #timeLimitHours').val(isNaN(rHours) || rHours == 0 ? '' : rHours);
    var minutes = (hours - rHours) * 60;
    var rminutes = Math.round(minutes);
    $('#edit-form #timeLimitMinutes').val(isNaN(rminutes) || rminutes == 0 ? '' : rminutes);

    $('#edit-form #maxUserBooking').val(data.maxUserBooking);
    $('#edit-form #isenabled').prop('checked', data.isEnabled);
    $('#edit-form #isAllowedMultiple').prop('checked', data.isAllowedMultiple);
    $('#edit-form #isAllowEmpOnProbation').prop('checked', data.isPermanentEmployee);
}

/**
 * Method will request for delete by passing resource id to be deleted.
 * 
 * @param resource_id
 */
function deleteResource(resourceId) {
	if (confirm("Do you really want to delete this ?")) {
		goToPage('ResourceServlet?form_action=delete&resourceId=' + resourceId);
	}
}

/**
 * Method will validate so that all the required resource form details are
 * filled.
 * 
 * @param formObj
 * @returns boolean
 */
function validateResource() {

    var formObj = document.getElementById('edit-form');
    clearNotice();

	/*if (formObj.resource_name.value.trim().length == 0) {
		addError('Please Enter the resource name.')
	}*/
	if (hasErrorNotice()) {
		displayNotice();
		return false;
	}
	
	disableButton();
	formObj.submit();
	return true;
}

function onlyNumber(hoursMins) {
	return hoursMins.value.replace(/[^0-9.]/g, '')
}