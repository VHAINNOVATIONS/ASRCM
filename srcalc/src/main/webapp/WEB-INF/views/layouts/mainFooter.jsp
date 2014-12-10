<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--
Only show the link if the user has access. This is just for user-friendliness,
of course. The real authorization check is done when making the request to one
of the /admin pages.
--%>
<sec:authorize url="/admin/models">
    <c:url var="adminHome" value="/admin/models" />
    <a href="${adminHome}">Administration</a>
</sec:authorize>