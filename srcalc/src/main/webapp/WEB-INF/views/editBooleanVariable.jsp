<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<srcalc:editVariablePage variable="${variable}" isNewVariable="false" saveUrl="/admin/variables/${variable.key}">
<%--
Boolean variables only have properties common to other variables. No extra
body content necessary.
--%>
</srcalc:editVariablePage>
