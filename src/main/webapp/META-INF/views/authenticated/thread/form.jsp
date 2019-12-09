<%--
- form.jsp
-
- Copyright (c) 2019 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form readonly="true">
	<acme:form-textbox code="authenticated.thread.form.label.title" path="title" />
	<acme:form-moment code="authenticated.thread.form.label.moment" path="moment" />
	<acme:form-return code="authenticated.thread.button.return" />
</acme:form>

<button type="button" formmethod="get" class="btn btn-default" onclick="location.href= 'authenticated/message/list_by_thread?id=${id}'">
	<acme:message code="authenticated.thread.form.label.messages" />
</button>


