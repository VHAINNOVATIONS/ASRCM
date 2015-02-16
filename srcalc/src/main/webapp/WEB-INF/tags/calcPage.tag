<%@ tag language="java" pageEncoding="ISO-8859-1"
    description="Defines the layout of a main calculation workflow page (as opposed to administration)" %>
<%@ attribute name="title" required="true" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="srcalc" %>

<srcalc:basePage headerText="NSO Surgical Risk Calculator" title="${title}">
<jsp:doBody/>
</srcalc:basePage>