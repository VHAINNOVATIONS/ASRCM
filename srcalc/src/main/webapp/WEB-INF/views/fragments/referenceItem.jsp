<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<ul class="patientNote">
<c:forEach var="item" items="${displayItemParam.referenceInfo}">
    <li>${item}</li>
</c:forEach>
</ul>