
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="header">
	<div id="logo-left"></div>
	<div id="logo-center"></div>
	<div id="logo-right"></div>
</div>

<c:if test="${not empty status}"> <h1 class="success"> ${status}</h1> </c:if>

<div id="menu">
	<ul>
		<li><a href="show">SHOW CONTACTS</a></li>
		<li><a href="add">ADD NEW CONTACT</a></li>
		<li><a href="search">SEARCH CONTACT</a></li>
<!-- 		<li><a href="send" class="hidden-link">SEND MAIL</a></li> -->
	</ul>
</div>
