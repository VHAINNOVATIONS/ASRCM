<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<srcalc:calcPage title="Enter Variables">
<section>
    <h2>Enter Risk Variables</h2>
    <%@include file="fragments/calculationSummary.jspf" %>
    <form:form id="riskVarForm" cssClass="srcalcForm attributeEditForm"
        method="post" action="enterVars" commandName="variableEntry">
    <%-- See srcalc.css for why HTML tables. --%>
    <table>
    <c:forEach var="variableGroup" items="${calculation.variableGroups}">
    <tbody>
    <tr><th colspan="2" class="groupName"><c:out value="${variableGroup.name}"/></th></tr>
    <c:forEach var="variable" items="${variableGroup.variables}">
    <tr>
        <c:set var="varPath" value="${srcalc:dynamicValuePath(variable.key)}" />
        <td class="attributeName"><c:out value="${variable.displayName}"/>:</td>
        <%--
        Use our variableSpecific custom tag to write the corresponding form
        control for each variable type.
        --%>
        <td class="attributeValue">
        <srcalc:variableSpecific variable="${variable}">
        <jsp:attribute name="numericalFragment">
            <form:input path="${varPath}" size="6"/>
            <c:out value="${variable.units} ${variableEntry.getMeasureDate(variable.key)}"/>
        </jsp:attribute>
        <jsp:attribute name="multiSelectFragment">
            <c:choose>
            <c:when test="${variable.displayType == 'Radio'}">
            <%-- Generate a radio button for each option --%>
            <c:forEach var="option" items="${variable.options}">
            <label class="radioLabel"><form:radiobutton path="${varPath}" value="${option.value}"/> <c:out value="${option.value}"/></label>
            </c:forEach>
            </c:when>
            <c:when test="${variable.displayType == 'Dropdown'}">
            Drop-down variables not supported yet.
            </c:when>
            <c:otherwise>Error: unexpected display type "${variable.displayType}".</c:otherwise>
            </c:choose>
        </jsp:attribute>
        <jsp:attribute name="booleanFragment">
            <label class="checkboxLabel"><form:checkbox path="${varPath}" value="true"/> <c:out value="${variable.displayName}"/></label>
        </jsp:attribute>
        <jsp:attribute name="discreteNumericalFragment">
            <!-- Wrap both the radio button and numerical entry in a span.radioLabel
                 for proper spacing. -->
            <span class="radioLabel"><form:radiobutton path="${varPath}" cssClass="numericalRadio" value="numerical"/>
            <c:set var="numericalVarName" value="${variable.key}$numerical" />
            <c:set var="numericalVarPath" value="${srcalc:dynamicValuePath(numericalVarName)}" />
            <form:input cssClass="numerical" path="${numericalVarPath}" size="6"/>
            <c:out value="${variable.units} ${variableEntry.getNumericalMeasureDate(variable.key)}"/></span>
            <form:errors path="${numericalVarPath}" cssClass="error" /><br>
            <c:forEach var="opt" items="${variable.options}">
            <label class="radioLabel"><form:radiobutton path="${varPath}" value="${opt.value}"/>
                Presumed <c:out value="${opt.value}"/></label>
            </c:forEach>
        </jsp:attribute>
        <jsp:attribute name="procedureFragment">
            <form:hidden path="${varPath}" cssClass="procedureHiddenInput" />
            <div class="procedureSelectGroup dialog uninitialized" title="Select ${variable.displayName}">
            <span class="loadingText">Loading...</span>
            <!-- The table will be filled by Javascript. -->
            <table id="procedureTable">
            <thead><tr><th>CPT Code</th><th>Description</th><th>RVU</th><th>Select</th></tr></thead>
            </table>
            </div>
            <%-- This text will be replaced by Javascript. --%>
            <span class="procedureDisplay"><span class="loadingText">Loading...</span></span>
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
    <li><button class="button-em" type="submit">Run Calculation</button></li>
    </ol>
    <%-- Add the required patientDfn parameter, preserving the patient from the current calculation. --%>
    <c:url var="newCalcUrl" value="/newCalc">
        <c:param name="patientDfn" value="${calculation.patient.dfn}"/>
        <c:param name="force" value="true"/>
    </c:url>
    <a href="${newCalcUrl}" class="btn-link">Start New Calculation</a>
    </div>
    <c:url var="enterVariablesJsUrl" value="/js/enterVariables.js"/>
    <script type="text/javascript" src="${enterVariablesJsUrl}"></script>
    <c:url var="dataTablesUrl" value="/js/vendor/DataTables-1.10.5/jquery.dataTables.min.js"/>
    <script type="text/javascript" src="${dataTablesUrl}"></script>
    <script type="text/javascript">
    $(document).ready(function(){
        ENTERVARIABLES.initPage();
    });
    </script>
    </form:form>
</section>
</srcalc:calcPage>