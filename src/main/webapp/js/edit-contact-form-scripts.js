/*
 * Global section
 */
var QUANTITY_ROWS_PER_HEADER = 2;
var selectAllAtachmentsButton;
var selectAllPhonesButton;
var self;

window.onload = init;

/*
 * Main function. Invoke after loaded document. Configure click event for
 * elements;
 * 
 * Look file js/parts/edit-phone-dialog-scripts.js It contain logic for add/edit
 * phone
 * 
 */
function init() {
	document.getElementById("delete-phone").onclick = deletePhones;
	document.getElementById("delete-atachment").onclick = deleteAtachments;
	
	selectAllPhonesButton = document.getElementById("select-all-phones");
	selectAllPhonesButton.setAttribute("onclick", "selectAllPhones(true);");

	selectAllAtachmentsButton = document.getElementById("select-all-atachments");
	selectAllAtachmentsButton.setAttribute("onclick", "selectAllAtachments(true);");
	
	phoneTable = document.getElementById("phone-table").tBodies[0];
	atachmentTable = document.getElementById("atachment-table").tBodies[0];

	document.getElementById("edit-contact-form").onsubmit = copyData;
	document.getElementById("for-test").onclick = clickListener;
	
	var avatarBox = document.getElementById("avatar");
	var newAvatar = document.getElementById("hidden-pick-avatart");
	
	avatarBox.addEventListener('click', function() {
		newAvatar.click();
	}, false);

	newAvatar.addEventListener('change', function(e) {
		if (typeof FileReader == "undefined") {
			return true;
		}
		var file = e.target.files[0];
		if ((/image/i).test(file.type)) {
			var reader = new FileReader();

			avatarBox.removeAttribute("class");
			avatarBox.setAttribute("class", "avatar");
			avatarBox.innerHTML = "";
			var imgTag = document.createElement("img");
			avatarBox.appendChild(imgTag);

			reader.onload = (function(img) {
				return function(evt) {
					img.src = evt.target.result;
				};
			}(imgTag));
			reader.readAsDataURL(file);
		}
	}, false);

	document.getElementById("hidden-deleting-atachment-ids").value = "";
	document.getElementById("hidden-deleting-phone-ids").value = "";
	
	selectAllAtachments(false);
	selectAllPhones(false);

	initAtachment();
	initPhone();
};

/*
 * Stub listener
 */
function clickListener() {
	console.log("validatePhone() BEGIN");

	var PATTERN;
	var value;
	
//	PATTERN = /^[\w!?\s,.]{1,30}$/;
//	value = "Some gratulation!";
//	if (!PATTERN.test(value)) {
//		alert("Email subject must be more 1 and less 50. Use only characters and digits.");
//		return false;
//	}
//	
//	console.log("\t email subject: " + value + " is valid!");

	PATTERN = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	value = "email1@mail.com; email2@mail.com; em#ail3@mail.com ";
	var tokens = value.split(';');
	for(var i = 0; i < tokens.length; ++i){
		var token = tokens[i].trim();
		if (!PATTERN.test(token)) {
			alert(token + " incorrect");
		}
	}
	
	
//	if (!PATTERN.test(value)) {
//		alert("Phone Number. Format: NNN NN NN. Can be used only digits.");
//		return false;
//	}
	
	console.log("\t emails: " + value + " is valid!");

//	PATTERN = /^[\w!?\s,.\n\<\>\[\]\"\'\(\):;]+$/;
//	value = "asdasdasd1212  sad a dqw 1 2! a das )) d## 2";
//	if (!PATTERN.test(value)) {
//		alert("Text contains illegal characters");
//		return false;
//	}
//
//	console.log("\t message: " + value + " is valid!");
	console.log("validatePhone() END");
	return true;
}




/*
 * Put all updated data to 'hidden' input before submit to server.
 * 
 * Submit form listener. Copy data from tables in the field and write id for
 * deleting. Parameters: table - target table for process. suffixName:
 * suffix for search statement, maybe: 'phone', 'atachment'
 */
