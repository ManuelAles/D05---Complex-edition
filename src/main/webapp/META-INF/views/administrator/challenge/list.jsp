<%-- list.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="administrator.challenge.list.label.deadline" path="deadline" width="204"/>
	<acme:list-column code="administrator.challenge.list.label.title" path="title" width="404"/>
</acme:list>