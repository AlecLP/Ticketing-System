<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manager Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <script src="https://code.jquery.com/jquery-3.6.3.min.js"
            integrity="sha256-pvPw+upLPUjgMXY0G+8O0xUf+/Im1MZjXxxgOcBQBXU="
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" />
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>

    <style>
        body {
            background-color: #f7f9fc;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #2c3e50;
            color: #f7f9fc;
            padding: 1.5rem 2rem;
            font-size: 2rem;
            font-weight: bold;
            letter-spacing: 0.03em;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
        }

        .table-wrapper {
            width: 85%;
            margin: 2rem auto 0 auto;
        }

        table {
            width: 100%;
            table-layout: auto;
            background-color: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            border-collapse: collapse;
        }

        th, td {
            word-wrap: break-word;
            white-space: normal;
            padding: 0.75rem;
            text-align: center;
            vertical-align: middle;
        }

        .btn-custom {
            background-color: #16a085;
            color: white;
            font-weight: 600;
            border: none;
            padding: 0.5rem 1rem;
        }

        .btn-custom:hover {
            background-color: #13856b;
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
            font-weight: 500;
            border: none;
        }

        .btn-secondary:hover {
            background-color: #495057;
        }

        #ticketHistoryContainer {
            margin-top: 30px;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.05);
        }
    </style>
</head>
<body>
	<security:authorize access="isAuthenticated()">
	    <header>Manager Dashboard</header>
	
	    <main class="table-wrapper">
	        <table class="table table-bordered" id="ticketsTable">
	            <thead class="thead-light">
	                <tr>
	                    <th>Title</th>
	                    <th>Description</th>
	                    <th>Created By</th>
	                    <th>Assignee</th>
	                    <th>Priority</th>
	                    <th>Status</th>
	                    <th>Date</th>
	                    <th>Category</th>
	                    <th>Files</th>
	                    <th>History</th>
	                    <th>Action</th>
	                    <th>Comments</th>
	                </tr>
	            </thead>
	            <tbody>
	                <!-- JS will insert rows here -->
	            </tbody>
	        </table>
	    </main>
	
	    <div class="table-wrapper" id="ticketHistoryContainer" style="display: none;">
	        <h4>Ticket History</h4>
	        <div id="ticketHistoryContent"></div>
	    </div>
	
	    <div class="mt-4 d-flex gap-2 justify-content-center">
	        <button onclick="submitDecisions()" class="btn btn-custom">Submit Decisions</button>
	        <button onclick="history.back()" class="btn btn-secondary ml-2">Back</button>
	    </div>
	</security:authorize>
	<script>
		let currentHistoryTicketId = null;
		var currentUser = "${pageContext.request.userPrincipal.name}"
		$(document).ready(function() {
			$.ajax({
			    url: "http://localhost:8282/getManagerTickets",
			    type: "POST",
				data: JSON.stringify({
					email : currentUser
				}),
			    contentType: "application/json",
			    success: function(data) {
			        populateTicketsTable(data);
			    },
			    error: function(xhr, status, error) {
			        alert("Error fetching tickets: " + error);
			    }
			})
		})
		
		function populateTicketsTable(tickets) {
		    const tableBody = $("#ticketsTable tbody");
		    tableBody.empty();
		    tickets.forEach(ticket => {
				let downloadButton = 'None';

		        if (ticket.fileAttachmentPaths && ticket.fileAttachmentPaths.length > 0) {
					downloadButton = "<button onclick=\"downloadFiles("+ticket.id+")\">Download Files</button>"
		        }
				let actionsSelector = "<select class=\"ticketAction\" data-ticket-id=\""+ticket.id+"\"><option value=\"\">-- Select --</option><option value=\"APPROVED\">Approve</option><option value=\"REJECTED\">Reject</option></select>"
				let historyButton = "<button onclick=\"viewHistory("+ticket.id+")\">View History</button>"
				let comments = "<input type=\"text\" class=\"comments\" data-ticket-id=\""+ticket.id+"\"/>"
				tableBody.append("<tr><td>"+ticket.title+"</td><td>"+ticket.description+"</td><td>"+ticket.createdBy+"</td><td>"+ticket.assignee+"</td><td>"+ticket.priority+"</td><td>"+ticket.status+"</td><td>"+(ticket.ticket_date || '').split('T')[0]+"</td><td>"+ticket.category+"</td><td>"+downloadButton+"</td><td>"+historyButton+"</td><td>"+actionsSelector+"</td><td>"+comments+"</td></tr>");
		    });
		}
		
		function downloadFiles(ticketId) {
		    const link = document.createElement('a');
		    link.href = "http://localhost:8282/download/"+ticketId;
		    link.download = `ticket_${ticketId}_attachments.zip`;
		    document.body.appendChild(link);
		    link.click();
		    document.body.removeChild(link);
		}
		
		function submitDecisions() {
		    const decisions = [];

		    $(".ticketAction").each(function() {
		        const ticketId = $(this).data("ticket-id");
		        const status = $(this).val();

		        if (status === "APPROVED" || status === "REJECTED") {
					const comment = $(".comments[data-ticket-id='" + ticketId + "']").val() || "";
		            decisions.push({
		                ticketId: ticketId,
		                status: status,
						email: currentUser,
						comments: comment
		            });
		        }
		    });

		    if (decisions.length === 0) {
		        alert("No decisions to submit.");
		        return;
		    }

		    $.ajax({
		        url: "http://localhost:8282/updateTicketStatuses",
		        type: "POST",
		        contentType: "application/json",
		        data: JSON.stringify(decisions),
		        success: function(response) {
		            alert("Decisions submitted successfully.");
		            location.reload();
		        },
		        error: function(xhr, status, error) {
		            alert("Error submitting decisions: " + error);
		        }
		    });
		}
		
		function viewHistory(ticketId) {
			const container = $("#ticketHistoryContainer");
            const content = $("#ticketHistoryContent");
			
			if (currentHistoryTicketId === ticketId && container.is(":visible")) {
		        container.hide();
		        currentHistoryTicketId = null;
		        return;
	    	}
		    currentHistoryTicketId = ticketId;
			
		    $.ajax({
		        url: "http://localhost:8282/ticketHistory/"+ticketId,
		        type: "GET",
		        success: function(historyList) {
		            if (!historyList || historyList.length === 0) {
		                content.html("<p>No history found for this ticket.</p>");
		                container.show();
		                return;
		            }

		            let historyHtml = "<ul>";
		            historyList.forEach(entry => {
		                historyHtml += "<li><strong>Action:</strong> "+entry.action+"<br/><strong>By:</strong> "+entry.actionBy+"<br/><strong>Date:</strong> "+entry.actionDate.split('T')[0]+"<br/><strong>Comments:</strong> "+entry.comments+"</li><hr/>"
		            });
		            historyHtml += "</ul>";

		            content.html(historyHtml);
		            container.show();
		        },
		        error: function(xhr, status, error) {
		            alert("Error fetching history: " + error);
		        }
		    });
		}

	</script>
</body>
</html>
