<%@ taglib tagdir="/WEB-INF/tags" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<srcalc:adminPage title="Model Administration">
<section>
    <h2>Model Administration</h2>
    <h3>Variable Definitions</h3>
    <table class="srcalcTable" id="variableSummaryTable">
    <tr>
        <th class="main">Name</th>
        <th class="main">Internal Key</th>
        <th class="main">Type</th>
        <th>Edit</th>
    </tr>
    <c:forEach var="variable" items="${variables}">
    <c:url var="editVariable" value="/admin/variables/${variable.key}" />
    <tr>
        <td><c:out value="${variable.displayName}"/></td>
        <td><c:out value="${variable.key}"/></td>
        <td><c:out value="${variable.typeName}"/></td>
        <td><a href="${editVariable}" class="editObjectLink">Edit</a></td>
    </tr>
    </c:forEach>
    </table>
    <p>Add New:</p>
    <ul id="newVariableLinks">
    <li><c:url var="newBooleanVar" value="/admin/newBooleanVar" />
        <a href="${newBooleanVar}" class="btn-link">Checkbox</a></li>
    </ul>
</section>
</srcalc:adminPage>