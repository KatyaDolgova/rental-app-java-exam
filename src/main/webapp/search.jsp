<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("title", "Поиск жилья");
    request.setAttribute("content", "search-content.jsp");
%>
<jsp:include page="/WEB-INF/layout.jsp"/>