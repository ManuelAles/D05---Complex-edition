<%-- form.jsp --%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form readonly="true">

	<acme:form-textbox code="authenticated.challenge.form.label.title" path="title"/>
	<acme:form-moment code="authenticated.challenge.form.label.deadline" path="deadline"/>
	<acme:form-textarea code="authenticated.challenge.form.label.description" path="description"/>
	<acme:form-money code="authenticated.challenge.form.label.goldReward" path="goldReward"/>
	<acme:form-money code="authenticated.challenge.form.label.silverReward" path="silverReward"/>
	<acme:form-money code="authenticated.challenge.form.label.bronzeReward" path="bronzeReward"/>
	<acme:form-textarea code="authenticated.challenge.form.label.goldGoal" path="goldGoal"/>
	<acme:form-textarea code="authenticated.challenge.form.label.silverGoal" path="silverGoal"/>
	<acme:form-textarea code="authenticated.challenge.form.label.bronzeGoal" path="bronzeGoal"/>
	
	
	<acme:form-return code="authenticated.challenge.form.button.return"/>
</acme:form>