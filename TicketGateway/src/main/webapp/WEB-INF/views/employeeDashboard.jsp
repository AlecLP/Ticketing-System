<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Employee Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" />

    <style>
        body {
            background: #f7f9fc;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            min-height: 100vh;
            margin: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        header {
            width: 100%;
            padding: 2rem 1rem;
			background-color: #2c3e50;
			color: #f7f9fc;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            text-align: left;
            font-size: 2rem;
            font-weight: 700;
            letter-spacing: 0.05em;
            margin-bottom: 3rem;
        }

        main {
            width: 100%;
            max-width: 400px;
            display: flex;
            flex-direction: column;
            gap: 20px;
            padding: 0 1rem;
        }

        button.dashboard-btn {
            padding: 15px 25px;
            font-size: 1.1rem;
            border-radius: 8px;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s ease, box-shadow 0.3s ease;
            width: 100%;
            font-weight: 600;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }

		button.dashboard-btn.user {
		    background-color: #1abc9c; /* lighter teal */
		    color: white;
		}
		button.dashboard-btn.user:hover {
		    background-color: #16a085;
		    box-shadow: 0 4px 12px rgba(22, 160, 133, 0.4);
		}

		button.dashboard-btn.manager {
		    background-color: #d4a017; /* warm amber */
		    color: white;
		}
		button.dashboard-btn.manager:hover {
		    background-color: #b3860e; /* deeper golden amber */
		    box-shadow: 0 4px 12px rgba(212, 160, 23, 0.4);
		}

		button.dashboard-btn.admin {
		    background-color: #8e4b4b; /* muted earthy red */
		    color: white;
		}
		button.dashboard-btn.admin:hover {
		    background-color: #6f2d2d;
		    box-shadow: 0 4px 12px rgba(143, 61, 61, 0.4);
		}

		button.logout-btn {
		    background-color: #95a5a6; /* muted gray-blue */
		    color: white;
		    margin-top: 40px;
		}
		button.logout-btn:hover {
		    background-color: #7f8c8d;
		    box-shadow: 0 4px 12px rgba(127, 140, 141, 0.4);
		}

    </style>
</head>
<body>
    <header>
        Welcome, <security:authentication property="principal.username" />
    </header>

    <main>
        <security:authorize access="hasAuthority('USER')">
            <button class="dashboard-btn user" onclick="location.href='/ticketForm'">Create Ticket</button>
            <button class="dashboard-btn user" onclick="location.href='/viewTickets'">View Tickets</button>
        </security:authorize>

        <security:authorize access="hasAuthority('MANAGER')">
            <button class="dashboard-btn manager" onclick="location.href='/managerDashboard'">Manager Dashboard</button>
        </security:authorize>

        <security:authorize access="hasAuthority('ADMIN')">
            <button class="dashboard-btn admin" onclick="location.href='/adminDashboard'">Admin Dashboard</button>
        </security:authorize>

        <button class="dashboard-btn logout-btn" onclick="location.href='/login?logout'">Logout</button>
    </main>
</body>
</html>
