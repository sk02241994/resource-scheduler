
/**
 * Get email address for editing user details.
 * 
 * @param email_address
 */
function getUser(userId) {
	goToPage('UserServlet?form_action=edit&userId=' + userId);
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

	return true;
}