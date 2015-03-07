<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/form-style.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/add-contact-form-style.css" />

<script src="${pageContext.request.contextPath}/js/datetimepicker.js"></script>

<div id="content">
	<form action="add?x=add" method="post" class="smart-green">
	    <h1>ADD NEW CONTACT FORM
	        <span>Please fill all the texts in the fields.</span>
	    </h1>
    	<label>
        <span>First Name<span class="required">*</span> :</span>
        <input id="first-name" type="text"  name="first-name" 
							        placeholder="Your First Name" pattern="[A-Za-z0-9]{3,50}"
							        maxlength="50" 
							        title="First name must be from 3 to 50 characters. Can be used only letters and digits." 
							        required="required"/>
	    </label>
	    <label>
	        <span>Middle Name<span class="required">*</span> :</span>
	        <input id="middle-name" type="text" name="middle-name" placeholder="Your Middle Name"
							        pattern="[A-Za-z0-9]{3,50}" 
							        maxlength="50" 
							        title="Midle name must be from 3 to 50 characters. Can be used only letters and digits." 
							        required="required"/>
	    </label>
	    <label>
	        <span>Last Name<span class="required">*</span> :</span>
	        <input id="last-name" type="text" name="last-name" placeholder="Your Last Name" 
							        pattern="[A-Za-z0-9]{3,50}" 
							        maxlength="50" 
							        title="Last name must be from 3 to 50 characters. Can be used only letters and digits." 
							        required="required"/>
	    </label>
	    <label>
			<span>Date of birth<span class="required">*</span> :</span>
			<a href="javascript:DateTimePick('date-of-birth')">
				 <img src="${pageContext.request.contextPath}/images/icons/calendar.gif"
					width="16" height="16" border="0" style="margin: 5px 0 0 50px;"
					alt="Pick a date">
			</a>
			<input type="Text" id="date-of-birth" 
				   name="date-of-birth" value="${contact.dateOfBirth }"  
				   maxlength="30" placeholder="Your Date Of Birth" 
			       required="required" readonly="readonly"/>
	    </label>
   		<div class="oneline">
			<div class="div1">
				<div>
					<span>SEX</span>
				</div>
				<select name="sex">
					<c:forEach var="item" items="${sexesList}">
						<option value="<c:out value="${item.value }"/>">
							<c:out value="${item.value }" />
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="div2">
				<div>
					<span>NATIONALITY</span>
				</div>
				<select name="nationality">
					<c:forEach var="item" items="${nationalitiesList}">
						<option value="<c:out value="${item.value }"/>">
							<c:out value="${item.value }" />
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="div3">
				<div>
					<span>FAMILY STATUS</span>
				</div>
				<select name="family-status">
					<c:forEach var="item" items="${familyStatusesList}">
						<option value="<c:out value="${item.value }"/>">
							<c:out value="${item.value }" />
						</option>
					</c:forEach>
				</select>
			</div>
		</div>
	    <label>
	        <span>Web Site :</span>
	        <input id="web-site" type="text" name="web-site" placeholder="Your Web Site Address" 
									pattern="^[a-zA-Z0-9\-\.]+\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$" 
							        maxlength="30" 
							        title="Address of your web site incorrect format"/>
	    </label>
	    <label>
	        <span>Email :</span>
	        <input id="email" type="email" name="email-address" placeholder="Your Email Address" 
							        maxlength="50" 
							        title="Your Email addreess. Max length 50 charaters"/> 
	    </label>
	    <label>
	        <span>Current employment :</span>
	        <input id="current-employment" type="text" name="current-employment" placeholder="Your Employment" 
	        						pattern="[A-Za-z0-9]{0,50}" 
									maxlength="50" 
							        title="Your Current Employement. Max Length 30 characters. Can be used only letters and digits."/> 
	    </label>
	     <label>
	        <span>County :</span>
	        <input id="country" type="text" name="country" placeholder="Your Country" 
	        						pattern="[A-Za-z0-9]{0,30}" 
	        						maxlength="30" 
	        						title="Your Coutry. Max Length 30 characters. Can be used only letters and digits."/>
	    </label>    
	     <label>
	        <span>City :</span>
	        <input id="city" type="text" name="city" placeholder="Your City" 
	        						pattern="[A-Za-z0-9]{0,50}" 
	        						maxlength="30" 
	        						title="Your City. Max Length 30 characters. Can be used only letters and digits."/>
	    </label>    
	     <label>
	        <span>Street :</span>
	        <input id="street" type="text" name="street" placeholder="Your Street" 
	        						pattern="[A-Za-z0-9]{0,50}" 
	        						maxlength="50" 
	        						title="Your Coutry. Max Length 50 characters. Can be used only letters and digits."/>
	    </label>    
	     <label>
	        <span>House :</span>
	        <input id="house" type="text" name="house" placeholder="Your House" 
									pattern="[A-Za-z0-9]{0,10}" 
	        						maxlength="10" 
	        						title="Your House. Max Length 10 characters. Can be used only letters and digits."/>
	    </label>    
	     <label>
	        <span>Block :</span>
	        <input id="block" type="text" name="block" placeholder="Your Block" 
									pattern="[A-Za-z0-9]{0,10}" 
	        						maxlength="10" 
	        						title="Your Block. Max Length 10 characters. Can be used only letters and digits."/>
	    </label>    
	     <label>
	        <span>Apartment :</span>
	        <input id="apartment" type="text" name="apartment" placeholder="Your Apartment" 
									pattern="[A-Za-z0-9]{0,10}" 
	        						maxlength="10" 
	        						title="Your Apartment. Max Length 10 characters. Can be used only letters and digits."/>
	    </label>    
	    <label>
	        <span>City Index :</span>
	        <input id="city-index" type="text" name="city-index" placeholder="Your Index" 
									pattern="[0-9]{0,10}"
	        						maxlength="10" 
	        						title="Your City Index. Max Length 10 characters. Can be used only digits."/>
	    </label> 
	     <label>
	        <input type="submit" class="button" value="Add" />
	    </label>    
</form>
</div>