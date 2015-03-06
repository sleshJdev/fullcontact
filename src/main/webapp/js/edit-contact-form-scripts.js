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

//	document.getElementById("edit-contact-form").onsubmit = copyData;
	document.getElementById("for-test").onclick = clickListener;
	document.getElementById("avatar").onclick = pickAvatar;

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
	var NAME_PATTERN = /^[A-Za-z0-9 ]{3,20}$/;
	var name = "hell#o";
	if (!NAME_PATTERN.test(name)) {
		alert("You valid Name ." + name);
	}
}

function pickAvatar(){
	var fileInput = document.getElementById("hidden-pick-avatart");
	fileInput.click();
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
	console.log("suffixName: " + suffixName);
	
	var deletingRows = document.getElementById("hidden-deleting-" + suffixName + "-ids");
	deletingRows.value = "";
	
	var rows = table.getElementsByTagName("tr");

	for (var y = 1; y < rows.length; ++y) {
		var css = rows[y].className;
		if (css.indexOf("delete") != -1) {
			var phoneId = rows[y].getElementsByClassName("table-" + suffixName + "-id")[0];
			deletingRows.value += phoneId.value + " ";
			continue;
		}

		var columns = rows[y].getElementsByTagName("td");
		for (var x = 2; x < columns.length - 1; ++x) {
			var cellValue = columns[x].getElementsByTagName("span")[0].textContent.trim();
			var input = columns[x].getElementsByTagName("input")[0];
			input.value = cellValue;
		}
	}
	
	console.log("prepareBeforeSubmit END");
}

function copyData() {
	prepareBeforeSubmit(phoneTable, "phone");
	prepareBeforeSubmit(atachmentTable, "atachment");
	
	phoneTable.removeChild(phoneTemplate);
	atachmentTable.removeChild(atachmentTemplate);
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
	for (var i = 0; i < checkboxes.length; ++i) {
		checkboxes[i].checked = state;
	}
};

/*
 * Numberd
 */
function numbered(table) {
	var counter = 0;
	var rows = table.getElementsByTagName("tr");
	for (var i = 1; i < rows.length - 1; ++i) {// skip header(first) and template(last)
		if (rows[i].hasAttribute("class")) {
			continue;
		}
		rows[i].getElementsByTagName("span")[0].textContent = ++counter;
	}
	return counter;
}


/*
 * Enable checkboxes in atachments table
 */
function selectAllAtachments(state) {
	console.log("selectAllAtachments BEGIN");
	console.log("state: " + state);
	
	selectAll(atachmentTable, state);
	if (state) {
		selectAllAtachmentsButton.setAttribute("onclick", "selectAllAtachments(false);");
	} else {
		selectAllAtachmentsButton.setAttribute("onclick", "selectAllAtachments(true);");
	}
	
	console.log("selectAllAtachments END");
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
	console.log("deletePhones BEGIN");
	deleteSelectedFrom(phoneTable, "phone");
	console.log("deletePhones END");
}

function deleteAtachments() {
	console.log("deleteAtachments BEGIN");
	deleteSelectedFrom(atachmentTable, "atachment");
	console.log("deleteAtachments END");
}

function deleteSelectedFrom(table, suffixName) {
	var deletingRows = document.getElementById("hidden-deleting-" + suffixName + "-ids");
	var rows = table.getElementsByTagName("tr");
	for (var y = 1; y < rows.length - 1; ++y) {// skip header(first) and
												// template(last)
		var inputs = rows[y].getElementsByTagName("input");
		for (var x = 0; x < inputs.length; ++x) {
			if (inputs[x].getAttribute("type") == "checkbox") {
				if (inputs[x].checked) {
					var itemId = rows[y].getElementsByClassName("table-" + suffixName + "-id")[0];
					if (itemId.value.indexOf("-1") == -1) {
						if (deletingRows.value.indexOf(itemId.value) == -1) {
							deletingRows.value += itemId.value + " ";
						}
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
		console.log("loop delete");
	} while (hasMoreToDelete);
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
