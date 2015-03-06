<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/form-style.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/edit-contact-form-style.css" />

<jsp:include page="parts/edit-phone-dialog-content.jsp" />
<jsp:include page="parts/edit-atachment-dialog-content.jsp" />

<script src="${pageContext.request.contextPath}/js/edit-contact-form-scripts.js"></script>
<script src="${pageContext.request.contextPath}/js/datetimepicker.js"></script>

<div id="content">
	<div class="send-div">
		 	<a href="send?&id=${contact.id}">SEND MAIL</a>
	</div>
	<form id="edit-contact-form" class="form smart-green" action="edit?x=save&id=${contact.id}" method="post" enctype="multipart/form-data">
		
		<!-- Hidden inputs -->
		<input type="hidden" name="contact-id" value="${contact.id}">
		<input type="hidden" id="hidden-deleting-phone-ids" name="deleting-phone-ids" >
		<input type="hidden" id="hidden-deleting-atachment-ids" name="deleting-atachmet-ids" >
		<input type="file" id="hidden-pick-avatart" name="hidden-avatar-file" class="avatart-file">	<!--class="avatart-file"  -->	
		
		<h1>EDIT CONTACT</h1>
		<input id="for-test" type="button" value="Click me">
		
		<div class="vertical-tables">
		
			<div class="contact-info">
				<div id="avatar"
					<c:choose>
						<c:when test="${empty contact.avatarPath }">
							class="avatar-default"
						</c:when>
						<c:otherwise>
							class="avatar"
						</c:otherwise>
					</c:choose>>
					<c:if test="${not empty contact.avatarPath }">
						<img alt="avatar" src="${pageContext.request.contextPath}${contact.avatarPath }">
					</c:if>
				</div>
				<table>
					<tr>
						<td><span>First Name</span></td>
						<td> 
							<input type="text" name="first-name" value="${contact.firstName}"
							        placeholder="Your First Name" pattern="[A-Za-z0-9]{1,50}"
							        maxlength="50" 
							        title="First name must be from 1 to 50 characters. Can be used only letters and digits." 
							        required="required"/>
						</td>
					</tr>
					<tr>
						<td><span>Middle Name</span></td>
						<td> 
							<input type="text" name="middle-name" value="${contact.middleName}"
									placeholder="Your Middle Name"
							        pattern="[A-Za-z0-9]{1,50}" 
							        maxlength="50" 
							        title="Midle name must be from 1 to 50 characters. Can be used only letters and digits." 
							        required="required"/>
						
						</td>
					</tr>
					<tr>
						<td><span>Last Name</span></td>
						<td> 
							<input type="text" name="last-name" value="${contact.lastName}"
									placeholder="Your Last Name" 
							        pattern="[A-Za-z0-9]{1,50}" 
							        maxlength="50" 
							        title="Last name must be from 1 to 50 characters. Can be used only letters and digits." 
							        required="required"/>
						</td>
					</tr>
				</table>
			</div>

			<div id="phones">
				
				<!-- Phones table menu -->
			
				<div class="table-menu">
					<div class="pick-all">
						<ul class="ul-inline">
							<li><img 
								alt="Select/Deselect all phones"
								id="select-all-phones" 
								src="${pageContext.request.contextPath}/images/icons/ok.png">
							</li>
						</ul>
					</div>
					<div class="table-title">
						<h2>PHONES</h2>
					</div>
					<div class="menu">
						<ul class="ul-inline">
							<li><img 
								alt="Add phone" 
								id="add-phone"
								src="${pageContext.request.contextPath}/images/icons/add-phone.png">
							</li>
							<li><img 
								alt="Delete selecteds" 
								id="delete-phone"
								src="${pageContext.request.contextPath}/images/icons/delete-phone.png">
							</li>
						</ul>
					</div>
				</div>
				
				<!--Css classes table-* is fictitious. They use for search  -->
				
				<table id="phone-table" class="table">
					<tr>
						<th></th>	
						<th></th>
						<th>COUNTRY CODE</th>
						<th>OPERATOR CODE</th>
						<th>PHONE</th>
						<th>TYPE</th>
						<th>COMMENT</th>
						<th></th>
					</tr>
					<c:forEach varStatus="loop" var="phone" items="${contact.phones }">
						<tr>
							<td>
								<input type="hidden" class="table-phone-id" name="phone-id" value="${phone.id}">
								<span>${loop.index + 1}</span>
							</td>
							<td>
								<div class="center-cell">
									<input type="checkbox">
								</div>
							</td>
							<td>
								<input type="hidden" name="phone-country-code" value="${phone.countryCode }">
								<span class="table-phone-country-code">${phone.countryCode }</span>
							</td>
							<td>
								<input type="hidden" name="phone-operator-code" value="${phone.operatorCode }">
								<span class="table-phone-operator-code">${phone.operatorCode }</span>
							</td>
							<td>
								<input type="hidden" name="phone-value" value="${phone.value }" >
								<span class="table-phone-value">${phone.value }</span>
							</td>
							<td>
								<input type="hidden" name="phone-type" value="${phone.type}" >
								<span class="table-phone-type">${phone.type}</span>
							</td>
							<td>
								<input type="hidden" name="phone-comment" value="${phone.comment }">
								<span class="table-phone-comment">${phone.comment }</span>
							</td>
							<td><div class="center-cell">
									<img 
										alt="Edit" title="Edit" 
										class="edit-phone"
										id="edit-phone-icon${loop.index + 1}"
										src="${pageContext.request.contextPath}/images/icons/edit.png">
								</div>
							</td>
						</tr>
					</c:forEach>
					<tr id="table-phone-row-template" style="visibility:collapse;">
						<td><input type="hidden" class="table-phone-id" name="phone-id"><span></span></td>
						<td><div class="center-cell"> <input type="checkbox"></div></td>
						<td><input type="hidden" name="phone-country-code" value=""><span class="table-phone-country-code"></span></td>
						<td><input type="hidden" name="phone-operator-code" value=""><span class="table-phone-operator-code"></span></td>
						<td><input type="hidden" name="phone-value" value=""><span class="table-phone-value"></span></td>
						<td><input type="hidden" name="phone-type" value="Mobile"><span class="table-phone-type"></span></td>
						<td><input type="hidden" name="phone-comment" value=""><span class="table-phone-comment"></span></td>
						<td><div class="center-cell"> <img alt="Edit" title="Edit" class="edit-phone" src="${pageContext.request.contextPath}/images/icons/edit.png"></div></td>
					</tr>
				</table>
			</div>
			
			<!--Css classes table-* is fictitious. They use for search  -->
			
			<div id="atachments">
			
				<!-- Atachment table menu -->
			
				<div class="table-menu">
					<div class="pick-all">
						<ul class="ul-inline">
							<li>
								<img 
									alt="Select/Deselect all atachments"
									id="select-all-atachments"
									src="${pageContext.request.contextPath}/images/icons/ok.png">
							</li>
						</ul>
					</div>
					<div class="table-title">
						<h2>ATACHMENTS</h2>
					</div>
					<div class="menu">
						<ul class="ul-inline">
							<li><img alt="Add" id="add-atachment"
								src="${pageContext.request.contextPath}/images/icons/add-atachment.png">
							</li>
							<li><img alt="Delete selected" id="delete-atachment"
								src="${pageContext.request.contextPath}/images/icons/delete-atachment.png">
							</li>
						</ul>
					</div>
				</div>
				
				<table id="atachment-table" class="table">
					<tr>
						<th></th>
						<th></th>
						<th>ATACHMENT NAME</th>
						<th>UPLOAD DATE</th>
						<th>COMMENT</th>
						<th></th>
					</tr>
					<c:forEach varStatus="loop" var="atachment" items="${contact.atachments}">
						<tr>
							<td>
								<input type="hidden" class="table-atachment-id" name="atachment-id" value="${atachment.id}">
								<span>${loop.index + 1}</span>
							</td>
							<td><div class="center-cell">
									<input type="checkbox">
								</div>
							</td>
							<td >
								<input type="hidden" name="atachment-name" value="${atachment.name }">
								<span class="table-atachment-name">${atachment.name }</span>
							</td>
							<td >
								<input type="hidden" name="atachment-upload-date" value="${atachment.uploadDate }">
								<span class="table-atachment-upload-date">${atachment.uploadDate }</span>
							</td>
							<td >
								<input type="hidden" name="atachment-comment" value="${atachment.comment }">
								<span class="table-atachment-comment">${atachment.comment }</span>
							</td>
							<td>
								<div class="center-cell">
									<img 
										alt="Edit" title="Edit" class="edit-atachment"
										id="edit-atachment-icon${loop.index + 1}"
										src="${pageContext.request.contextPath}/images/icons/edit.png">
								</div>
							</td>
						</tr>
					</c:forEach>
					<tr id="table-atachment-row-template" style="visibility:collapse;">
						<td><input type="hidden" class="table-atachment-id" name="atachment-id"><span></span></td>
						<td><div class="center-cell"> <input type="checkbox"></div></td>
						<td><input type="hidden" name="atachment-name"><span class="table-atachment-name"></span></td>
						<td><input type="hidden" name="atachment-upload-date"><span class="table-atachment-upload-date"></span></td>
						<td><input type="hidden" name="atachment-comment"><span class="table-atachment-comment"></span></td>
						<td><div class="center-cell"> <img alt="Edit" title="Edit" class="edit-atachment" src="${pageContext.request.contextPath}/images/icons/edit.png"> </div></td>
					</tr>
				</table>
			</div>
		</div>
		
		<div id="contact">
			<table id="contact-info2" class="form">
				<tr>
					<td colspan="2">
						<div class="div1">
							<div>
								<span>SEX</span>
							</div>
							<select name="sex">
								<c:forEach var="item" items="${sexesList}">
									<option value="<c:out value="${item.value }"/>"
										<c:if test="${item.value == contact.sex }"> 
											<c:out value="selected" />
										</c:if>>
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
									<option value="<c:out value="${item.value }"/>"
										<c:if test="${item.value == contact.nationality }"> 
											<c:out value="selected" />
										</c:if>>
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
									<option value="<c:out value="${item.value }"/>"
										<c:if test="${item.value == contact.familyStatus }"> 
											<c:out value="selected" />
										</c:if>>
										<c:out value="${item.value }" />
									</option>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						 <a href="javascript:DateTimePick('date-of-birth')">
						 <img src="${pageContext.request.contextPath}/images/icons/calendar.gif"
							width="16" height="16" border="0"
							alt="Pick a date"></a>
						<span>Date of birth</span>
					</td>
					<td>
						<input type="Text" id="date-of-birth" name="date-of-birth" value="${contact.dateOfBirth }"
							   readonly="readonly">
					</td>
				</tr>
				<tr>
					<td><span>Web Site</span></td>
					<td> 
					
						<input type="text" name="web-site" value="${contact.webSite}"
									placeholder="Your Web Site Address" 
									pattern="^[a-zA-Z0-9\-\.]+\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$" 
							        maxlength="30" 
							        title="Address of your web site incorrect format or you have exceeded the maximum length"/>
					</td>
				</tr>
				<tr>
					<td><span>Email</span></td>
					<td>
						<input type="email" name="email-address" value="${contact.emailAddress}" 
									placeholder="Your Email Address" 
							        maxlength="50" 
							        title="Your Email addreess. Max length 50 charaters"/> 
					</td>
				</tr>
				<tr>
					<td><span>Current Employment</span></td>
					<td>
						<input type="text" name="current-employment" value="${contact.currentEmployment}"
									placeholder="Your Employment" 
	        						pattern="[A-Za-z0-9]{0,50}" 
									maxlength="50" 
							        title="Your Current Employement. Max Length 30 characters. Can be used only letters and digits."/> 
					</td>
				</tr>
				<tr>
					<td><span>Country</span></td>
					<td>
						<input type="text" name="country" value="${contact.country}"
									placeholder="Your Country" 
	        						pattern="[A-Za-z0-9]{0,30}" 
	        						maxlength="30" 
	        						title="Your Coutry. Max Length 30 characters. Can be used only letters and digits."/>
					</td>
				</tr>
				<tr>
					<td><span>City</span></td>
					<td>
						<input type="text" name="city" value="${contact.city}"
									placeholder="Your City" 
	        						pattern="[A-Za-z0-9]{0,50}" 
	        						maxlength="30" 
	        						title="Your City. Max Length 30 characters. Can be used only letters and digits."/>
					</td>
				</tr>
				<tr>
					<td><span>Street</span></td>
					<td>
						<input type="text" name="street" value="${contact.street}" 
									placeholder="Your Street" 
	        						pattern="[A-Za-z0-9]{0,50}" 
	        						maxlength="50" 
	        						title="Your Coutry. Max Length 50 characters. Can be used only letters and digits."/>
					</td>
				</tr>
				<tr>
					<td><span>House</span></td>
					<td>
						<input id="house" type="text" name="house" value="${contact.house}" 
									placeholder="Your House" 
									pattern="[A-Za-z0-9]{0,10}" 
	        						maxlength="10" 
	        						title="Your House. Max Length 10 characters. Can be used only letters and digits."/>
					</td>
				</tr>
				<tr>
					<td><span>Block</span></td>
					<td>
						<input type="text" name="block" value="${contact.block}" 
									placeholder="Your Block" 
									pattern="[A-Za-z0-9]{0,10}" 
	        						maxlength="10" 
	        						title="Your Block. Max Length 10 characters. Can be used only letters and digits."/>
					</td>
				</tr>
				<tr>
					<td><span>Apartment</span></td>
					<td>
						<input id="apartment" type="text" name="apartment" value="${contact.apartment}"
									placeholder="Your Apartment" 
									pattern="[A-Za-z0-9]{0,10}" 
	        						maxlength="10" 
	        						title="Your Apartment. Max Length 10 characters. Can be used only letters and digits."/>
					</td>
				</tr>
				<tr>
					<td><span>Index</span></td>
					<td>
						<input id="city-index" type="text" name="city-index" value="${contact.cityIndex}" 
									placeholder="Your Index" 
									pattern="[0-9]{0,10}"
	        						maxlength="10" 
	        						title="Your City Index. Max Length 10 characters. Can be used only digits."/>
					</td>
				</tr>
			</table>
		</div>
		<input type="submit" id="contact-submit" class="button submit" value="Update">
	</form>
</div>
