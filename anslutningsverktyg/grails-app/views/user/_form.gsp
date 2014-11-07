<%@ page import="se.skltp.av.User" %>



<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'username', 'error')} required">
	<label for="username">
		<g:message code="user.username.label" default="Username" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="username" required="" value="${userInstance?.username}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'passwordHash', 'error')} required">
	<label for="passwordHash">
		<g:message code="user.passwordHash.label" default="Password Hash" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="passwordHash" required="" value="${userInstance?.passwordHash}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'datumSkapad', 'error')} ">
	<label for="datumSkapad">
		<g:message code="user.datumSkapad.label" default="Datum Skapad" />
		
	</label>
	<g:datePicker name="datumSkapad" precision="day"  value="${userInstance?.datumSkapad}" default="none" noSelection="['': '']" />

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'datumUppdaterad', 'error')} ">
	<label for="datumUppdaterad">
		<g:message code="user.datumUppdaterad.label" default="Datum Uppdaterad" />
		
	</label>
	<g:datePicker name="datumUppdaterad" precision="day"  value="${userInstance?.datumUppdaterad}" default="none" noSelection="['': '']" />

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'epost', 'error')} required">
	<label for="epost">
		<g:message code="user.epost.label" default="Epost" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="email" name="epost" required="" value="${userInstance?.epost}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'permissions', 'error')} ">
	<label for="permissions">
		<g:message code="user.permissions.label" default="Permissions" />
		
	</label>
	

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'roles', 'error')} ">
	<label for="roles">
		<g:message code="user.roles.label" default="Roles" />
		
	</label>
	<g:select name="roles" from="${se.skltp.av.Role.list()}" multiple="multiple" optionKey="id" size="5" value="${userInstance?.roles*.id}" class="many-to-many"/>

</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'tjanstekomponenter', 'error')} ">
	<label for="tjanstekomponenter">
		<g:message code="user.tjanstekomponenter.label" default="Tjanstekomponenter" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${userInstance?.tjanstekomponenter?}" var="t">
    <li><g:link controller="tjansteproducent" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="tjansteproducent" action="create" params="['user.id': userInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'tjansteproducent.label', default: 'Tjansteproducent')])}</g:link>
</li>
</ul>


</div>

