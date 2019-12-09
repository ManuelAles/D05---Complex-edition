<%-- form.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form readonly="true">

	<acme:form-textbox code="worker.application.form.label.reference" path="reference" />
	<acme:form-moment code="worker.application.form.label.moment" path="moment" />
	<acme:form-textbox code="worker.application.form.label.status" path="status" />
	<acme:form-textbox code="worker.application.form.label.statement" path="statement" />
	<acme:form-textbox code="worker.application.label.skills" path="skills" />
	<acme:form-textbox code="worker.application.label.qualifications" path="qualifications" />
	<acme:form-textbox code="worker.application.label.job" path="job.title" />
	
	<acme:form-return code="worker.application.form.button.return" />

</acme:form>