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
    <%-- Set variableEntry and displayItem to request scope so that the included
        jsps can also use them. --%>
    <c:set var="variableEntryParam" scope="request" value="${variableEntry}"/>
    <c:forEach var="displayGroup" items="${displayGroups}">
    <tbody>
    <tr><th colspan="2" class="groupName"><c:out value="${displayGroup.name}"/></th></tr>
    <c:forEach var="displayItem" items="${displayGroup.displayItems}">
    <c:set var="displayItemParam" scope="request" value="${displayItem}"/>
    <tr>
        <c:url var="qMarkImageUrl" value="/css/images/qmark.png"/>
        <td class="attributeName">
            <c:out value="${displayItem.displayName}"/>:
            <c:if test="${displayItem.helpTextAsHtml != ''}">
                <a class="helpTextToggler"><img src="${qMarkImageUrl}" alt="?"/></a>
            </c:if>
        </td>
        <td class="attributeValue">
            <jsp:include page="fragments/${displayItem.fragmentName}"/>
			<div class="variableDef">${displayItem.helpTextAsHtml}</div>
        </td>
    </tr>
    </c:forEach>
    </tbody>
    </c:forEach>
    </table>
    <div class="actionButtons">
    <ol>
    <li><button id="runCalcButton" class="button-em" type="submit">Run Calculation</button></li>
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
