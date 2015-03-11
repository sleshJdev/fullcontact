<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/form-style.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/search-contact-form-style.css" />

<script src="${pageContext.request.contextPath}/js/datetimepicker.js"></script>

<div id="content">
	<form id="search-form" action="search?x=search" method="post" class="smart-green">
	    <h1>SEARCH CONTACT PAGE 
	        <span>Please fill texts in the fields for search</span>
	    </h1>
    	<label>
        <input id="first-name" type="text"  name="first-name" 
							        placeholder="First Name For Search" pattern="[A-Za-z0-9]{0,50}"
							        maxlength="50" 
							        title="First name must be from 3 to 50 characters. Can be used only letters and digits." />
	    </label>
	    <label>
	        <input id="middle-name" type="text" name="middle-name" placeholder="Middle Name For Search"
							        pattern="[A-Za-z0-9]{0,50}" 
							        maxlength="50" 
							        title="Midle name must be from 3 to 50 characters. Can be used only letters and digits." /> 
	    </label>
	    <label>
	        <input id="last-name" type="text" name="last-name" placeholder="Last Name For Search" 
							        pattern="[A-Za-z0-9]{0,50}" 
							        maxlength="50" 
							        title="Last name must be from 3 to 50 characters. Can be used only letters and digits." /> 
	    </label>
	    <div class="line">
	        	<div class="date-pick">
	        		<a href="javascript:DateTimePick('date-of-birth')">
					 <img src="${pageContext.request.contextPath}/images/icons/calendar.gif" alt="Pick a date">
					</a>
	        	</div>
	        	<div class="date-input">
	        		<jsp:useBean id="currentDate" class="java.util.Date"/>
					<jsp:setProperty name="currentDate" property="time" value="${currentDate.time + 86400000}"/>
	        		<input type="Text" id="date-of-birth" 
					   name="date-of-birth" value= "<fmt:formatDate value="${currentDate }" pattern="yyyy-MM-dd"/>"
					   maxlength="30" placeholder="Your Date Of Birth" 
				       required="required" readonly="readonly"/>
	        	</div>
	        	<div class="date-radio">
		    		<input type=radio name="age-search-type" value="less" checked="checked"><span>less</span><br>
					<input type=radio name="age-search-type" value="more"><span>more</span>
		    	</div>
	    </div>
   		<div id="oneline" class="oneline">
   			<div class="div1">
				<div>
					<span>SEX</span>
				</div>
				<select name="sex">
					<option value="" selected="selected">Not know</option>
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
					<option value="" selected="selected">Not know</option>
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
					<option value="" selected="selected">Not know</option>
					<c:forEach var="item" items="${familyStatusesList}">
						<option value="<c:out value="${item.value }"/>">
							<c:out value="${item.value }" />
						</option>
					</c:forEach>
				</select>
			</div>
   		</div>
	    <label>
	        <input id="web-site" type="text" name="web-site" placeholder="Web Site Address For Search" 
									pattern="^[a-zA-Z0-9\-\.]+\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$" 
							        maxlength="30" 
							        title="Address of your web site incorrect format"/>
	    </label>
	    <label>
	        <input id="email" type="email" name="email-address" placeholder="Email Address For Search" 
							        maxlength="50" 
							        title="Your Email addreess. Max length 50 charaters"/> 
	    </label>
	    <label>
	        <input id="current-employment" type="text" name="current-employment" placeholder="Employment For Search" 
	        						pattern="[A-Za-z0-9]{0,50}" 
									maxlength="50" 
							        title="Your Current Employement. Max Length 30 characters. Can be used only letters and digits."/> 
	    </label>
	     <label>
	        <input id="country" type="text" name="country" placeholder="Country For Search" 
	        						pattern="[A-Za-z0-9]{0,30}" 
	        						maxlength="30" 
	        						title="Your Coutry. Max Length 30 characters. Can be used only letters and digits."/>
	    </label>    
	     <label>
	        <input id="city" type="text" name="city" placeholder="City For Search" 
	        						pattern="[A-Za-z0-9]{0,50}" 
	        						maxlength="30" 
	        						title="Your City. Max Length 30 characters. Can be used only letters and digits."/>
	    </label>    
	     <label>
	        <input id="street" type="text" name="street" placeholder="Street For Search" 
	        						pattern="[A-Za-z0-9]{0,50}" 
	        						maxlength="50" 
	        						title="Your Coutry. Max Length 50 characters. Can be used only letters and digits."/>
	    </label>    
	     <label>
	        <input id="house" type="text" name="house" placeholder="House For Search" 
									pattern="[A-Za-z0-9]{0,10}" 
	        						maxlength="10" 
	        						title="Your House. Max Length 10 characters. Can be used only letters and digits."/>
	    </label>    
	     <label>
	        <input id="block" type="text" name="block" placeholder="Block For Search" 
									pattern="[A-Za-z0-9]{0,10}" 
	        						maxlength="10" 
	        						title="Your Block. Max Length 10 characters. Can be used only letters and digits."/>
	    </label>    
	     <label>
	        <input id="apartment" type="text" name="apartment" placeholder="Apartment For Search" 
									pattern="[A-Za-z0-9]{0,10}" 
	        						maxlength="10" 
	        						title="Your Apartment. Max Length 10 characters. Can be used only letters and digits."/>
	    </label>    
	    <label>
	        <input id="city-index" type="text" name="city-index" placeholder="Index For Search" 
									pattern="[0-9]{0,10}"
	        						maxlength="10" 
	        						title="Your City Index. Max Length 10 characters. Can be used only digits."/>
	    </label>
	     <label>
	        <span>&nbsp;</span>
	        <input type="submit" class="button" value="Search" />
	    </label>    
	    <div class="clear"></div>
</form>
</div>
