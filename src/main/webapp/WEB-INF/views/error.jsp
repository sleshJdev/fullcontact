<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="content">
	<div class="error">
		<div class="error-icon"></div>
		<div class="error-ul">
			<h3 class="error-header">${error.header }</h3>
			<c:choose>
				<c:when test="${error.statusCode != 500}">
					<ul>
						<li><strong><span class="error-title">Status Code:</span><span class="error-value">${error.statusCode }</span></strong></li>
						<li><strong><span class="error-title">Requested URI:</span><span class="error-value">${error.requestUri }</span></strong></li>
					</ul>
				</c:when>
				<c:otherwise>
					<ul>
						<li><strong><span class="error-title">Servlet Name:</span><span class="error-value">${error.servletName }</span></strong></li>
						<li><strong><span class="error-title">Exception Name:</span><span class="error-value">${error.className }</span></strong></li>
						<li><strong><span class="error-title">Requested URI:</span><span class="error-value">${error.requestUri }</span></strong></li>
						<li><strong><span class="error-title">Exception Message:</span><span class="error-value">${error.message }</span></strong></li>
					</ul>
				</c:otherwise>
			</c:choose>
		</div>
		<a href="${pageContext.request.contextPath}/action/show" onclick="history.back()">Back</a>
	</div>
</div>