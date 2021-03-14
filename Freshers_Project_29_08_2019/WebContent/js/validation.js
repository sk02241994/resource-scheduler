/**
 * 
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

// Pattern comparison to check if the password entered is valid enough
function validatePassword(password){
	var passformat = "^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{6,}$";
	var errorMessage = "Password must contain at least one letter, at least one number, and be longer than six charaters.";
	if(password.value.match(passformat)){
		document.getElementById('error-name').innerHTML = " ";
		return true;
	}
	else{
		document.getElementById('error-name').innerHTML = errorMessage;
		return false;
	}
	
}

// Check if the new password and confirm password are the same
function checkpassword(form){
	var e = form.elements;
	
	if (e['upassword'].value != e['new_password'].value){
		alert ("Password does not match. Please try again");
		return false;
	}
	return true;
}

function validateForm(){
	var error = 0;
	
	if(!validatePassword(password)){
		error++;
	}
	if(!validateEmail(email)){
		error++;
	}
	
	if (error > 0){
		return false;
	}
	else{
		return true;
	}
}

function goToLogin(){
	window.location = "/ResourceScheduler/LoginServlet";//rs-jsp/login.jsp
}