function prepareBeforeSubmit(table, suffixName) {
	console.log("prepareBeforeSubmit BEGIN");
	console.log("\tsuffixName: " + suffixName);
	
	var rows = table.getElementsByTagName("tr");
	for (var y = 1; y < rows.length; ++y) {//skip header
		var columns = rows[y].getElementsByTagName("td");
		for (var x = 2; x < columns.length - 1; ++x) {//skip first 2 column(number, checkbox) and last(icon)
			var cellValue = columns[x].getElementsByTagName("span")[0].textContent.trim();
			var input = columns[x].getElementsByTagName("input")[0];
			input.value = cellValue;
		}
	}
	
	console.log("prepareBeforeSubmit END");
}

/*
 * Special function for prepate atachment name
 * It fill 'changed-atachment-name' and 'atachment-name' field
 * For 'changed-atachment-name' execute contact 'salt' + 'changed-name(from from span[class=table-atachment-name])'
 * This need to be compare old and new name on server   
 */
function specialPrepareAtachmentNameBeforeSubmit(){
	console.log("prepareAtachmentNameBeforeSubmit() BEGIN")
	
//	var rows = atachmentTable.getElementsByTagName("tr");
//	for(var y = 1; y < rows.length - 1; ++y){//skip header(first) and template row(last)
//		var row = rows[y];
//		var simpleName = row.getElementsByClassName("table-atachment-name")[0].textContent.trim();
//		var salt = row.getElementsByClassName("table-atachment-name-salt")[0].value.trim();
//		var changedName = row.getElementsByClassName("table-changed-atachment-name")[0];
//		changedName.value = salt + simpleName;
//		
//		console.log("\tsimpleName: " + simpleName + ", salt: " + salt + ", changedName: " + changedName.value);
//	}
	
	console.log("prepareAtachmentNameBeforeSubmit() END")
}

function copyData() {
	prepareBeforeSubmit(phoneTable, "phone");
	prepareBeforeSubmit(atachmentTable, "atachment");
	
	specialPrepareAtachmentNameBeforeSubmit();
	
	phoneTemplate.parentNode.removeChild(phoneTemplate);
	atachmentTemplate.parentNode.removeChild(atachmentTemplate);
}

/*
 * Initialize pop-up window for edit phone number
 */
function mouseDownListener(e) {
	e = e || window.event;

	var coords = getPosition(self);
	var shiftX = e.pageX - coords[0];
	var shiftY = e.pageY - coords[1];

	function moveAt(e) {
		self.style.left = e.pageX - shiftX + 'px';
		self.style.top = e.pageY - shiftY + 'px';
	}

	document.onmousemove = function(e) {
		e = e || window.event;
		moveAt(e);
	};

	this.onmouseup = function() {
		document.onmousemove = self.onmouseup = null;
	};
};

function dragStartListener() {
	return false;
};

/*
 * Return all checkboxes in table
 */
function getCheckBoxesFromTable(table){
	var counter = 0;
	var checkboxes = [];
	var inputs = table.getElementsByTagName("input");
	for (var i = 0; i < inputs.length; ++i) {
		if (inputs[i].getAttribute("type") == "checkbox") {
			 checkboxes[counter++] = inputs[i];
		}
	}
	
	return checkboxes;
}

/*
 * Check box checked change
 */
function selectAll(table, state) {
	var checkboxes = getCheckBoxesFromTable(table);
	for (var i = 0; i < checkboxes.length - 1; ++i) {
		checkboxes[i].checked = state;
	}
};

/*
 * Numberd rows
 */
function numbered(table) {
	var preffix = table == phoneTable ? "phone" : "atachment";
	var rows = table.getElementsByTagName("tr");
	for (var i = 1; i < rows.length - 1; ++i) {// skip header(first) and template(last)
		var row = rows[i];
		if (row.hasAttribute("class")) {
			continue;
		}
		row.getElementsByTagName("span")[0].textContent = i;
		
		var iconEdit = row.getElementsByTagName("img")[0];
		iconEdit.id = iconEdit.id.substring(0, iconEdit.id.lastIndexOf("-") + 1) + i;
		iconEdit.setAttribute("onclick", preffix + "EditPopupShow('edit', " + i + ");");
	}
}


/*
 * Enable checkboxes in atachments table
 */
