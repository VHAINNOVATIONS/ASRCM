<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<srcalc:editVariablePage variable="${variable}" isNewVariable="false" saveUrl="/admin/variables/${variable.key}">
<%@ include file="fragments/discreteNumericalProperties.jspf" %>
</srcalc:editVariablePage>
