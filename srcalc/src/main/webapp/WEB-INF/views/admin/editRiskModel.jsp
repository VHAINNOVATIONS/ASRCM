<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:set var="pageTitle" value="Edit Model"/>

<srcalc:adminPage title="${pageTitle}">

<section>
    <h2>${pageTitle} ${riskModel.modelName}</h2>
    <%-- Calculate the full URL for form submission. --%>
    <form:form id="editModelForm" cssClass="srcalcForm attributeEditForm"
        method="post" commandName="riskModel" enctype="multipart/form-data">
    <%-- Display any object-wide errors --%><form:errors cssClass="error"/>
    <%-- See srcalc.css for why HTML tables. --%>
    <table>
    <tbody>
        <tr>
        <td class="attributeName">Display Text</td>
        <td>
            <form:input autocomplete="false" path="modelName" size="${srcalc:min( riskModel.maxDisplayNameLength,60) }" />
            <form:errors path="modelName" cssClass="error" />
        </td>
        </tr>
        <tr>
        <!-- 
        <td class="attributeName">Specialties</td>
        <td>
        <ul>       
        <c:forEach var="specialty" items="${riskModel.specialties }">
            <li>${specialty.name}</li>
        </c:forEach>
        </ul>
        </td>
         -->
        </tr>
        <tr>
        <td class="attributeName">Summation Terms</td>
        <td>
            <c:if test="${!empty uploadErrors}">
            <div class="error">
            <p>The uploaded term set was not valid. The below terms have not been modified.</p>
            <p>Errors were:</p>
            <ul>
                <c:forEach var="error" items="${uploadErrors}">
                <li>${error.locationPrefix}<spring:message message="${error}" /></li>
                </c:forEach>
            </ul>
            </div>
            </c:if>
            <table>
            <tbody>
            <tr>
               <th align="left" >Term</th>
               <th align="left">Type</th>
               <th align="left" class="main">Coefficient</th>           
            </tr>
            <%-- We iterate over the termSummaries (via forEach) and the terms themselves
                 (via this path string) concurrently as they are in the same order. --%>
            <c:set var="i" value="0" />
            <c:forEach var="termSummary" items="${termSummaries}">
            <c:set var="termPath" value="terms[${i}]" />
            <tr>
                <%-- Include any post-parsing validation errors inline. --%>
                <td>
                ${termSummary.identificationString}
                <form:hidden path="${termPath}.termType"/>
                <form:hidden path="${termPath}.key"/>
                <form:hidden path="${termPath}.optionValue"/>
                <form:hidden path="${termPath}.coefficient"/>
                <form:errors path="${termPath}.*" cssClass="error" />
                </td>
                <td>${termSummary.termType}
                <td>${termSummary.coefficient}</td>
            </tr>
            <c:set var="i" value="${i+1}" />
            </c:forEach>
             
            </tbody>
            </table>
            <p><a id="importRiskModel" class="btn-link" href="#">Import New Model</a>
            (Note this will overwrite the displayed terms.)</p>
            <div id="termsUploadControls">
            <c:url var="sampleUploadUrl" value="/admin/resources/sample_terms_upload.csv" />
            <p><a href="${sampleUploadUrl}">Here</a> is a sample CSV file.</p>
            <input type="file" name="termsFile" accept=".csv">
            <button type="submit" name="uploadTerms" value="uploadTerms">Import</button>
            </div>
        </td>        
        </tr>
    </tbody>
    </table>
   
    <div class="actionButtons">
    <ol>
    <li><c:url var="cancelUrl" value="${srcalcUrls.modelAdminHome}" />
        <a class="btn-default" href="${cancelUrl}">Cancel</a></li>
    <li><button class="button-em" type="submit">Save Changes</button></li>
    </ol>
    </div>
    
    </form:form>
    
    <script>
    $(document).ready(function() {
        // Handle the import.
        $('#importRiskModel').on('click', function (event) {
            event.preventDefault();
            $('#termsUploadControls').slideToggle(200);
        });
    });
    </script>
</section>
</srcalc:adminPage>