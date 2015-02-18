<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="srcalc" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<srcalc:calcPage title="Select Specialty">

<section>
    <h2>Select Surgical Specialty</h2>
    <ol class="calculationSummary">
    <li><label class="variableName">User:</label> <sec:authentication property="principal.displayName" /></li>
    <li><label class="variableName">Patient:</label> ${calculation.patient}</li>
    </ol>
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

<footer>
<%--
Only show the link if the user has access. This is just for user-friendliness,
of course. The real authorization check is done when making the request to one
of the /admin pages.
--%>
<sec:authorize url="/admin/models">
    <c:url var="adminHome" value="/admin/models" />
    <a href="${adminHome}">Administration</a>
</sec:authorize>
</footer>

</srcalc:calcPage>