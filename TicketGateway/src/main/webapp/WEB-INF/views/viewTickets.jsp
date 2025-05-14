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
		<c:if test="${not empty tickets}">
			<table border="1">
		        <thead>
		            <tr>
		                <th>ID</th>
						<th>Title</th>
						<th>Description</th>
						<th>Created By</th>
						<th>Assignee</th>
						<th>Priority</th>
						<th>Status</th>
						<th>Date Created</th>
						<th>Category</th>
						<th>File</th>
						<th>History</th>
		            </tr>
		        </thead>
		        <tbody>
		            <c:forEach var="ticket" items="${tickets}">
		                <tr>
		                    <td>${ticket.id}</td>
							<td>${ticket.title}</td>
							<td>${ticket.description}</td>
							<td>${ticket.createdBy.email}</td>
							<td>
							    <c:choose>
							        <c:when test="${not empty ticket.assignee}">
							            ${ticket.assignee.email}
							        </c:when>
							        <c:otherwise>
							            N/A
							        </c:otherwise>
							    </c:choose>
							</td>
							<td>${ticket.priority}</td>
							<td>${ticket.status}</td>
							<td>${ticket.ticket_date}</td>
							<td>${ticket.category}</td>
							<td>
							    <c:choose>
									<c:when test="${ticket.hasFile}">
								        <a href="http://localhost:8282/tickets/${ticket.id}/download">
								            <button type="button">Download File</button>
								        </a>
								    </c:when>
								    <c:otherwise>
								        No File
								    </c:otherwise>
							    </c:choose>
							</td>
							<td>
								<a href="/viewHistory/${ticket.id}">
						            <button type="button">View History</button>
						        </a>
		                    </td>
		                </tr>
		            </c:forEach>
		        </tbody>
		    </table>
			<button onclick="history.back()">Back</button>
		</c:if>
		<c:if test="${empty tickets}">
			No Tickets Found
		</c:if>
    </security:authorize>
</body>
</html>
