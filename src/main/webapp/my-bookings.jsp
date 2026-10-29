<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("title", "Мои бронирования");
    request.setAttribute("content", "my-bookings-content.jsp");
%>
<jsp:include page="/WEB-INF/layout.jsp"/>