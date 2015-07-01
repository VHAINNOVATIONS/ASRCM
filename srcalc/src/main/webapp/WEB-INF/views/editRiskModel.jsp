<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:set var="pageTitle" value="Edit Model"/>

<srcalc:adminPage title="${pageTitle}">

<section>
    <h2>${pageTitle} ${riskModel.modelName}</h2>
    <%-- Calculate the full URL for form submission. --%>
    <c:url var="fullSaveUrl" value="/admin/models/${riskModel.id}"/>
    <!-- TODO : Change this to appropriate or new class -->
    <form:form id="editModelForm" cssClass="srcalcForm attributeEditForm"
        method="post" action="${fullSaveUrl}" commandName="riskModel">
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
        <td class="attributeName">Specialties</td>
        <td>
        <ul>       
        <c:forEach var="specialty" items="${riskModel.specialties }">
            <li>${specialty.name}</li>
        </c:forEach>
        </ul>
        </td>
        </tr>
        <tr>
        <td class="attributeName">Summation Terms</td>
        <td>
			<table>
			<tbody>
			<tr>
			   <th align="left" >Term</th>
			   <th align="left" class="main">Type</th>
			   <th align="left" class="main">Coefficient</th>		   
			</tr>
			<c:forEach var="term" items="${riskModel.termSummaries}">
			<tr>
			    <td>${term.displayName}</td>
			    <td class="attributeName">${term.termType}</td>
			    <td class="attributeName">${term.coefficient}</td>
			</tr>
			</c:forEach>
			 
			</tbody>
			</table>
		</td>        
        </tr>
   	</tbody>
  	</table>
	<span>
	<a id="importRiskModel" class="btn-link" href="#">Import New Model</a>  (Note this will overwrite the displayed terms.)</span>
   
    <div class="actionButtons">
    <ol>
    <li><c:url var="cancelUrl" value="/admin" />
        <a class="btn-default" href="${cancelUrl}">Cancel</a></li>
    <li><button class="button-em" type="submit">Save Changes</button></li>
    </ol>
    </div>
    
    
    </form:form>
</section>
</srcalc:adminPage>