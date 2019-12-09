<%-- list.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:list>	
	<acme:list-column code="authenticated.auditRecord.list.label.title" path="title" width="60%"/>
	<acme:list-column code="authenticated.auditRecord.list.label.moment" path="moment" width="20%"/>
	<acme:list-column code="authenticated.auditRecord.list.label.job" path="job.title" width="20%"/>
</acme:list>

<acme:form-return code="authenticated.auditRecord.list.button.return.list"/>
