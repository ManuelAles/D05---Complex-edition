<%-- form.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form readonly="true">

	<acme:form-textbox code="authenticated.job.form.label.reference" path="reference"/>
	<acme:form-textbox code="authenticated.job.form.label.title" path="title"/>
	<acme:form-moment code="authenticated.job.form.label.deadline" path="deadline"/>
	<acme:form-money code="authenticated.job.form.label.salary" path="salary" />
	<acme:form-url code="authenticated.job.form.label.moreInfo" path="moreInfo"/>
	<acme:form-textarea code="authenticated.job.form.label.description" path="descriptor.description"/>
	
		<button type="button" formmethod="get" class="btn btn-default" onclick="location.href= 'authenticated/duty/list_by_job?id=${id}'">
		<acme:message code="authenticated.job.form.label.duties" />
	</button>
	
	<button type="button" formmethod="get" class="btn btn-default" onclick="location.href= 'authenticated/audit-record/list_by_job?id=${id}'">
		<acme:message code="authenticated.job.form.label.auditRecords" />
	</button>
	
	<acme:form-return code="authenticated.job.form.button.return"/>
</acme:form>