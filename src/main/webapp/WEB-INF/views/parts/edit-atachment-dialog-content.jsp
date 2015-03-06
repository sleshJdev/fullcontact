<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="${pageContext.request.contextPath}/js/parts/edit-atachment-dialog-scripts.js"></script>

<div id="dialog-atachment-form">
	<form class="smart-green form ">
		<div class="close-icon-box"></div>
		<h3 id="dialog-atachment-hook">EDIT ATACHMENT</h3>
		<label id="dialog-field-switcher">
			<span>File Name:</span>
		</label>
		<label>
			<span>Comment:</span>
			<textarea id="dialog-atachment-comment" class="input" name="dialog-atachment-comment" rows="7" cols="30"
							placeholder="Atachment comment" 
					       	maxlength="100" 
					        title="Atachment comment. Max length 100 charaters">
			</textarea>
		</label>
		<label>
			<input id="dialog-atachment-action-button" class="button" value="Save" type="button">
		</label>
	</form>
</div>