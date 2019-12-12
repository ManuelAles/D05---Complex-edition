<%-- form.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form>

	<acme:form-textbox code="worker.application.form.label.reference" path="reference" placeholder="EMPX-JOBY-WORKZ"/>

	<jstl:if test="${command != 'create'}">
		<acme:form-moment code="worker.application.form.label.moment" path="moment" readonly="true" />
		<acme:form-textbox code="worker.application.form.label.status" path="status" readonly="true" />
		<acme:form-textbox code="worker.application.form.label.job" path="job.title" readonly="true" />
	</jstl:if>
	
	<acme:form-textarea code="worker.application.form.label.statement" path="statement" />
	<acme:form-textbox code="worker.application.form.label.skills" path="skills" />
	<acme:form-textbox code="worker.application.form.label.qualifications" path="qualifications" />

	<acme:form-submit test="${command == 'create'}" 
	code="worker.application.form.button.create" 
	action="/worker/application/create?jobId=${param.jobId}" />

	<acme:form-return code="worker.application.form.button.return" />

</acme:form>