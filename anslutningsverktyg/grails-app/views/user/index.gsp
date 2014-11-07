<%@ page import="se.skltp.av.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
    <style>
    .table {
        table-layout:fixed;
    }

    .table td {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .size {
        max-width: 700px;
        padding: 15px;
        margin: 0 auto;
    }
    </style>
</head>

<body>
<div class="container size">
<a href="#list-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                           default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="list-user" class="content scaffold-list" role="main">
    <h1 class="form-signin-heading"><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
        <table class="table table-striped table-condensed">
            <thead>
            <tr>

                <g:sortableColumn property="username"
                                  title="${message(code: 'user.username.label', default: 'Username')}"/>

                <g:sortableColumn property="passwordHash"
                                  title="${message(code: 'user.passwordHash.label', default: 'Password Hash')}"/>

                <g:sortableColumn property="datumSkapad"
                                  title="${message(code: 'user.datumSkapad.label', default: 'Datum Skapad')}"/>

                <g:sortableColumn property="datumUppdaterad"
                                  title="${message(code: 'user.datumUppdaterad.label', default: 'Datum Uppdaterad')}"/>

                <g:sortableColumn property="epost" title="${message(code: 'user.epost.label', default: 'Epost')}"/>

            </tr>
            </thead>
            <tbody>
            <g:each in="${userInstanceList}" status="i" var="userInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                    <td><g:link action="show"
                                id="${userInstance.id}">${fieldValue(bean: userInstance, field: "username")}</g:link></td>

                    <td>${fieldValue(bean: userInstance, field: "passwordHash")}</td>

                    <td><g:formatDate date="${userInstance.datumSkapad}"/></td>

                    <td><g:formatDate date="${userInstance.datumUppdaterad}"/></td>

                    <td>${fieldValue(bean: userInstance, field: "epost")}</td>

                </tr>
            </g:each>
            </tbody>
        </table>

        <div class="pagination">
            <g:paginate total="${userInstanceCount ?: 0}"/>
        </div>
    </div>

</div>
</body>
</html>
