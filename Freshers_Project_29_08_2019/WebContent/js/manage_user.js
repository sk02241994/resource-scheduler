
/**
 * Get email address for editing user details.
 * 
 * @param email_address
 */
function getUser(userId) {
    clearNotice();
    enableButton();
    $.ajax({
        url: 'UserServlet',
        type: 'GET',
        dataType: 'json',
        data: {form_action: 'edit', userId: userId},
        contentType: 'application/json',
        success: function(data){
            displayData(data);
        }
    });


}

function displayData(data){
    $('#edit-form #userId').val(data.rsUserId);
    $('#edit-form #name').val(data.name);
    $('#edit-form #email').val(data.email_address);
    $('#edit-form #isenabled').prop('checked', data.isEnabled);
    $('#edit-form #isadmin').prop('checked', data.isAdmin);
    $('#edit-form #isPermanentEmployee').prop('checked', data.isPermanentEmployee);
    $('input[name="gender"]').each(function(){
        if($(this).val() == data.gender){
            $(this).prop('checked', 'checked');
        }
    });
}

/**
 * Method will validate email address according to what is input by user.
 * 
 * @param mail
 * @returns boolean
 */
function isNotValidEmail(mail) {
	return !/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail);
}

/**
 * Method to validate users first name.
 * 
 * @param name
 * @returns boolean
 */
function isNotValidName(name) {
	return !/^[a-zA-Z]{1,60}\s?[a-zA-Z]{1,30}$/.test(name);
}

/**
 * Method to validate the form.
 * 
 * @param form
 * @returns
 */
function validateForm(form) {

    var form = document.getElementById('edit-form');
	clearNotice();

	if (form.name.value.trim().length == 0) {
		addError('Please enter name.');
	}

	if (form.email.value.trim().length == 0) {
		addError('Please enter email.');
	}

	if (form.name.value.trim().length != 0 && isNotValidName(form.name.value)) {
		addError('Please enter valid name.');
	}

	if (form.email.value.trim().length != 0 && isNotValidEmail(form.email.value)) {
		addError('Please enter valid email.');
	}

	if (hasErrorNotice()) {
		displayNotice();
		return false;
	}

	disableButton();
	form.submit();
	return true;
}
