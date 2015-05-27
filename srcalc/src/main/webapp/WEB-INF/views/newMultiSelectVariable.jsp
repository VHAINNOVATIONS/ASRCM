<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<srcalc:editVariablePage variable="${variable}" isNewVariable="true" saveUrl="${SAVE_URL}">
        <tr>
        <td>Display Type:</td>
        <td>
            <span class="springGeneratedRadios">
            <form:radiobuttons cssClass="springFormGenerated" path="displayType" items="${variable.allDisplayTypes}"/>
            </span>
        </td>
        </tr>
        <tr>
        <td><p>Options:</p>(Trailing blanks will be omitted.)</td>
        <td id="multiSelectOptions">
            <%-- Display any overall errors --%><form:errors path="options" cssClass="error" />
            <ol id="multiSelectOptionsList">
            <%--
            Insert the existing options. Javascript will handle adding/removing
            as the user desires.
            --%>
            <c:set var="i" value="0" />
            <c:forEach items="${variable.options}">
            <li><form:input path="options[${i}]"/> <form:errors path="options[${i}]" cssClass="error"/></li>
            <c:set var="i" value="${i+1}" />
            </c:forEach>
            </ol>
            <a id="addNewOption" href="#" data-max-options="${variable.maxOptions}">Add Another</a>
        </td>
        </tr>
</srcalc:editVariablePage>