/*
 * Global section
 */
var dialogAtachmentActionButton;

var atachmentTemplate;

var oldColor;
var atachmentTable;

var dialogAtachmentName;
var dialogAtachmentComment;

var tableAtachmentComment;
var tableAtachmentName;

/*
 * Main function. Invoke after loaded document. Configure click event for
 * elements;
 * 
 */
function initAtachment() {
	var addIcon = document.getElementById("add-atachment");
	addIcon.setAttribute("onclick", "atachmentEditPopupShow('add');");

	var editIcons = document.getElementsByClassName("edit-atachment");
	for (var i = 0; i < editIcons.length; ++i) {
		editIcons[i].setAttribute("onclick", "atachmentEditPopupShow('edit', " + (i + 1) + ");")
	}

	atachmentTable = document.getElementById("atachment-table").tBodies[0];
	atachmentTemplate = document.getElementById("table-atachment-row-template");
}

function atachmentEditPopupShow(action, rowNumber) {
	// get need elements
	var dialog = document.getElementById("dialog-atachment-form");
	var dimmer = document.createElement("div");
	var hook = document.getElementById("dialog-atachment-hook");

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
			afterAtachmentEdit(rowNumber);
		}
		removeField();
	}

	dialogAtachmentActionButton = document.getElementById("dialog-atachment-action-button");
	dialogAtachmentActionButton.onclick = function() {
		var status;
		
		if (action == "add") {
			status = afterAtachmentAdd();
		} else {
			status = afterAtachmentEdit(rowNumber);
		}
		
		console.gol("validate status :" + status);
		
		if(status){
			document.body.removeChild(dimmer);
			dialog.style.visibility = "hidden";
		}
	}

	hook.onmousedown = mouseDownListener;
	hook.ondragstart = dragStartListener;

	if (action == "add") {
		beforeAtachmentAdd();
	} else {
		beforeAtachmentEdit(rowNumber);
	}

	return false;
}


function removeField(){
	var input = document.getElementById("dialog-atachment-name");
	input.parentNode.removeChild(input);
}

/*
 * Prepare popUp window to create new atachment
 */
function createAtachmentField(type){
	var field = document.createElement("input");
	field.id = "dialog-atachment-name";
	field.name = "dialog-atachment-name";
	field.placeholder = "Atachment Name";
	field.maxlength = "50";
	field.title="Atachment Name. Max Length 30 characters. Can be used only letters and digits.";
	field.type = type;
	
	var switcher = document.getElementById("dialog-field-switcher");
	switcher.appendChild(field);
	
	return field;
}

function beforeAtachmentAdd() {
	document.getElementById("dialog-atachment-hook").textContent = "ADD ATACHMENT";
	document.getElementById("dialog-atachment-hook").style.backgroundColor = "green";
	dialogAtachmentActionButton.value = "Add";
	
	var f = createAtachmentField("file");	
	initAtachmentDialogItems();
}

function beforeAtachmentEdit(rowNumber) {
	document.getElementById("dialog-atachment-hook").textContent = "EDIT ATACHMENT";
	document.getElementById("dialog-atachment-hook").style.backgroundColor = "orange";
	dialogAtachmentActionButton.value = "Save";
	
	var f = createAtachmentField("text");
	initAtachmentDialogItems();
	
	var row = atachmentTable.getElementsByTagName("tr")[rowNumber];
	oldColor = row.style.backgroundColor;
	row.style.backgroundColor = "yellow";
	initAtachmentTableItems(row);
	readAtachmentFromTableToDialog();//TODO: need implement copy data for 'text' and 'file'
}

function afterAtachmentAdd() {
	var status = validate();
	if(status){
		var row = createAtachmentRow();
		initAtachmentTableItems(row);
		readAtachmentFromDialogToTable("add");//TODO: need implement copy data for 'text' and 'file'
		numbered(atachmentTable);//from edit-contact-form-script.js
		removeField();
	}
	
	return status;
}

function afterAtachmentEdit(rowNumber) {
	var status = validate();
	if (status) {
		var rows = atachmentTable.getElementsByTagName("tr");
		rows[rowNumber].style.backgroundColor = oldColor;
		readAtachmentFromDialogToTable("edit");
		removeField();
	}
	
	return status;
}

function validate() {
	var NAME_PATTERN = /^[A-Za-z0-9_\-\.]{3,50}$/;
	var name = dialogAtachmentName.value;
	if (!NAME_PATTERN.test(name)) {
		alert("Atachment Name. Max Length 50 characters. Can be used only letters and digits.");
		return false;
	}

	var COMMENT_PATTERN = /^.{0,100}$/;
	var comment = dialogAtachmentComment.value;
	if (!COMMENT_PATTERN.test(comment)) {
		alert("Atachment Comment. Max Length 100 characters.");
	}

	return true;
}

function createAtachmentRow() {
	console.log("createAtachmentRow BEGIN");

	var allQuantity = atachmentTable.getElementsByTagName("tr").length;
	var nextNumber = allQuantity - 1;
	
	console.log("quantity rows: " + allQuantity);
	
	var addedRow = atachmentTemplate.cloneNode(true);
	addedRow.getElementsByTagName("span")[0].textContent = nextNumber;
	addedRow.removeAttribute("style"); 
	addedRow.removeAttribute("id"); 
	addedRow.removeAttribute("class"); 
	var insertBefore = atachmentTable.getElementsByTagName("tr")[nextNumber];
	atachmentTable.insertBefore(addedRow, insertBefore);
	
	var inputs = addedRow.getElementsByTagName("input");

	var atachmentId = null;
	for (var i = 0; i < inputs.length; i++) {
		if(inputs[i].type == "hidden" && inputs[i].name == "atachment-id"){
			atachmentId = inputs[i];
			break;
		}
	}
	atachmentId.setAttribute("value", "-1");
	console.log("createAtachmentRow() >>> set -1 id value");
	
	addedRow.getElementsByClassName("edit-atachment")[0].setAttribute("id", "edit-atachment-icon" + nextNumber);
	addedRow.getElementsByClassName("edit-atachment")[0].setAttribute("onclick", "atachmentEditPopupShow('edit', " + nextNumber + ");");

	console.log("createAtachmentRow END");
	return addedRow;
}

function initAtachmentTableItems(row) {
	tableAtachmentName = row.getElementsByClassName("table-atachment-name")[0];
	tableAtachmentComment = row.getElementsByClassName("table-atachment-comment")[0];
}

function initAtachmentDialogItems() {
	dialogAtachmentName = document.getElementById("dialog-atachment-name");
	dialogAtachmentComment = document.getElementById("dialog-atachment-comment");
}

function readAtachmentFromDialogToTable(actionType) {
	switch (actionType) {
	case "add":

		break;
	case "edit":
		tableAtachmentComment.textContent = dialogAtachmentComment.value;
		tableAtachmentName.textContent = dialogAtachmentName.value;
		break;
	}
}

function readAtachmentFromTableToDialog() {
	dialogAtachmentName.value = tableAtachmentName.textContent;
	dialogAtachmentComment.value = tableAtachmentComment.textContent;
}