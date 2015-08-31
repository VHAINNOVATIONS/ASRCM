<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="srcalc" %>

<srcalc:calcPage title="Please Login">

<section>
    <h2>Please Login</h2>
    <%-- This parameter name must match the Spring Security configuration. --%>
    <c:if test="${!empty param.login_failure}">
    <p class="error">Login failure. Please check your credentials and try again.</p>
    </c:if>
    <c:url var="loginTargetUrl" value="${srcalcUrls.loginTarget}" />
    <form id="vistaLoginForm" class="srcalcForm attributeEditForm"
        action="${loginTargetUrl}" method="post">
        <table>
        <tr>
            <td>Access Code:</td>
            <td><!-- Hide input per Access Code conventions -->
            <input type="password" id="accessCode" name="accessCode" autofocus /></td>
        </tr>
        <tr>
            <td>Verify Code:</td>
            <td><input type="password" id="verifyCode" name="verifyCode" /></td>
        </tr>
        <tr>
            <td>Division:</td>
            <td><input type="text" id="division" name="division" /> (e.g., 500)</td>
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

</srcalc:calcPage>
