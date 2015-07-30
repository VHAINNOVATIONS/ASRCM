<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<srcalc:editVariablePage variable="${variable}" isNewVariable="true" saveUrl="${SAVE_URL}">
<%@ include file="fragments/multiSelectProperties.jspf" %>
</srcalc:editVariablePage>