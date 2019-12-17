<%-- form.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form>


	<acme:form-textbox code="employer.job.form.label.reference" path="reference" placeholder="EEEE-JJJJ"/>
	<acme:form-textbox code="employer.job.form.label.title" path="title" />
	<acme:form-moment code="employer.job.form.label.deadline" path="deadline" />
	<acme:form-money code="employer.job.form.label.salary" path="salary" />
	<acme:form-url code="employer.job.form.label.moreInfo" path="moreInfo" />
	<acme:form-textarea code="employer.job.form.label.description" path="descriptor.description" />
	<acme:form-checkbox code="employer.job.form.label.finalMode" path="finalMode" readonly="true" />
		
	
	<acme:form-submit test="${command == 'create'}" code="employer.job.form.button.create" action="/employer/job/create" />
	<jstl:if test="${finalMode == false}">	
	<acme:form-submit test="${command == 'show'}" code="employer.job.form.button.update" action="/employer/job/update" />
	<acme:form-submit test="${command == 'show'}" code="employer.job.form.button.publish" action="/employer/job/publish" />
	</jstl:if>
	
	
	<acme:form-submit test="${command == 'show'}" code="employer.job.form.button.delete" action="/employer/job/delete" />
	
	<acme:form-return code="employer.job.form.button.return" />
	<br>	

	<acme:form-submit test="${command != 'create'}" method="get" code="employer.job.form.button.duties" action="/employer/duty/list_by_job?id=${id}"/>
	
	<jstl:if test="${finalMode == false}">
	<jstl:set var="jobId" value="${id}"/>
	<acme:form-submit test="${command != 'create'}" method="get" code="employer.job.form.button.duty.create" action="/employer/duty/create?jobId=${jobId}"/>
	</jstl:if>
	<br>	

	<acme:form-submit test="${command != 'create'}" method="get" code="employer.job.form.button.auditRecords" action="/employer/audit-record/list_by_job?id=${id}"/>
	<br>
	
	



</acme:form>