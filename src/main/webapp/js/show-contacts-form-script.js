var selectAllContactsButton = null;

window.onload = function() {
	selectAllContactsButton = document.getElementById("select-all");
	selectAllContactsButton.setAttribute("onclick", "selectAllListener(true);");

	// Change action attribute
	var sendEmailLink = document.getElementById("send-email-link");
	sendEmailLink.onclick = function() {
		alert(this.innerHTML);
		var form = document.getElementById("show-contacts-form");
		form.action = "send";
		form.submit();
	}
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


