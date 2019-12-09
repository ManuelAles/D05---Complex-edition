<%-- form.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form readonly="true">

	<acme:form-textbox code="auditor.job.form.label.reference" path="reference" />
	<acme:form-textbox code="auditor.job.form.label.title" path="title" />
	<acme:form-moment code="auditor.job.form.label.deadline" path="deadline" />
	<acme:form-money code="auditor.job.form.label.salary" path="salary" />
	<acme:form-url code="auditor.job.form.label.moreInfo" path="moreInfo" />
	<acme:form-textarea code="auditor.job.form.label.description" path="descriptor.description" />

		
	<button type="button" formmethod="get" class="btn btn-default" onclick="location.href= 'auditor/duty/list_by_job?id=${id}'">
		<acme:message code="auditor.job.form.label.duties" />
	</button>
	
	<acme:form-return code="auditor.job.form.button.return" />

</acme:form>