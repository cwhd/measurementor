
<%@ page import="org.cwhd.measure.configuration.SourceType" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'sourceType.label', default: 'SourceType')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-sourceType" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-sourceType" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="systemName" title="${message(code: 'sourceType.systemName.label', default: 'System Name')}" />
					
						<g:sortableColumn property="sourceName" title="${message(code: 'sourceType.sourceName.label', default: 'Source Name')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${sourceTypeInstanceList}" status="i" var="sourceTypeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${sourceTypeInstance.id}">${fieldValue(bean: sourceTypeInstance, field: "systemName")}</g:link></td>
					
						<td>${fieldValue(bean: sourceTypeInstance, field: "sourceName")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${sourceTypeInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
