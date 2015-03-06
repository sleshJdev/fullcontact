window.onload = function(){
	var addAttachmentButton = document.getElementById('add-attachment-field');
	addAttachmentButton.setAttribute("onclick", "attachListener();");
	attachListener();
};

function deleteAttachmentField(id){
	var node = document.getElementById(id);
	node.parentNode.removeChild(node);
}

function attachListener(){
	var box = document.getElementById('attachment-box');
	var quantity = box.getElementsByClassName('attachment-field').length;
	box.appendChild(createAttachmentFieldHtml(quantity + 1));
}

function createAttachmentFieldHtml(id) {
	var input = document.createElement("input");
	input.setAttribute("type", "file");
	input.setAttribute("id", "file" + id);
	input.setAttribute("name", "file" + id);
	
	var deleteIcon = document.createElement("div");
	deleteIcon.setAttribute("class", "close-icon-box");
	deleteIcon.setAttribute("onclick", "deleteAttachmentField('box" + id + "');")
	
	var div = document.createElement("div");
	div.setAttribute("id", "box" + id);
	div.setAttribute("class", "attachment-field");
	
	div.appendChild(deleteIcon);
	div.appendChild(input);
	
	return div;
}

