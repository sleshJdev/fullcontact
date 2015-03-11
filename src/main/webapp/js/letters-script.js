var selectAllContactsButton = null;

window.onload = function() {
	selectAllContactsButton = document.getElementById("select-all");
	selectAllContactsButton.setAttribute("onclick", "selectAllListener(true);");
	
	document.getElementById("delete-emails-form").setAttribute("onsubmit", "return deleteLettersListener();");
}

function deleteLettersListener() {
	console.log("deleteLettersListener() BEGIN");
	
	var checkboxes = checkable.getElementsByTagName("input");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].getAttribute("type") == "checkbox") {
			if (checkboxes[i].checked) {
				document.getElementById("delete-emails-form").submit();
				return true;
			}
		}
	}
	
	alert("Select letters to delete!");
	
	console.log("deleteLettersListener() END");
	return false;
}

function selectAllListener(checked) {
	var checkable = document.getElementById("checkable");
	var checkboxes = checkable.getElementsByTagName("input");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].getAttribute("type") == "checkbox") {
			checkboxes[i].checked = checked;
		}
	}
	if (checked) {
		selectAllContactsButton.setAttribute("onclick", "selectAllListener(false);");
	} else {
		selectAllContactsButton.setAttribute("onclick", "selectAllListener(true);");
	}
}
