
<%@ page import="org.cwhd.measure.configuration.SourceType" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'sourceType.label', default: 'SourceType')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-sourceType" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-sourceType" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list sourceType">
			
				<g:if test="${sourceTypeInstance?.systemName}">
				<li class="fieldcontain">
					<span id="systemName-label" class="property-label"><g:message code="sourceType.systemName.label" default="System Name" /></span>
					
						<span class="property-value" aria-labelledby="systemName-label"><g:fieldValue bean="${sourceTypeInstance}" field="systemName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sourceTypeInstance?.sourceName}">
				<li class="fieldcontain">
					<span id="sourceName-label" class="property-label"><g:message code="sourceType.sourceName.label" default="Source Name" /></span>
					
						<span class="property-value" aria-labelledby="sourceName-label"><g:fieldValue bean="${sourceTypeInstance}" field="sourceName"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:sourceTypeInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${sourceTypeInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
