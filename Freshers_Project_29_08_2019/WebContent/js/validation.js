function goToLogin() {
	window.location = "/ResourceScheduler/LoginServlet";
}

function validateChangePassword(form) {
	clearNotice();

	var oldPassword = form.old_password.value.trim();
	var newPassword = form.new_password.value.trim();
	if (oldPassword.length == 0) {
		addError('Please enter password.');
	}

	if (newPassword.length == 0) {
		addError('Please enter confirm password.');
	}

	if (oldPassword.length != 0 && newPassword.length != 0
			&& oldPassword != newPassword) {
		addError('Password does not match. Please try again.');
	}

	var regex = /^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{6,}$/;
	if (!regex.test(oldPassword) || !regex.test(newPassword)) {
		addError('Password must contain at least one letter, at least one number, and be longer than six charaters.');
	}

	if (hasErrorNotice()) {
		displayNotice();
		return false;
	}

	return true;
}