

/**
 * Get email address for editing user details.
 * 
 * @param email_address
 */
function getEmail(email_address){
	var address = email_address;
	window.location='UserServlet?form_action=edit&email_address='+address;
	
}


/**
 * Method will validate email address according to what is input by user.
 * 
 * @param mail
 * @returns boolean
 */
function validateEmail(mail){
	var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

	if(mail.value.match(mailformat)){
		document.getElementById("error-mail").innerHTML = " ";
		return true;
	}
	else{
		document.getElementById("error-mail").innerHTML = "You have entered invalid email format";
		return false;
	}
	
}

/**
 * Method to validate users first name.
 * 
 * @param name
 * @returns boolean
 */
function validateFirstName(name){
	
	var nameformat = /^[A-z][a-z]{1,30}$/;
	
	if(name.value.match(nameformat)){
		document.getElementById("error-firstname").innerHTML = " ";
		return true;
	}
	else{
		document.getElementById("error-firstname").innerHTML = "You have entered invalid name format";
		return false;
	}
	
}

/**
 * Method to validate user last name.
 * 
 * @param name
 * @returns boolean
 */
function validateLastName(name){
	
	var nameformat = /^[A-z][a-z]{1,30}$/;
	
	if(name.value.match(nameformat)){
		document.getElementById("error-lastname").innerHTML = " ";
		return true;
	}
	else{
		document.getElementById("error-lastname").innerHTML = "You have entered invalid name format";
		return false;
	}
	
}

/**
 * Method to validate add form and will notify user if error occurs.
 * 
 * @param formObj
 * @returns boolean
 */
function validateAddForm(formObj){
	var error = 0;
	
	var firstName = document.forms["add_user"]["first_name"].value;
	var lastName = document.forms["add_user"]["last_name"].value;
	var email = document.forms["add_user"]["email"];
	var designation = document.forms["add_user"]["designation"].value;
	var department = document.forms["add_user"]["department"].value;	
	
	if(email.length == 0){
		error++;
	}
	if(firstName.length == 0){
		error++;
	}
	if(lastName.length == 0){
		error++;
	}
	
	if(designation == "Designation"){
		error++;
	}
	
	if(department == ""){
		error++;
	}
	if(error > 0){
		document.getElementById("error-user").innerHTML = "Please Enter all the details";
		return false;
	}
	else{
		return true;
	}

}

/**
 * Method will open add-user form if there is error from server side and will notify what that error is. 
 */
function validateErrorForm() {
	var error = document.getElementsByClassName("error-user").childern[1].innerHTML;
	debugger;
	if(error == ""){
		document.getElementById('add-user').style.display='block';
	}
	else{
		document.getElementById('add-user').style.display='none';
	}
}