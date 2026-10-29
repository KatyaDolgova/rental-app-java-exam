<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("title", "Мои объявления");
    request.setAttribute("content", "my-properties-content.jsp");
%>
<jsp:include page="/WEB-INF/layout.jsp"/>