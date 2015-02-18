<%@ tag language="java" pageEncoding="ISO-8859-1"
    description="Defines the layout of an administration page" %>
<%@ attribute name="title" required="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<srcalc:basePage headerText="NSO Surgical Risk Calculator - Administration" title="${title}">
<jsp:doBody/>
<%-- All admin pages have the same footer. --%>
<footer>
    <c:url var="mainHome" value="/" />
    <a href="${mainHome}">Back to the Calculator</a>
</footer>
</srcalc:basePage>