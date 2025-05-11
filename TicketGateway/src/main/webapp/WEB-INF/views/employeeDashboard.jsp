<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Employee Dashboard</title>
</head>
<body>
    <security:authorize access="isAuthenticated()">
        <p>Welcome, 
            <span id="name">
                <security:authentication property="principal.username" />
            </span>
        </p>

        <!-- USER button -->
        <security:authorize access="hasAuthority('USER')">
            <button onclick="location.href='/ticketForm'">Create Ticket</button>
        </security:authorize>
		
		<!-- MANAGER button -->
		        <security:authorize access="hasAuthority('MANAGER')">
		            <button onclick="location.href='/managerDashboard'">Manager Dashboard</button>
		        </security:authorize>

        <!-- ADMIN button -->
        <security:authorize access="hasAuthority('ADMIN')">
            <button onclick="location.href=''">Admin Button</button>
        </security:authorize>
		
		<!-- LOGOUT button -->
		<button onclick=location.href="/login?logout">Logout</button>
    </security:authorize>
</body>
</html>
