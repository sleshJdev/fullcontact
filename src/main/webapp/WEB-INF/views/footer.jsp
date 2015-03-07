<%@page import="java.time.LocalDate"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="footer-wrapper">
	<div id="logo-left"></div>
	<div id="logo-right"></div>
	<div id="footer">
		<strong>
			Copyright © <%=LocalDate.now().getYear()%> Eugene Putsykovich(slesh)
		</strong>
	</div>
</div>
