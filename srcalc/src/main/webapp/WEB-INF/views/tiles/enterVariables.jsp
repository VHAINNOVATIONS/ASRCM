<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
        <h2>Enter Risk Variables</h2>
        <ol class="calculationSummary">
        <li><label class="variableName">User:</label> <sec:authentication property="principal.displayName" /></li>
        <li><label class="variableName">Specialty:</label> ${calculation.specialty}</li>
        <li><label class="variableName">Patient:</label> ${calculation.patient}</li>
        </ol>
        <form:form id="riskVarForm" cssClass="srcalcForm attributeEditForm"
            method="post" action="enterVars" commandName="variableEntry">
        <c:forEach var="variableGroup" items="${calculation.variableGroups}">
        <fieldset>
            <legend>${variableGroup.name}</legend>
            <!-- Use an ordered list for the list of fields. -->
            <ol>
            <c:forEach var="variable" items="${variableGroup.variables}">
            <c:set var="varPath" value="dynamicValues[${variable.displayName}]" />
            <li><label class="attributeName">${variable.displayName}:</label>
            <%--
            Use our variableSpecific custom tag to write the corresponding form
            control for each variable type.
            --%>
            <%-- TODO: can I preserve the inputted value even if invalid? --%>
            <srcalc:variableSpecific variable="${variable}">
            <jsp:attribute name="numericalFragment">
                <form:input path="${varPath}" size="8"/>
            </jsp:attribute>
            <jsp:attribute name="multiSelectFragment">
                <c:choose>
                <c:when test="${variable.displayType == 'Radio'}">
                <%-- Generate a radio button for each option --%>
                <c:forEach var="option" items="${variable.options}">
                <label class="radioLabel"><form:radiobutton path="${varPath}" value="${option.value}"/> ${option.value}</label>
                </c:forEach>
                </c:when>
                <c:when test="${variable.displayType == 'Dropdown'}">
                Drop-down variables not supported yet.
                </c:when>
                <c:otherwise>Error: unexpected display type "${variable.displayType}".</c:otherwise>
                </c:choose>
            </jsp:attribute>
            <jsp:attribute name="booleanFragment">
                <label class="checkboxLabel"><form:checkbox path="${varPath}" value="true"/> ${variable.displayName}</label>
            </jsp:attribute>
            <jsp:attribute name="procedureFragment">
                <%--
                Javascript code below will transform this table into a jQueryUI
                dialog for a much better user experience.
                --%>
                <div class="procedureSelectGroup dialog" title="Select ${variable.displayName}">
                <table>
                <thead><tr><th>Select</th><th>CPT Code</th><th>Description</th><th>RVU</th></tr></thead>
                <c:forEach var="procedure" items="${srcalc:truncateList(variable.procedures, 100)}">
                <tr>
                <td class="selectRadio">
                <form:radiobutton path="${varPath}" value="${procedure.cptCode}" data-display-string="${procedure}"/></td>
                <td>${procedure.cptCode}</td><td>${procedure.longDescription}</td><td>${procedure.rvu}</td></tr>
                </c:forEach>
                </table>
                </div>
            </jsp:attribute>
            </srcalc:variableSpecific>
            <%-- Display any errors immediately following the input control. --%>
            <form:errors path="${varPath}" cssClass="error" />
            </li>
            </c:forEach>
            </ol>
        </fieldset>
        </c:forEach>
        <div class="actionButtons">
        <ol>
        <li><button type="submit">Run Calculation</button></li>
        <li><c:url var="newCalcUrl" value="/newCalc" />
        <a class="btn-link" href="${newCalcUrl}">Start New Calculation</a></li>
        </ol>
        </div>
        <c:url var="enterVariablesJsUrl" value="/js/enterVariables.js"/>
        <script type="text/javascript" src="${enterVariablesJsUrl}"></script>
        <script>
        $(document).ready(initProcedureSelect);
        </script>
        </form:form>
