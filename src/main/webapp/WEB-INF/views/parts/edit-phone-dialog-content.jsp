<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script src="${pageContext.request.contextPath}/js/parts/edit-phone-dialog-scripts.js"></script>  
  
<div id="dialog-phone-form">
	<form class="smart-green form" action="#" method="get">
		<div class="close-icon-box"></div>
		<h3 id="dialog-phone-hook">EDIT PHONE</h3>
		<label>
			<span>Country code:</span>
			<input type="text" id="dialog-phone-country-code" placeholder="Country code">
		</label>
		<label>
			<span>Operator code:</span>
			<input type="text" id="dialog-phone-operator-code" placeholder="Operator code">
		</label>
		<label>
			<span>Phone number:</span>
			<input type="text"  id="dialog-phone-value" placeholder="Phone number">
		</label>
		<label>
			<span>Phone type:</span> 
			<select id="dialog-phone-type">
				<option value="Mobile">Mobile</option>
				<option value="Home">Home</option>
			</select>
		</label>
		<label>
			<span>Comment:</span>
			<textarea id="dialog-phone-comment" class="input" name="dialog-phone-comment" rows="7" cols="30"></textarea>
		</label>
		<label>
			<input id="dialog-phone-action-button" class="button" value="Save" type="button">
		</label>
	</form>
</div>