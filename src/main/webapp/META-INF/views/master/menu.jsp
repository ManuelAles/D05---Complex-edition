<%--
- menu.jsp
-
- Copyright (c) 2019 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java" import="acme.framework.helpers.PrincipalHelper,acme.entities.roles.Provider,acme.entities.roles.Consumer"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:menu-bar code="master.menu.home">
	<acme:menu-left>
    
    <!-- authenticated -->
		
		<acme:menu-option code="master.menu.authenticated.reporting" access="isAuthenticated()">			
			<acme:menu-suboption code="master.menu.authenticated.announcement.list" action="/authenticated/announcement/list"/>
			<acme:menu-suboption code="master.menu.authenticated.request.list" action="/authenticated/request/list"/>
			<acme:menu-suboption code="master.menu.authenticated.offer.list" action="/authenticated/offer/list"/>
			<acme:menu-suboption code="master.menu.authenticated.company-record.list" action="/authenticated/company-record/list"/>
			<acme:menu-suboption code="master.menu.authenticated.investor-record.list" action="/authenticated/investor-record/list"/>
			<acme:menu-suboption code="master.menu.authenticated.challenge.list" action="/authenticated/challenge/list" />
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.authenticated.job.list" action="/authenticated/job/list" />
			<acme:menu-suboption code="master.menu.authenticated.thread.list" action="/authenticated/thread/list_mine" />
		</acme:menu-option>

    
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link1" action="http://www.youtube.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link0" action="https://www.youtube.com/watch?v=ht6VF5no938"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link" action="https://www.nbamaniacs.com"/>
			<acme:menu-separator/>
      <acme:menu-suboption code="master.menu.anonymous.company-record.list" action="/anonymous/company-record/list"/>
      <acme:menu-suboption code="master.menu.anonymous.investor-record.list" action="/anonymous/investor-record/list"/>
      <acme:menu-suboption code="master.menu.anonymous.announcement.list" action="/anonymous/announcement/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRole('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.company-record.list" action="/administrator/company-record/list" />
			<acme:menu-suboption code="master.menu.administrator.company-record.create" action="/administrator/company-record/create" />
			<acme:menu-suboption code="master.menu.administrator.investor-record.list" action="/administrator/investor-record/list" />
			<acme:menu-suboption code="master.menu.administrator.investor-record.create" action="/administrator/investor-record/create" />
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.announcements" action="/administrator/announcement/list" />
			<acme:menu-suboption code="master.menu.administrator.createAnnouncement" action="/administrator/announcement/create"/>
			<acme:menu-suboption code="master.menu.administrator.configuration" action="/administrator/configuration/show"/>
			<acme:menu-suboption code="master.menu.administrator.challenge.list" action="/administrator/challenge/list" />
			<acme:menu-suboption code="master.menu.administrator.challenge.createChallenge" action="/administrator/challenge/create" />
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shutdown" action="/master/shutdown"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRole('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
			<acme:menu-suboption code="master.menu.provider.request.create" action="/provider/request/create"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRole('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
			<acme:menu-suboption code="master.menu.consumer.offer.create" action="/consumer/offer/create"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.employer" access="hasRole('Employer')">
			<acme:menu-suboption code="master.menu.employer.job.list" action="/employer/job/list_mine"/>
			<acme:menu-suboption code="master.menu.employer.application.list" action="/employer/application/list_by_job"/>
			
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.worker" access="hasRole('Worker')">
			<acme:menu-suboption code="master.menu.worker.application.list" action="/worker/application/list_mine"/>
		</acme:menu-option>
		
				<acme:menu-option code="master.menu.auditor" access="hasRole('Auditor')">
			<acme:menu-suboption code="master.menu.auditor.job.list_mine" action="/auditor/job/list_mine"/>
			<acme:menu-suboption code="master.menu.auditor.job.list_not_mine" action="/auditor/job/list_not_mine"/>
		</acme:menu-option>
		
	</acme:menu-left>
	


	<acme:menu-right>
		<acme:menu-option code="master.menu.sign-up" action="/anonymous/user-account/create" access="isAnonymous()"/>
		<acme:menu-option code="master.menu.sign-in" action="/master/sign-in" access="isAnonymous()"/>

		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-data" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRole('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider" action="/authenticated/provider/update" access="hasRole('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRole('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer" action="/authenticated/consumer/update" access="hasRole('Consumer')"/>
		</acme:menu-option>		



		<acme:menu-option code="master.menu.sign-out" action="/master/sign-out" access="isAuthenticated()"/>
	</acme:menu-right>
</acme:menu-bar>
