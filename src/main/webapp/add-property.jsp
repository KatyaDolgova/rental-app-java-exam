<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("title", "Добавить жильё");
    request.setAttribute("content", "add-property-content.jsp");
%>
<jsp:include page="/WEB-INF/layout.jsp"/>