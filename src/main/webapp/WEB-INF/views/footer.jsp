<%@page import="java.time.LocalDate"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="year" value="${now}" pattern="yyyy" />
<div id="footer-wrapper">
	<div id="logo-left"></div>
	<div id="logo-right"></div>
	<div id="footer">
		<strong>
			Copyright © ${year} Eugene Putsykovich(slesh)
		</strong>
	</div>
</div>
