<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:choose>
    <c:when test="${isNewRule}">
        <c:set var="pageTitle" value="New Rule"/>
    </c:when>
    <c:otherwise>
        <c:set var="pageTitle" value="Edit Rule"/>
    </c:otherwise>
</c:choose>

<srcalc:adminPage title="${pageTitle}">
<section>
    <h3>Required Values</h3>
    <form:form id="ruleEditForm" cssClass="srcalcForm attributeEditForm"
        method="post" action="${fullSaveUrl}" commandName="rule">
    Display name: <form:input path="displayName" size="80"/>
    <form:errors path="displayName" cssClass="error"/><br>
    <form:checkbox path="required"/> Require all value(s) in this rule.
    <h4>Summand Expression</h4>
    The expression will be added to the risk model's sum.<br>
    <form:input path="summandExpression" size="80"/>
    <form:errors path="summandExpression" cssClass="error"/><br>
    Available variables: ${rule.requiredVariables}<br>
    <%-- For each variable, give the type of variable, and a short description of the 
    options available. (i.e. range or categories)--%>
    <ul id="matchersList">
    <c:set var="i" value="0" />
    <c:forEach items="${rule.matchers}">
        <li>
            <c:set var="matcherPath" value="matchers[${i}]" />
            <c:set var="summary" value="${variableSummaries[rule.matchers[i].variableKey]}"/>
            <div class="matcherInputs">
            <h4>${summary.displayName}</h4>
            <p><c:out value="${summary.typeName} ${summary.optionString}"/></p>
            <p><label>Apply condition to variable: <form:checkbox class="applyCheckbox" path="${matcherPath}.enabled"/></label></p>
            <label>Expression: <form:input class="booleanExpression" path="${matcherPath}.booleanExpression" size="80"/></label>
            <form:errors path="${matcherPath}" cssClass="error"/>
            <form:hidden path="${matcherPath}.variableKey"/>
            </div>
            <form:errors cssClass="error" path="${matcherPath}.*" /><br>
            <button class="btn-link" type="submit"
                name="submitButton" value="${i}">Remove Variable</button>
        </li>
        <c:set var="i" value="${i+1}" />
    </c:forEach>
    </ul>
    <%-- Use a drop down to add a new matcher on the specified variable.--%>
    <br>
    <form:select path="newVariableKey">
        <form:options items="${allVariables}" itemLabel="key" itemValue="key"/>
    </form:select>
    <button class="btn-link" type="submit"
        name="submitButton" value="newMatcher">Add New Variable</button>
    <div class="actionButtons">
    <ol>
    <li><c:url var="cancelUrl" value="/admin" />
        <a class="btn-default" href="${cancelUrl}">Cancel</a></li>
    <li><button class="button-em" type="submit" name="submitButton" value="saveRule">Save Changes</button></li>
    </ol>
    </div>
    </form:form>
    <c:url var="editRuleJsUrl" value="/js/editRule.js"/>
    <script type="text/javascript" src="${editRuleJsUrl}"></script>
    <script type="text/javascript">
	    $(document).ready(function(){
	    	EDITRULES.initPage();
	    });
    </script>
</section>
</srcalc:adminPage>