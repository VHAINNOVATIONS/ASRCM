<%@ taglib tagdir="/WEB-INF/tags" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<srcalc:adminPage title="Model Administration">
<section>
    <h2>Model Administration</h2>
    <h3>Model Definitions</h3>    

    <table class="srcalcTable" id="riskModelsTable">
    <tr>
        <th class="main">Name</th>
        <th></th>
    </tr>
    
    <c:forEach var="model" items="${riskModels}">
    <c:url var="editModel" value="/admin/models/${model.id}" />

    <tr>
        <td><c:out value="${model.displayName}"/></td>
        <td><a href="${editModel}" class="editObjectLink">Edit</a></td>
    </tr>
    </c:forEach>
    <tr>
    </tr>
        
    </table>
    
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
    <div id="newVariableLinks">
    Add New:
    <ul>
    <li><c:url var="newBooleanVar" value="/admin/newBooleanVar" />
        <a href="${newBooleanVar}" class="btn-default">Checkbox</a></li>
    <li><c:url var="newMultiSelectVar" value="/admin/newMultiSelectVar" />
        <a href="${newMultiSelectVar}" class="btn-default">Radio</a></li>
    <li><c:url var="newDiscreteVar" value="/admin/newDiscreteVar" />
        <a href="${newDiscreteVar}" class="btn-default">Discrete Numerical</a></li>
    </ul>
    </div>
    <h3>Rules</h3>
    <table class="srcalcTable" id="rulesTable">
    <tr>
        <th class="main">Name</th>
        <th>Edit</th>
    </tr>
    <c:forEach var="rule" items="${rules}">
        <c:url var="ruleId" value="/admin/rules/${rule.id}" />
	    <tr>
	        <td><c:out value="${rule.displayName}"/></td>
	        <td><a href="${ruleId}" class="editObjectLink">Edit</a></td>
	    </tr>
    </c:forEach>
    </table>
    <div id="newRuleLink">
    Add New:
    <ul>
        <li><c:url var="newRule" value="/admin/newRule" />
            <a href="${newRule}" class="btn-default">Rule</a></li>
    </ul>
    </div>
    <h3>Procedures</h3>
    <c:url var="editProcedures" value="/admin/procedures" />
    <p><a href="${editProcedures}" class="btn-link">Manage Procedures</a></p>
</section>
</srcalc:adminPage>
