
/**
 * Method will request for editing by passing the id of resource.
 * 
 * @param resource_id
 */
function getResource(resourceId) {
	goToPage('ResourceServlet?form_action=edit&resourceId=' + resourceId);

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
function validateResource(formObj) {

	if (formObj.resource_name.value.trim().length == 0) {
		addError('Please Enter the resource name.')
	}
	if (hasErrorNotice()) {
		displayNotice();
		return false;
	}
	
	disableButton();
	return true;
}

function onlyNumber(hoursMins) {
	return hoursMins.value.replace(/[^0-9.]/g, '')
}