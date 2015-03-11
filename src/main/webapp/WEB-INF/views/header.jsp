<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="header">
	<div id="logo-left"></div>
	<div id="logo-center"></div>
	<div id="logo-right"></div>
</div>

<h1 class="status">${status}</h1>
<%-- <c:if test="${not empty status}"> --%>
	
<%-- </c:if> --%>

<div class="clear"></div>

<div id="menu">
	<ul>
		<li><a href="show">SHOW CONTACTS</a></li>
		<li><a href="add">ADD NEW CONTACT</a></li>
		<li><a href="search">SEARCH CONTACT</a></li>
		<li><a href="letters">LETTERS</a></li>
		<li></li>
	</ul>
</div>

<div class="notify-link">
	<div class="link">
		<a href="notification">NOTIFICATIONS</a>
	</div>
	<c:if test="${not empty quantity }">
		<div class="quantity">${quantity }</div>
	</c:if>
</div>

<div class="clear"></div>