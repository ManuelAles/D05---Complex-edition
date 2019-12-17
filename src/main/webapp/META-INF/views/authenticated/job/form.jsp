<%-- form.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form readonly="true">

	<acme:form-textbox code="authenticated.job.form.label.reference" path="reference"/>
	<acme:form-textbox code="authenticated.job.form.label.title" path="title"/>
	<acme:form-moment code="authenticated.job.form.label.deadline" path="deadline"/>
	<acme:form-money code="authenticated.job.form.label.salary" path="salary" />
	<acme:form-url code="authenticated.job.form.label.moreInfo" path="moreInfo"/>
	<acme:form-textarea code="authenticated.job.form.label.description" path="descriptor.description"/>
	
	<acme:form-return code="authenticated.job.form.button.return"/>
	<br>
	
	<acme:form-submit method="get" code="authenticated.job.form.button.duties" action="/authenticated/duty/list_by_job?id=${id}"/>
	
	<acme:form-submit method="get" code="authenticated.job.form.button.auditRecords" action="/authenticated/audit-record/list_by_job?id=${id}"/>

	
	<security:authorize access="hasRole('Worker')">
	<br>
	<jstl:set var="jobId" value="${id}"/>
	<acme:form-submit method="get" code="authenticated.job.form.button.application" action="/worker/application/create?jobId=${jobId}"/>
	</security:authorize>
	
	
</acme:form>