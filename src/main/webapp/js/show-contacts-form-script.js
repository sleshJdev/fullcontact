var selectAllContactsButton = null;

window.onload = function() {
	selectAllContactsButton = document.getElementById("select-all");
	selectAllContactsButton.setAttribute("onclick", "selectAllListener(true);");

	// Change action attribute
	var sendEmailLink = document.getElementById("send-email-link");
	sendEmailLink.onclick = sendMailClickListener;
}

function sendMailClickListener() {
	var checkboxes = checkable.getElementsByTagName("input");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].getAttribute("type") == "checkbox") {
			if (checkboxes[i].checked) {
				var form = document.getElementById("show-contacts-form");
				form.action = "send";
				form.submit();
				return;
			}
		}
	}
	alert("Select contact to send mail!");
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
