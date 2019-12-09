<%-- form.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form readonly="true">

	<acme:form-textbox code="authenticated.auditRecord.form.label.title" path="title" />
	<acme:form-moment code="authenticated.auditRecord.form.label.moment" path="moment" />
	<acme:form-textarea code="authenticated.auditRecord.form.label.body" path="body" />	
		
	<acme:form-return code="authenticated.auditRecord.form.button.return" />

</acme:form>