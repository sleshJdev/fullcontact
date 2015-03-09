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

	numbered(atachmentTable);
	
	atachmentTable = document.getElementById("atachment-table").tBodies[0];
	atachmentTemplate = document.getElementById("table-atachment-row-template");
}

function getAtachmentName(uuid, separator){
	var index = uuid.lastIndexOf(separator);
	return uuid.substring(index + 1);
}

/*
 * Atachment dialog: editing, add
 */
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
		removeField();
		if (action == "edit") {
			var row = atachmentTable.getElementsByTagName("tr")[rowNumber];
			row.style.backgroundColor = oldColor;
//			if (afterAtachmentEdit(rowNumber)) {
//			}
		}
	}

	dialogAtachmentActionButton = document.getElementById("dialog-atachment-action-button");
	dialogAtachmentActionButton.onclick = function() {
		var status;
		
		if (action == "add") {
			status = afterAtachmentAdd();
		} else {
			status = afterAtachmentEdit(rowNumber);
		}
		
		console.gol("validate atachment status :" + status);
		
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
	var input = document.getElementById("atachment-file");
	input.parentNode.removeChild(input);
}

/*
 * Prepare popUp window to create new atachment
 */
function createAtachmentField(type){
	var field = document.createElement("input");
	field.id = "atachment-file";
	field.name = "atachment-file";
	field.placeholder = "atachment-file";
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
	
	var fileField = createAtachmentField("file");
	
	initAtachmentDialogItems();
}

function beforeAtachmentEdit(rowNumber) {
	console.log("beforeAtachmentEdit()  BEGIN");
	console.log("\trowNumber: " + rowNumber);
	
	document.getElementById("dialog-atachment-hook").textContent = "EDIT ATACHMENT";
	document.getElementById("dialog-atachment-hook").style.backgroundColor = "orange";
	dialogAtachmentActionButton.value = "Save";
	
	var textField = createAtachmentField("text");
	initAtachmentDialogItems();
	
	var row = atachmentTable.getElementsByTagName("tr")[rowNumber];
	oldColor = row.style.backgroundColor;
	row.style.backgroundColor = "yellow";
	initAtachmentTableItems(row);
	readAtachmentFromTableToDialog();//TODO: need implement copy data for 'text' and 'file'
	
	console.log("beforeAtachmentEdit()  END");
}

function afterAtachmentAdd() {
	var status = validateAtachment();
	if(status){
		var row = createAtachmentRow();// in table
		initAtachmentTableItems(row);
		readAtachmentFromDialogToTable("add", row);//TODO: need implement copy data for 'text' and 'file'
		removeField();//remove generate field from dialog
		numbered(atachmentTable);//from edit-contact-form-script.js
	}
	
	return status;
}

function afterAtachmentEdit(rowNumber) {
	var status = validateAtachment();
	if (status) {
		var row = atachmentTable.getElementsByTagName("tr")[rowNumber];
		row.style.backgroundColor = oldColor;
		readAtachmentFromDialogToTable("edit", row);
		removeField();
		numbered(atachmentTable);//from edit-contact-form-script.js
	}
	
	return status;
}

function validateAtachment() {
	console.log("validateAtachment() BEGIN");
	
	var PATTERN;
	var value;
	
	value = dialogAtachmentName.value;

	if (!/\.(?:mp3|wav|og(?:g|a)|flac|midi?|rm|aac|wma|mka|ape)$/i.test(value) && // not audio
		!/\.(?:z(?:ip|[0-9]{2})|r(?:ar|[0-9]{2})|jar|bz2|gz|tar|rpm)$/i.test(value) && // archive
		!/\.(?:jp(?:e?g|e|2)|gif|png|tiff?|bmp|ico)$/i.test(value) && // not image
		!/\.(?:mpeg|ra?m|avi|mp(?:g|e|4)|mov|divx|asf|qt|wmv|m\dv|rv|vob|asx|ogm)$/i.test(value) // not video
	) {
		alert("Atachment Name. Length 1-50 characters. Can be only audio, archive, image or video");
		return false;
	}
	
	console.log("\t atachment name: " + value + " is valid!");

	PATTERN = /^[\w!?\s,.\n\<\>\[\]\"\'\(\):;]{0,100}$/;
	value = dialogAtachmentComment.value;
	if (!PATTERN.test(value)) {
		alert("Atachment Comment. Max Length 100 characters.");
	}

	console.log("\t atachment comment: " + value + " is valid!");

	console.log("validateAtachment() END");
	return true;
}

function createAtachmentRow() {
	console.log("createAtachmentRow BEGIN");

	var allQuantity = atachmentTable.getElementsByTagName("tr").length;
	var nextNumber = allQuantity - 1;
	
	console.log("\tquantity rows: " + allQuantity);
	
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
	atachmentId.setAttribute("value", null);
	console.log("\tset 'null' id value");
	
	addedRow.getElementsByClassName("edit-atachment")[0].setAttribute("id", "edit-atachment-icon" + nextNumber);
	addedRow.getElementsByClassName("edit-atachment")[0].setAttribute("onclick", "atachmentEditPopupShow('edit', " + nextNumber + ");");

	console.log("createAtachmentRow END");
	return addedRow;
}

function initAtachmentTableItems(row) {
	console.log("initAtachmentTableItems() BEGIN");
	console.log("\trow: " + row);
	
	tableAtachmentName = row.getElementsByClassName("table-atachment-name")[0];
	tableAtachmentComment = row.getElementsByClassName("table-atachment-comment")[0];
	
	console.log("initAtachmentTableItems() END");
}

function initAtachmentDialogItems() {
	console.log("initAtachmentDialogItems() BEGIN");
	
	dialogAtachmentName = document.getElementById("atachment-file");
	dialogAtachmentComment = document.getElementById("dialog-atachment-comment");

	console.log("initAtachmentDialogItems() BEGIN");
}

function readAtachmentFromDialogToTable(actionType, row) {
	console.log("readAtachmentFromDialogToTable() BEGIN");
	console.log("\t actionType: " + actionType);
	console.log("\t row: " + row);
	
	switch (actionType) {
	case "add":
		var file = dialogAtachmentName.cloneNode(true);
		file.setAttribute("class", "hidden");
		
		var originName = row.getElementsByClassName("table-origin-atachment-name")[0];
		tableAtachmentName.parentNode.parentNode.appendChild(file);
		originName.value = file.value.trim();
	case "edit":
		tableAtachmentComment.textContent = dialogAtachmentComment.value.trim();
		tableAtachmentName.textContent = dialogAtachmentName.value.trim();
		break;
	}

	console.log("\t comment: " + dialogAtachmentComment.value.trim());
	console.log("\t file name(original): " + dialogAtachmentName.value.trim());
	console.log("readAtachmentFromDialogToTable() END");
}

function readAtachmentFromTableToDialog() {
	console.log("readAtachmentFromTableToDialog() BEGIN");
	
	dialogAtachmentName.value = tableAtachmentName.textContent.trim();
	dialogAtachmentComment.value = tableAtachmentComment.textContent.trim();

	console.log("readAtachmentFromTableToDialog() END");
}