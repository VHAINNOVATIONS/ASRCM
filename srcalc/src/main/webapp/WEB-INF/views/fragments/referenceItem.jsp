<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- This file cannot be a jspf extension because it is not included statically. --%>
<%-- This jsp expects for displayItemParam to be defined. --%>
<ul class="patientNote">
<c:forEach var="item" items="${displayItemParam.referenceInfo}">
    <li>${item}</li>
</c:forEach>
</ul>