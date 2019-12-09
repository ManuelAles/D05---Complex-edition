<%-- list.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="auditor.duty.list.label.title" path="title" width="75%" />
	<acme:list-column code="auditor.duty.list.label.percentage" path="percentage" width="25%" />
</acme:list>


<acme:form-return code="auditor.duty.list.button.return.list" />
