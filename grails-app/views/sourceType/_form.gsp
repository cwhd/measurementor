<%@ page import="org.cwhd.measure.configuration.SourceType" %>



<div class="fieldcontain ${hasErrors(bean: sourceTypeInstance, field: 'systemName', 'error')} required">
	<label for="systemName">
		<g:message code="sourceType.systemName.label" default="System Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="systemName" from="${org.cwhd.measure.configuration.SourceType$SystemName?.values()}" keys="${org.cwhd.measure.configuration.SourceType$SystemName.values()*.name()}" required="" value="${sourceTypeInstance?.systemName?.name()}" />

</div>

<div class="fieldcontain ${hasErrors(bean: sourceTypeInstance, field: 'sourceName', 'error')} required">
	<label for="sourceName">
		<g:message code="sourceType.sourceName.label" default="Source Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="sourceName" required="" value="${sourceTypeInstance?.sourceName}"/>

</div>

