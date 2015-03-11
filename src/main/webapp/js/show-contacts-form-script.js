var selectAllContactsButton = null;

window.onload = function() {
	selectAllContactsButton = document.getElementById("select-all");
	selectAllContactsButton.setAttribute("onclick", "selectAllListener(true);");

	// Change action attribute
	var sendEmailLink = document.getElementById("send-email-link");
	sendEmailLink.onclick = sendMailClickListener;
}

function sendMailClickListener() {
	console.log("sendMailClickListener() BEGIN");

	var checkboxes = checkable.getElementsByTagName("input");
	var quantityCheckboxes = 0;
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].getAttribute("type") == "checkbox") {
			if (checkboxes[i].checked) {
				submit();
				return;
			}
			++quantityCheckboxes;
		}
	}
	console.log("quantity row: " + quantityCheckboxes);
	if(quantityCheckboxes == 0){//then this admim
		var input = document.createElement("input");
		input.setAttribute("id", "1");//admin
		document.getElementById("show-contacts-form").appendChild(input);
		submit();
		return;
	}
	
	console.log("sendMailClickListener() END");
	alert("Select contact to send mail!");
}

function submit(){
	var form = document.getElementById("show-contacts-form");
	form.action = "send";
	form.submit();
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
		selectAllContactsButton.setAttribute("onclick",
				"selectAllListener(false);");
	} else {
		selectAllContactsButton.setAttribute("onclick",
				"selectAllListener(true);");
	}
}
