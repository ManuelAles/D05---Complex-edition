<%-- form.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form>

	<acme:form-textbox code="employer.application.form.label.reference" path="reference" readonly="true" />
	<acme:form-moment code="employer.application.form.label.moment" path="moment" readonly="true" />

	<jstl:choose>
		<jstl:when test="${status == 'PENDING' || (status == 'REJECTED' && rejectedDecision == '')}">
			<acme:form-select code="employer.application.form.label.status" path="status">
				<acme:form-option code="employer.application.form.label.status.pending" value="PENDING" />
				<acme:form-option code="employer.application.form.label.status.accepted" value="ACCEPTED" />
				<acme:form-option code="employer.application.form.label.status.rejected" value="REJECTED" />
			</acme:form-select>
			<acme:form-textarea code="employer.application.form.label.rejectDecision" path="rejectedDecision" />
		</jstl:when>
		<jstl:when test="${status == 'REJECTED' && rejectedDecision != ''}">
			<acme:form-textbox code="employer.application.form.label.status" path="status" readonly="true" />
			<acme:form-textarea code="employer.application.form.label.rejectDecision" path="rejectedDecision" readonly="true"/>
		</jstl:when>
		<jstl:otherwise>
			<acme:form-textbox code="employer.application.form.label.status" path="status" readonly="true" />
		</jstl:otherwise>
	</jstl:choose>


	<acme:form-textbox code="employer.application.form.label.statement" path="statement" readonly="true" />
	<acme:form-textbox code="employer.application.form.label.skills" path="skills" readonly="true" />
	<acme:form-textbox code="employer.application.form.label.qualifications" path="qualifications" readonly="true" />
	<acme:form-textbox code="employer.application.form.label.worker" path="worker.userAccount.username" readonly="true" />

	<acme:form-submit test="${command == 'show'}" code="employer.application.form.button.update" action="/employer/application/update" />
	<acme:form-submit test="${command == 'update'}" code="employer.application.form.button.update"
		action="/employer/application/update" />

	<acme:form-return code="employer.application.form.button.return" />
</acme:form>