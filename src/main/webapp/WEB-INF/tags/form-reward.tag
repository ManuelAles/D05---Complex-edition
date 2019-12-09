<%--
- form-money.tag
-
- Copyright (c) 2019 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@tag language="java" body-content="empty"
	import="acme.framework.helpers.MessageHelper"
%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<%@attribute name="path1" required="true" type="java.lang.String"%>
<%@attribute name="path2" required="true" type="java.lang.String"%>
<%@attribute name="code" required="true" type="java.lang.String"%>
<%@attribute name="placeholder" required="false" type="java.lang.String"%>
<%@attribute name="readonly" required="false" type="java.lang.Boolean"%>

<jstl:if test="${placeholder == null}">
	<jstl:set var="placeholder" value="${MessageHelper.getMessage('default.placeholder.money')}"/>	
</jstl:if>

<jstl:if test="${readonly == null}">
	<jstl:set var="readonly" value="false"/>
</jstl:if>

<div class="form-group">
	<label for="${path1}">
		<acme:message code="${code}"/>
	</label>
	<input 
		id="${path1}" 
		name="${path1}"
		value="<acme:print value="${requestScope[path1]}"/> - <acme:print value="${requestScope[path2]}"/>"
		type="text"
		class="form-control"
		placeHolder="${placeholder}"
		<jstl:if test="${readonly}">
       		readonly
       	</jstl:if>
	/>

	<acme:form-errors path="${path1}"/>
	<acme:form-errors path="${path1}.amount"/>
	<acme:form-errors path="${path1}.currency"/>

	<acme:form-errors path="${path2}"/>
	<acme:form-errors path="${path2}.amount"/>
	<acme:form-errors path="${path2}.currency"/>
</div>
