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
        <%-- See srcalc.css for why HTML tables. --%>
        <table>
        <c:forEach var="variableGroup" items="${calculation.variableGroups}">
        <tbody>
        <tr><th colspan="2" class="groupName">${variableGroup.name}</th></tr>
        <c:forEach var="variable" items="${variableGroup.variables}">
        <tr>
            <c:set var="varPath" value="${srcalc:dynamicValuePath(variable.key)}" />
            <td class="attributeName">${variable.displayName}:</td>
            <%--
            Use our variableSpecific custom tag to write the corresponding form
            control for each variable type.
            --%>
            <td class="attributeValue">
            <srcalc:variableSpecific variable="${variable}">
            <jsp:attribute name="numericalFragment">
                <form:input path="${varPath}" size="6"/> ${variable.units}
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
            <jsp:attribute name="discreteNumericalFragment">
                <!-- Wrap both the radio button and numerical entry in a span.radioLabel
                     for proper spacing. -->
                <span class="radioLabel"><label><form:radiobutton path="${varPath}" cssClass="numericalRadio" value="numerical"/> Numerical:</label>
                <c:set var="numericalVarName" value="${variable.key}_numerical" />
                <c:set var="numericalVarPath" value="${srcalc:dynamicValuePath(numericalVarName)}" />
                <form:input cssClass="numerical" path="${numericalVarPath}" size="6"/> ${variable.units}</span>
                <form:errors path="${numericalVarPath}" cssClass="error" /><br>
                <c:forEach var="opt" items="${variable.options}">
                <label class="radioLabel"><form:radiobutton path="${varPath}" value="${opt.value}"/> Presumed ${opt.value}</label>
                </c:forEach>
            </jsp:attribute>
            <jsp:attribute name="procedureFragment">
                <c:set var="selectedCpt"
                    value="${variableEntry.dynamicValues[variable.displayName]}" />
                <c:set var="initialText" value="(none)" />
                <form:hidden path="${varPath}" cssClass="procedureHiddenInput" />
                <div class="procedureSelectGroup dialog" title="Select ${variable.displayName}">
                <table id="procedureTable">
                <thead><tr><th>CPT Code</th><th>Description</th><th>RVU</th><th>Select</th></tr></thead>
                </table>
                
                <script>
					procedureArray = new Array();
					<c:forEach var="procedure" items="${variable.procedures}">
					var entry = {
					        'cptCode': "${procedure.cptCode}",
					        'displayString': "${procedure}",
					        'longDescription': "${fn:escapeXml(procedure.longDescription)}",
					        'rvu': "${procedure.rvu}"
					};
                    procedureArray.push(entry);
                    <c:if test="${procedure.cptCode == selectedCpt}" >
                    <c:set var="initialText" value="${procedure}" />
                    </c:if>
					</c:forEach>
                </script>
                </div>
                <span class="procedureDisplay">${initialText}</span>
                <a class="selectProcedureLink" href="#">Select</a>
            </jsp:attribute>
            </srcalc:variableSpecific>
            <%-- Display any errors immediately following the input control. --%>
            <form:errors path="${varPath}" cssClass="error" />
            </td>
        </tr>
        </c:forEach>
        </tbody>
        </c:forEach>
        </table>
        <div class="actionButtons">
        <ol>
        <li><button type="submit">Run Calculation</button></li>
        <li><c:url var="newCalcUrl" value="/newCalc" />
        <a class="btn-link" href="${newCalcUrl}">Start New Calculation</a></li>
        </ol>
        </div>
        <c:url var="enterVariablesJsUrl" value="/js/enterVariables.js"/>
        <script type="text/javascript" src="${enterVariablesJsUrl}"></script>
        <c:url var="dataTablesUrl" value="/js/vendor/jquery.dataTables.min.js"/>
        <script type="text/javascript" src="${dataTablesUrl}"></script>
        <script type="text/javascript">
        $(document).ready(function(){
                initEnterVariablesPage();
        });
        </script>
        </form:form>
