<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="javax.servlet.jsp.PageContext"%>
<%@page import="java.time.LocalDate"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>${title}</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>
	<div id="container">
		<jsp:include page="header.jsp" />
		<div class="clear"></div>

		<jsp:include page="${content}" />
		<div class="clear"></div>
		
		<jsp:include page="footer.jsp" />
	</div>
</body>
</html>