function selectAllAtachments(state) {
	console.log("selectAllAtachments() BEGIN");
	console.log("\tstate: " + state);
	
	selectAll(atachmentTable, state);
	if (state) {
		selectAllAtachmentsButton.setAttribute("onclick", "selectAllAtachments(false);");
	} else {
		selectAllAtachmentsButton.setAttribute("onclick", "selectAllAtachments(true);");
	}
	
	console.log("selectAllAtachments() END");
};

/*
 * Enable checkboxes in phone table
 */
function selectAllPhones(state) {
	selectAll(phoneTable, state);
	if (state) {
		selectAllPhonesButton.setAttribute("onclick", "selectAllPhones(false);");
	} else {
		selectAllPhonesButton.setAttribute("onclick", "selectAllPhones(true);");
	}
};

/*
 * Delete rows from table
 */
function deletePhones() {
	console.log("deletePhones() BEGIN");
	deleteSelectedFrom(phoneTable, "phone");
	console.log("deletePhones() END");
}

function deleteAtachments() {
	console.log("deleteAtachments() BEGIN");
	deleteSelectedFrom(atachmentTable, "atachment");
	console.log("deleteAtachments() END");
}

function deleteSelectedFrom(table, suffixName) {
	console.log("deleteSelectedFrom() BEGIN");
	console.log("\tsuffixName: " + suffixName);
	var deletingRows = document.getElementById("hidden-deleting-" + suffixName + "-ids");
	var rows = table.getElementsByTagName("tr");
	for (var y = 1; y < rows.length - 1; ++y) {// skip header(first) and template(last)
		var inputs = rows[y].getElementsByTagName("input");
		for (var x = 0; x < inputs.length; ++x) {
			if (inputs[x].getAttribute("type") == "checkbox") {
				if (inputs[x].checked) {
					var itemId = rows[y].getElementsByClassName("table-" + suffixName + "-id")[0];
					console.log("itemId: " + itemId.value);
					if (itemId.value.indexOf("-1") == -1) {
						deletingRows.value += itemId.value + " ";
						console.log("id: " + itemId.value + " - add to delete list");
					}
					rows[y].classList.add("remove-me");
				}
			}
		}
	}

	var hasMoreToDelete = false;
	do {
		hasMoreToDelete = false;
		var rows = table.getElementsByTagName("tr");
		for (var y = 0; y < rows.length; ++y) {
			var row = rows[y];
			var css = row.className;
			if (css.indexOf("remove-me") != -1) {
				row.parentNode.removeChild(row);
				hasMoreToDelete = true;
			}
		}
		console.log("\tloop delete");
	} while (hasMoreToDelete);
	
	numbered(phoneTable);
	numbered(atachmentTable);
	
	console.log("deleteSelectedFrom() END");
}

/*
 * Return left-top cernel position of element
 */
function getPosition(element) {
	console.log("getPosition of element BEGIN");
	
	var x = 0;
	var y = 0;

	while (true) {
		x += element.offsetLeft;
		y += element.offsetTop;
		if (element.offsetParent === null) {
			break;
		}
		element = element.offsetParent;
	}

	console.log("\t x: " + x + ", y: " + y);
	console.log("getPosition of element END");
	return [ x, y ];
}

/*
 * Return size of element
 */
function getSize(element) {
	console.log("getSize of element BEGIN");
	
	var height = element.getBoundingClientRect().bottom
			- element.getBoundingClientRect().top;

	var width = element.getBoundingClientRect().right
			- element.getBoundingClientRect().left;

	console.log("\t width: " + width + ", height: " + height);
	console.log("getSize of element END");
	
	return [ width, height ];
}

/*
 * Clear all text
 */
function clearText(container) {
	console.log("clearText of container BEGIN");
	
	var texts = container.getElementsByTagName("input");
	for (var i = 0; i < texts.length; ++i) {
		if (texts[i].type == "text") {
			texts[i].value = "";
		}
	}
	var areas = container.getElementsByTagName("textarea");
	for (var i = 0; i < areas.length; ++i) {
		areas[i].value = "";
	}
	
	console.log("clearText of container END");
}
