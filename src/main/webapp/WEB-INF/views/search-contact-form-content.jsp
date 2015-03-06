<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/form-style.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/search-contact-form-style.css" />

<div id="content">
	<form id="search-form" action="search?x=search" method="post" class="smart-green">
	    <h1>SEARCH CONTACT PAGE 
	        <span>Please fill all the texts in the fields.</span>
	    </h1>
    	<label>
        <input id="first-name" type="text" name="first-name" placeholder="First Name  For Search" maxlength="50"/>
	    </label>
	    <label>
	        <input id="middle-name" type="text" name="middle-name" placeholder="Middle Name  For Search" maxlength="50" />
	    </label>
	    <label>
	        <input id="last-name" type="text" name="last-name" placeholder="Last Name  For Search" maxlength="50" />
	    </label>
	    <div class="line">
	        <div class="input-div">
	        	<input id="date-of-birth" type="text" name="date-of-birth" 
	        		   placeholder="Date Of Birth For Search" maxlength="30" />
	        </div>
	    	<div class="radio-div">
	    		<INPUT type=radio name="age-search-type" value="l"><span>less</span><br>
				<INPUT type=radio name="age-search-type" value="m"><span>more</span>
	    	</div>
	    </div>
   		<div id="oneline" class="oneline">
   			<div class="div1">
		        <div><span>SEX</span></div>
		        <select name="sex">
		        	<option value="m">Male</option>
		        	<option value="f">Female</option>
		        </select>
	    	</div>
		    <div class="div2">
		        <div><span>NATIONALITY</span></div>
		    	 <select name="nationality">
		        <option value="BY">Belaruss</option>
		        <option value="RU">Russian</option>
		        <option value="FR">France</option>
		        <option value="UA">Ukraine</option>
		        </select>
		    </div>
		    <div class="div3">
		        <div><span>FAMILY STATUS </span></div>
		        <select name="family-status">
			        <option value="m">Married</option>
			        <option value="d">Divorced</option>
			        <option value="w">Widower</option>
			        <option value="s">Single</option>
			        </select>
		    </div>
   		</div>
	    <label>
	        <input id="web-site" type="text" name="web-site" placeholder="Web Site  For Search" maxlength="30" />
	    </label>
	    <label>
	        <input id="email" type="email" name="email" placeholder="Email  For Search" maxlength="30"/>
	    </label>
	    <label>
	        <input id="current-employment" type="text" name="current-employment" placeholder="Employment  For Search" maxlength="50" />
	    </label>
	     <label>
	        <input id="country" type="text" name="country" placeholder="Country  For Search" maxlength="30" />
	    </label>    
	     <label>
	        <input id="city" type="text" name="city" placeholder="City  For Search" maxlength="30" />
	    </label>    
	     <label>
	        <input id="address" type="text" name="address" placeholder="Address  For Search" maxlength="50" />
	    </label>    
	    <label>
	        <input id="city-index" type="text" name="city-index" placeholder="Index  For Search" maxlength="20" />
	    </label> 
	     <label>
	        <span>&nbsp;</span>
	        <input type="submit" class="button" value="Search" />
	    </label>    
	    <div class="clear"></div>
</form>
</div>
