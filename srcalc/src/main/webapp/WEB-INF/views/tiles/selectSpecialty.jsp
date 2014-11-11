<%-- This file should really have a .jspf extension, but I get the following
     error:
     No mapping found for HTTP request with URI [/srcalc/WEB-INF/views/tiles/selectSpecialty.jspf] in DispatcherServlet with name 'srcalc'
     
     It works fine out-of-the-box with JSP.
      --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <p>Risk Calculation for ${calculation.patient}.</p>
        <h2>Select Surgical Specialty</h2>
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