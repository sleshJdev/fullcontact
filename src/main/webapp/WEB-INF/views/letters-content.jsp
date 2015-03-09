<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/letters-style.css" />

<div id="content">
	<c:choose>
		<c:when test="${not empty emails }">
			<h3>LETTERS</h3>
			<table id="checkable" class="table">
				<tr>
					<td></td>
					<td>FROM</td>
					<td>TO</td>
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
						<td><a href="edit?&id=${s.id}">${s.firstName} ${s.middleName} ${s.lastName}</a></td>
						<td>
							<c:choose>
								<c:when test="${not empty rs }">
									<ul>
										<c:forEach var="r" items="${rs }">
											<li>
												<a href="edit?id=${r.id }">${r.firstName} ${r.middleName} ${r.lastName}</a>
											</li>
										</c:forEach>
									</ul>
								</c:when>
								<c:otherwise>
									<p>EMPTY</p>
								</c:otherwise>								
							</c:choose>
						</td>
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
</div>