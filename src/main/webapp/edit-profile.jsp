<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("title", "Редактировать профиль");
    request.setAttribute("content", "edit-profile-content.jsp");
%>
<jsp:include page="/WEB-INF/layout.jsp"/>