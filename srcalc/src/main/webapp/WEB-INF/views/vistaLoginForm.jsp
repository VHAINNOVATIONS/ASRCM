<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="srcalc" %>

<srcalc:calcPage title="Please Login">

<section>
    <h2>Please Login</h2>
    <c:choose>
    <%-- This parameter name must match the Spring Security configuration. --%>
    <c:when test="${empty param.login_failure}">
    <%-- Only attempt CCOW auth if this is our first attempt. --%>
    <p id="attemptingCcowMessage">Attempting Single Sign-On. Please wait...</p>
    <!-- Using some HTML5-obsolete technology here, but it's the only way to CCOW for now. -->
    <object id="ContextorControl"
        data="data:application/x-oleobject;base64,96x4h6lc0xGHJwBgsLXhNwADAADYEwAA2BMAAA=="
        classid="CLSID:8778ACF7-5CA9-11D3-8727-0060B0B5E137" name="ContextorControl">
    </object>
    </c:when>
    <c:otherwise>
    <p class="error">Login failure. Please check your credentials and try again.</p>
    </c:otherwise>
    </c:choose>
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

<script type="text/javascript">
// Attempt CCOW login once the page is ready.
$(document).ready(function() {

    var vistaLoginForm = $('#vistaLoginForm');
    // Cleanly abort if the ContextorControl is not present.
    if (!$('#ContextorControl').length) {
        vistaLoginForm.show();
        return;
    }
    var attemptingCcowMessage = $('#attemptingCcowMessage');
    var accessCodeInput = $('#accessCode');
    var divisionInput = $('#division');
    try {
        ContextorControl.Run(
                '${applicationScope['srcalc.appInfo'].longName}',
                '',             // no passcode necessary
                false);         // not surveyable - we don't care about context changes

        try {
            var contextItems = ContextorControl.CurrentContext;
            // Lookup the static keys by name.
            var vistalogon = contextItems.Item('user.id.logon.vistalogon').value;
            var vistatoken = contextItems.Item('user.id.logon.vistatoken').value;

            var division = extractDivision(vistalogon);
            divisionInput.val(division);
            accessCodeInput.val(vistatoken);
            vistaLoginForm.submit();
        } finally {
            // The Vergence server seems to require this at the end of the script
            // or else the next Run() call will fail.
            ContextorControl.Suspend();
        }
    } catch (ex) {
        attemptingCcowMessage.data('ccow-failure', ex.message);
        attemptingCcowMessage.text('Single Sign-On Failed. Please enter your credentials.');
        vistaLoginForm.slideDown(200);
        return;
    }

});

/**
 * Extracts the division specifier from the given VistA logon string of the format
 * "vistadomain^division". Returns an empty string if unable to extract.
 */
function extractDivision(vistaLogonString) {
    var separatorIndex = vistaLogonString.indexOf("^");
    if (separatorIndex == -1) {
        return "";
    } else {
        return vistaLogonString.substr(separatorIndex + 1);
    }
}
</script>

</srcalc:calcPage>
