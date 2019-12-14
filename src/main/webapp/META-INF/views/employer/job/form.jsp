<%-- form.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form readonly="true">

	<acme:form-textbox code="employer.job.form.label.reference" path="reference" />
	<acme:form-textbox code="employer.job.form.label.title" path="title" />
	<acme:form-moment code="employer.job.form.label.deadline" path="deadline" />
	<acme:form-money code="employer.job.form.label.salary" path="salary" />
	<acme:form-url code="employer.job.form.label.moreInfo" path="moreInfo" />
	<acme:form-textarea code="employer.job.form.label.description" path="descriptor.description" />
	<acme:form-checkbox code="employer.job.form.label.finalMode" path="finalMode" />

		
	<button type="button" formmethod="get" class="btn btn-default" onclick="location.href= 'employer/duty/list_by_job?id=${id}'">
		<acme:message code="employer.job.form.button.duties" />
	</button>
	
	<security:authorize access="hasRole('Employer')">	
	<jstl:set var="jobId" value="${id}"/>
	<button type="button" formmethod="get" class="btn btn-default" onclick="location.href= 'employer/duty/create?jobId=${jobId}'">
		<acme:message code="employer.job.form.button.duty.create" />
	</button>
	</security:authorize>

	<br>
		<button type="button" formmethod="get" class="btn btn-default" onclick="location.href= 'employer/audit-record/list_by_job?id=${id}'">
		<acme:message code="employer.job.form.button.auditRecords" />
	</button>
	
	<acme:form-return code="employer.job.form.button.return" />

</acme:form>