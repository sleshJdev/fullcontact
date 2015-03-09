<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/form-style.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/send-email-form-style.css" />
<script src="${pageContext.request.contextPath}/js/send-email-form-scripts.js"></script> 
 
<!-- Contact all emails to line --> 
<c:set var="emailsList"  value="" />
<c:forEach var="email" items="${emails}" varStatus="loop">
	<c:set var="emailsList" value="${emailsList} ${email};"/><%-- ${!loop.last ? '; ' : ''} --%>
</c:forEach>



<div id="content">
	<form id="contact-form" class="smart-green" action="send?x=send" method="post" enctype="multipart/form-data">
		<div>
			<div>
				<h1>SEND EMAIL FORM</h1>
			</div>
			<div id="message-box">
				<h3>MESSAGE BOX</h3>
				<label>
					<span>Subject:</span>
					<input id="name" class="input" name="email-subject" type="text" value="" /><br />
				</label>
				<label>
					<span>Email:</span>
					<input id="emails" class="input" name="email-address" type="text" value="${emailsList}" required="required"/><br />
				</label>
				<label>
					<span>Template</span>
					<select id="template-selector"  name="email-type">
							<option value="plain" selected="selected">plain text</option>
							<option value="congratulation">congratulation</option>
					</select>
				</label>
				<label>
					<span>Message:</span>
					<textarea id="message" class="input" name="email-message" rows="7" cols="50"></textarea> <br />
				</label>
				<label><input id="submit-button" type="submit" value="Send email" /></label>
			</div>
		
			<div id="attachment-box">
				<h3>ATTACHMENTS BOX</h3>
				 <label>
				 	<input type="button" id="add-attachment-field" value="Add"> 
				 </label>
			</div>
		</div>
		<div class="clear"></div>
	</form>
</div>

