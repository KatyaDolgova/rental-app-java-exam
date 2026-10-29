<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("title", "Регистрация");
    request.setAttribute("content", "register-content.jsp");
%>
<jsp:include page="/WEB-INF/layout.jsp"/>