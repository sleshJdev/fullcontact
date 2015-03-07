/*
 * Global section
 */

var dialogPhoneActionButton;

var phoneTemplate;

var oldColor;
var phoneTable;

var dialogPhoneCountryCode;
var dialogPhoneOperatorCode;
var dialogPhoneNumber;
var dialogPhoneType;
var dialogPhoneComment;

var tablePhoneCountryCode;
var tablePhoneOperatorCode;
var tablePhoneValue;
var tablePhoneType;
var tablePhoneComment;

/*
 * Main function. Invoke after loaded document. Configure click event for
 * elements;
 * 
 */
function initPhone() {
	var addIcon = document.getElementById("add-phone");
	addIcon.setAttribute("onclick", "phoneEditPopupShow('add');");

	numbered(phoneTable);
	
	phoneTable = document.getElementById("phone-table").tBodies[0];
	phoneTemplate = document.getElementById("table-phone-row-template");
}

/*
 * Prepare popUp window to create new phone
 */
function phoneEditPopupShow(action, rowNumber) {
	// get need elements
	var dialog = document.getElementById("dialog-phone-form");
	var dimmer = document.createElement("div");
	var hook = document.getElementById("dialog-phone-hook");

	/*
	 * Global value. Look file edit-contact-form-scripts.js
	 * Containt link to click window
	 */
	self = dialog;

	// find height for dimmer
	var footer = document.getElementById("footer");
	var point = getPosition(footer);
	var size = getSize(footer);
	var height = point[1] + size[1];

	dimmer.style.width = window.innerWidth + 'px';
	dimmer.style.height = height + 'px';
	dimmer.className = 'dimmer';

	document.body.appendChild(dimmer);

	var left = (window.innerWidth - dialog.offsetWidth) / 2;
	var top = (window.innerHeight - dialog.offsetHeight) / 2;

	dialog.style.position = "fixed";
	dialog.style.visibility = "visible";
	dialog.style.top = top + 'px';
	dialog.style.left = left + 'px';

	clearText(dialog);

	var closeIcon = dialog.getElementsByClassName("close-icon-box")[0];
	closeIcon.onclick = function() {
		document.body.removeChild(dimmer);
		dialog.style.visibility = "hidden";
		if (action == "edit") {
			var rows = phoneTable.getElementsByTagName("tr");
			rows[rowNumber].style.backgroundColor = oldColor;
//			afterEditPhone(rowNumber);
		}
	}

	dialogPhoneActionButton = document.getElementById("dialog-phone-action-button");
	dialogPhoneActionButton.onclick = function() {
		var status;
		if (action == "add") {
			status = afterAddPhone();
		} else {
			status = afterEditPhone(rowNumber);
		}
		
		console.log("validate phone status: " + status);
		
		if(status){
			document.body.removeChild(dimmer);
			dialog.style.visibility = "hidden";
		}
	}

	hook.onmousedown = mouseDownListener;
	hook.ondragstart = dragStartListener;

	initPhoneDialogItems();

	if (action == "add") {
		beforeAddPhone();
	} else {
		beforeEditPhone(rowNumber);
	}

	return false;
}


function createPhoneRow() {
	console.log("createPhoneRow BEGIN");
	
	var allQuantity = phoneTable.getElementsByTagName("tr").length;
	var nextNumber = allQuantity - 1;
	
	console.log("quantity rows: " + allQuantity);
	
	var addedRow = phoneTemplate.cloneNode(true);
	addedRow.getElementsByTagName("span")[0].textContent = nextNumber;
	addedRow.removeAttribute("style"); 
	addedRow.removeAttribute("id"); 
	addedRow.removeAttribute("class"); 
	var insertBefore = phoneTable.getElementsByTagName("tr")[nextNumber];
	phoneTable.insertBefore(addedRow, insertBefore);
	
	var inputs = addedRow.getElementsByTagName("input");

	var phoneId = null;
	for (var i = 0; i < inputs.length; i++) {
		if(inputs[i].type == "hidden" && inputs[i].name == "phone-id"){
			phoneId = inputs[i];
			break;
		}
	}
	phoneId.setAttribute("value", "-1");
	console.log("set -1 id value");
	
	addedRow.getElementsByClassName("edit-phone")[0].setAttribute("id", "edit-phone-icon" + nextNumber);
	addedRow.getElementsByClassName("edit-phone")[0].setAttribute("onclick", "phoneEditPopupShow('edit', " + nextNumber + ");");
	
	console.log("createPhoneRow() END");
	return addedRow;
}

function beforeAddPhone() {
	document.getElementById("dialog-phone-hook").textContent = "ADD PHONE";
	document.getElementById("dialog-phone-hook").style.backgroundColor = "green";
	dialogPhoneActionButton.value = "Add";
}

