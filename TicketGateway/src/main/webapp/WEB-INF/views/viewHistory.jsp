<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Manager Dashboard</title>
</head>
<body>
    <security:authorize access="isAuthenticated()">
        
		<h2>Tickets</h2>
		<c:if test="${not empty histories}">
			<table border="1">
		        <thead>
		            <tr>
		                <th>Ticket ID</th>
						<th>Action</th>
						<th>Action By</th>
						<th>Action Date</th>
						<th>Comments</th>
		            </tr>
		        </thead>
		        <tbody>
		            <c:forEach var="history" items="${histories}">
		                <tr>
		                    <td>${history.ticketId}</td>
							<td>${history.action}</td>
							<td>${history.actionBy}</td>
							<td>${history.actionDate}</td>
							<td>${history.comments}</td>
		                </tr>
		            </c:forEach>
		        </tbody>
		    </table>
			<button onclick="history.back()">Back</button>
		</c:if>
		<c:if test="${empty histories}">
			No History
		</c:if>
    </security:authorize>
</body>
</html>
