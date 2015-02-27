<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="srcalc" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<srcalc:calcPage title="Select Specialty">

<section>
    <h2>Select Surgical Specialty</h2>
    <%@include file="fragments/calculationSummary.jspf" %>
    <h3>Surgical Specialty</h3>
    <form action="selectSpecialty" method="post">
    <ul id="specialtyRadios">
    <c:forEach var="specialty" items="${specialties}">
    <li>
        <label><input type="radio" name="specialty" value="${specialty.name}"> ${specialty.name}</label>
    </li>
    </c:forEach>
    </ul>
    <button type="submit">Continue</button>
    </form>
</section>

</srcalc:calcPage>