function beforeEditPhone(rowNumber) {
	console.log("beforeEditPhone() rowNumber: " + rowNumber + " BEGIN");
	document.getElementById("dialog-phone-hook").textContent = "EDIT PHONE";
	document.getElementById("dialog-phone-hook").style.backgroundColor = "orange";
	dialogPhoneActionButton.value = "Save"

	var row = phoneTable.getElementsByTagName("tr")[rowNumber];
	oldColor = row.style.backgroundColor;
	row.style.backgroundColor = "yellow";
	initPhoneTableItems(row);
	readPhoneFromTableToDialog();
	console.log("beforeEditPhone() rowNumber: " + rowNumber + " END");
}

function afterAddPhone() {
	console.log("afterAddPhone() BEGIN");
	var status = validatePhone();
	if(status){
		var row = createPhoneRow();
		initPhoneTableItems(row);
		readPhoneFromDialogToTable();
		numbered(phoneTable);//from edit-contact-form-script.js
	}
	console.log("afterAddPhone() END");
	return status;
}

function afterEditPhone(rowNumber) {
	console.log("afterEditPhone() rowNumber: " + rowNumber + " BEGIN");
	var status = validatePhone();
	if(status){
		var rows = phoneTable.getElementsByTagName("tr");
		rows[rowNumber].style.backgroundColor = oldColor;
		readPhoneFromDialogToTable();
		numbered(atachmentTable);//from edit-contact-form-script.js
	}
	console.log("afterEditPhone() rowNumber: " + rowNumber + " END");
	return status;
}

function validatePhone() {
	console.log("validatePhone() BEGIN");

	var PATTERN;
	var value;
	
	PATTERN = /^[+]{1}[0-9]{3}$/;
	value = dialogPhoneCountryCode.value;
	if (!PATTERN.test(value)) {
		alert("Phone Country Code. Format: +XXX. Can be used only digits.");
		return false;
	}
	
	console.log("\t phone country code: " + value + " is valid!");

	PATTERN = /^[0-9]{2}$/;
	value = dialogPhoneOperatorCode.value;
	if (!PATTERN.test(value)) {
		alert("Phone Operator Code. Format: YY. Can be used only digits.");
		return false;
	}

	console.log("\t phone operator code: " + value + " is valid!");
	
	PATTERN = /^[0-9]{7}$/;
	value = dialogPhoneNumber.value;
	if (!PATTERN.test(value)) {
		alert("Phone Number. Format: NNN NN NN. Can be used only digits.");
		return false;
	}
	
	console.log("\t phone phone number: " + value + " is valid!");

	PATTERN = /^[\w!?\s,.\n\<\>\[\]\"\'\(\):;]{0,100}$/;
	value = dialogPhoneComment.value;
	if (!PATTERN.test(value)) {
		alert("Phone Comment. Max Length 100 characters.");
		return false;
	}

	console.log("\t phone comment: " + value + " is valid!");
	console.log("validatePhone() END");
	return true;
}

function initPhoneTableItems(row) {
	console.log("initPhoneTableItems() BEGIN");
	tablePhoneCountryCode = row.getElementsByClassName("table-phone-country-code")[0];
	tablePhoneOperatorCode = row.getElementsByClassName("table-phone-operator-code")[0];
	tablePhoneValue = row.getElementsByClassName("table-phone-value")[0];
	tablePhoneType = row.getElementsByClassName("table-phone-type")[0];
	tablePhoneComment = row.getElementsByClassName("table-phone-comment")[0];
	console.log("initPhoneTableItems() END");
}

function initPhoneDialogItems() {
	console.log("initPhoneDialogItems() BEGIN");
	dialogPhoneOperatorCode = document.getElementById("dialog-phone-operator-code");
	dialogPhoneNumber = document.getElementById("dialog-phone-value");
	dialogPhoneType = document.getElementById("dialog-phone-type");
	dialogPhoneComment = document.getElementById("dialog-phone-comment");
	dialogPhoneCountryCode = document.getElementById("dialog-phone-country-code");
	console.log("initPhoneDialogItems() BEGIN");
}

function readPhoneFromDialogToTable() {
	console.log("readPhoneFromDialogToTable() BEGIN");
	tablePhoneCountryCode.textContent = dialogPhoneCountryCode.value.trim();
	tablePhoneOperatorCode.textContent = dialogPhoneOperatorCode.value.trim();
	tablePhoneValue.textContent = dialogPhoneNumber.value.trim();
	tablePhoneType.textContent = dialogPhoneType.value.trim();
	tablePhoneComment.textContent = dialogPhoneComment.value.trim();
	console.log("readPhoneFromDialogToTable() END");
}

function readPhoneFromTableToDialog() {
	console.log("readPhoneFromTableToDialog() BEGIN");
	dialogPhoneCountryCode.value = tablePhoneCountryCode.textContent.trim();
	dialogPhoneOperatorCode.value = tablePhoneOperatorCode.textContent.trim();
	dialogPhoneNumber.value = tablePhoneValue.textContent.trim();
	dialogPhoneType.value = tablePhoneType.textContent.trim();
	dialogPhoneComment.value = tablePhoneComment.textContent.trim();
	console.log("readPhoneFromTableToDialog() END");
}