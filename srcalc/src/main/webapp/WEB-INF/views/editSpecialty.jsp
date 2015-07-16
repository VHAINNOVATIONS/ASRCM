<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:set var="pageTitle" value="Edit Specialty"/>

<srcalc:adminPage title="${pageTitle}">

<section>
    <h2>${pageTitle} ${specialty.name}</h2>
    <span>Use this page to identify the set of models outcomes that will be calculated when the user selects this specialty.</span>
    
    <%-- Calculate the full URL for form submission. --%>
    <form:form id="editSpecialtyForm" cssClass="srcalcForm attributeEditForm"
        method="post" modelAttribute="specialty" enctype="multipart/form-data">
    <%-- Display any object-wide errors --%><form:errors cssClass="error"/>
    <%-- See srcalc.css for why HTML tables. --%>
    <table>
    <tbody>
        <tr>
        <td class="attributeName">Name</td>
        <td>
            <form:input autocomplete="false" path="name" size="${srcalc:min( maxDisplayNameLength, 60) }" />
            <form:errors path="name" cssClass="error" />
        </td>
        </tr>
        <tr>
        <td class="attributeName">Included Models</td>
        <td id="IncludedModels">
          <ol>
           <c:set var="m" value="0" />
              
           <c:forEach var="selectableModel" items="${specialty.selectableModels}">
           
              <li>
                <label><form:checkbox path="selectableModels[${m}].included" /> <c:out value="${selectableModel.modelName}"/></label>
                <form:errors cssClass="error" path="${selectableModel}.*" />
              </li>

              <c:set var="m" value="${m+1}" />
           </c:forEach> 
           </ol>       
        </td>
        </tr>
    </tbody>
    </table>
   
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