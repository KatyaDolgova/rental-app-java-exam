<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("title", "Брони на моё жильё");
    request.setAttribute("content", "owner-bookings-content.jsp");
%>
<jsp:include page="/WEB-INF/layout.jsp"/>
