<%-- list.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="employer.application.list.label.reference" path="reference" width="75%"/>
	<acme:list-column code="employer.application.list.label.moment" path="moment" width="25%"/>
	<acme:list-column code="employer.application.list.label.status" path="status" width="25%"/>		
</acme:list>

<acme:form-return code="employer.application.list.button.return"/>

<button type="button" formmethod="get" onclick="javascript: redirect('employer/application/show?id=${id}')" class="btn btn-default">
				<acme:message code="employer.application.list.button.return"/>				
</button>	
