<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/letters-style.css" />

<script src="${pageContext.request.contextPath}/js/letters-script.js"></script>

<div id="content">
	<form id="delete-emails-form" action="delete-letters" method="get">
		<c:choose>
			<c:when test="${not empty emails }">
				<h3>LETTERS</h3>
				<table id="checkable" class="table">
					<tr>
						<td></td>
						<td>
							<div class="menu">
								<input id="delete-selected" type="submit" value="" title="Delete selected">
								<input id="select-all" type="button" value="" title="Select/Deselect all">
							</div>
						</td>
						<td>FROM</td>
						<td>TO</td>
						<td>DATE</td>
						<td>SUBJECT</td>
						<td>MESSAGE</td>
						<td>ATACHMENTS</td>
					</tr>
					<c:forEach var="email" items="${emails}" varStatus="loop">
						<c:set var="s" value="${email.sender }"/>
						<c:set var="as" value="${email.atachments }"/> 
						<c:set var="rs" value="${email.receivers }"/>
						<tr>
							<td>${loop.index + 1}</td>
							<td>
								<div style="width: 10px; height: 10px; margin: 0 auto;">
									<input name="id" value="${email.id}" type="checkbox">
								</div>
							</td>
							<td><a href="edit?&id=${s.id}">${s.firstName}</a></td>
							<td>
								<c:choose>
									<c:when test="${not empty rs }">
										<ul>
											<c:forEach var="r" items="${rs }">
												<li>
													<a href="edit?id=${r.id }">${r.firstName}</a>
												</li>
											</c:forEach>
										</ul>
									</c:when>
									<c:otherwise>
										<p>EMPTY</p>
									</c:otherwise>								
								</c:choose>
							</td>
							<td>${email.sendDate}</td>
							<td>${email.subject}</td>
							<td>${email.text}</td>
							<td>
								<c:choose>
									<c:when test="${not empty as }">
										<ul>
											<c:forEach var="a" items="${as }">
												<li>
													<a href="load?name=${a.name }">
														<span>
															<c:set var="indexOf" value="${fn:indexOf(a.name, '_') }"/>
															<c:set var="nameLength" value="${fn:length(a.name) }"/>
															<c:set var="simpleName" value="${fn:substring(a.name, indexOf + 1, nameLength) }"/>
															<c:out value="${simpleName }"/>
														</span>
													</a>											
												</li>
											</c:forEach>
										</ul>
									</c:when>
									<c:otherwise>
										<p>EMPTY</p>
									</c:otherwise>								
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:when>
			<c:otherwise>
				<h3>NO LETTERS TODAY</h3>
			</c:otherwise>
		</c:choose>	
	</form>
</div>