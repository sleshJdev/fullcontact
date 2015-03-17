<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- ${pageContext.request.contextPath} -->

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/show-contacts-form-style.css" />

<script src="${pageContext.request.contextPath}/js/show-contacts-form-script.js"></script>

<!-- 
	Boolean
	this flag determines where the data came:
		*from home page or
		*from search page
	need for settings pagination
	see below		
-->
<c:set var="is" value="${isSearchResult }"/>

<div id="content">
	<form id="show-contacts-form" action="delete-contacts" method="get">
		<div class="send-div">
			<a id="send-email-link" href="#">SEND MAIL</a>
		</div>
		<table id="checkable" class="table">
			<tr>
				<td></td>
				<th>
					<div class="menu">
						<input id="delete-selected" type="submit" value="" title="Delete selected">
						<input id="select-all" type="button" value="" title="Select/Deselect all">
					</div>
				</th>
				<th>FULL NAME</th>
				<th>DATE OF BIRTH</th>
				<th>COUNTRY</th>
				<th>CITY</th>
				<th>STREET</th>
				<th>HOUSE</th>
				<th>BLOCK</th>
				<th>APARTMENT</th>
				<th>INDEX</th>
			</tr>

			<c:forEach var="contact" items="${contacts}" varStatus="loop">
				<tr>
					<td>${begin + loop.index + 1}</td>
					<td>
						<c:if test="${contact.id != 1 }">
							<div style="width: 10px; height: 10px; margin: 0 auto;">
								<input name="id" value="${contact.id}" type="checkbox">
							</div>
						</c:if>
					</td>
					<td><a href="edit?&id=${contact.id}">${contact.firstName} ${contact.middleName} ${contact.lastName}</a></td>
					<td>${contact.dateOfBirth}</td>
					<td>${contact.country}</td>
					<td>${contact.city}</td>
					<td>${contact.street}</td>
					<td>${contact.house}</td>
					<td>${contact.block}</td>
					<td>${contact.apartment}</td>
					<td>${contact.cityIndex}</td>
				</tr>
			</c:forEach>
		</table>
		<div class="quantity-contacts status"><span>Total: </span>${total}</div>
	</form>
</div>


<div class="pagination">
	<c:forEach var="page" begin="1" end="${pages}">
		<c:choose>
			<c:when test="${is }">
				<a href="search?x=show&page=${page}" class="page">${page}</a>
			</c:when>
			<c:otherwise>
				<a href="show?page=${page}" class="page">${page}</a>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</div>
