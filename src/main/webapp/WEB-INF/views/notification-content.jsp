<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="content">
	<c:choose>
		<c:when test="${not empty contactsBirthdayMans }">
			<h3>BIRTHDAY MANS</h3>
			<table id="checkable" class="table">
				<tr>
					<td></td>
					<th>FULL NAME</th>
					<th>EMAIL ADDRESS</th>
					<th>DATE OF BIRTH</th>
				</tr>
				<c:forEach var="contact" items="${contactsBirthdayMans}" varStatus="loop">
					<tr>
						<td>${loop.index + 1}</td>
						<td>
							<a href="edit?&id=${contact.id}">
								${contact.firstName} ${contact.middleName} ${contact.lastName}
							</a>
						</td>
						<td><a href="send?&id=${contact.id}">${contact.emailAddress}</a></td>
						<td>${contact.dateOfBirth}</td>
					</tr>
				</c:forEach>
			</table>
		</c:when>
		<c:otherwise>
			<h3>NO NOTIFICATION TODAY</h3>
		</c:otherwise>
	</c:choose>
</div>