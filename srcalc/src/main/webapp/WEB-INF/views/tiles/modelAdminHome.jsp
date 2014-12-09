<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <h2>Model Administration</h2>
    <h3>Variable Definitions</h3>
    <table class="srcalcTable" id="variableSummaryTable">
    <tr><th>Name</th><th>Type</th></tr>
    <c:forEach var="variable" items="${variables}">
    <tr><td>${variable.displayName}</td><td>${variable.typeName}</td></tr>
    </c:forEach>
    </table>