<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="srcalc" %>

<srcalc:adminPage title="Please Login">

<section>
    <h2>Please Login</h2>
    <%-- This parameter name must match the Spring Security configuration. --%>
    <c:if test="${!empty param.login_failure}">
    <p class="error">Login failure. Please check your credentials and try again.</p>
    </c:if>
    <c:url var="loginTargetUrl" value="${srcalcUrls.adminLoginTarget}" />
    <form id="adminLoginForm" class="srcalcForm attributeEditForm"
        action="${loginTargetUrl}" method="post">
        <table>
        <tr>
            <td>Username:</td>
            <td><input type="text" id="username" name="username" autofocus /></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" id="password" name="password" /></td>
        </tr>
        </table>
        <div class="actionButtons">
        <!-- There is only one button, but use an <ol> anyway for consistency. -->
        <ol>
        <li><button type="submit">Log In</button></li>
        </ol>
        </div>
    </form>
</section>

</srcalc:adminPage>
