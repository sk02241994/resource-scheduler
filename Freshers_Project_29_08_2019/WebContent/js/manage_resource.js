

/**
 * Method will request for editing by passing the id of resource.
 * 
 * @param resource_id
 */
function getId(resource_id){
	var id = resource_id;
	window.location='ResourceServlet?form_action=edit&resource_id='+id;
	
}


/**
 * Method will request for delete by passing resource id to be deleted.
 * 
 * @param resource_id
 */
function getIdForDelete(resource_id){
	var id = resource_id;
	if(confirm("Do you really want to delete this ?"))
		window.location='ResourceServlet?form_action=delete&resource_id='+id;
}

/**
 * Method will validate so that all the required resource form details are filled.
 * 
 * @param formObj
 * @returns boolean
 */
function validateResource(formObj){
	
	var resourceName = document.forms["resource_form"]["resource_name"].value;
	console.log(resourceName);
	if(resourceName.length === 0){
		document.getElementById("error-resource").innerHTML = "Please Enter the resource name"
		return false;
	}
	return true;
}

/**
 * Method will check if the error-resource-validation id has come content in it so it will keep the add reource form 
 * open else close the form.
 */
function formValidationAdding(){
	var errorMessageAdding = document.getElementById("error-resource-validation").children[1].innerHTML;
	
	if(errorMessageAdding != "" ){
		document.getElementById('add-resource').style.display='block';
	}else{
		document.getElementById('add-resource').style.display='none'
	}
}