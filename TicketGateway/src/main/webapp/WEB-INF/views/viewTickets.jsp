<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<script src="https://code.jquery.com/jquery-3.6.3.min.js" integrity="sha256-pvPw+upLPUjgMXY0G+8O0xUf+/Im1MZjXxxgOcBQBXU=" crossorigin="anonymous"></script>
<html>
<head>
    <title>User Tickets</title>
</head>
<body>
	<h2>Tickets</h2>
	<security:authorize access="isAuthenticated()">
		<table border="1" id="ticketsTable">
		  <thead>
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
		    </tr>
		  </thead>
		  <tbody>
		    <!-- JS will insert rows here -->
		  </tbody>
		</table>
		<div id="ticketHistoryContainer" style="margin-top: 20px; display: none; border: 1px solid #ccc; padding: 10px;">
		    <h3>Ticket History</h3>
		    <div id="ticketHistoryContent"></div>
		</div>
		<button onclick="history.back()">Back</button>
	</security:authorize>
	<script>
		let currentHistoryTicketId = null;
		var currentUser = "${pageContext.request.userPrincipal.name}"
		$(document).ready(function() {
			$.ajax({
			    url: "http://localhost:8282/getUserTickets",
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
				let downloadButton = '';

		        if (ticket.fileAttachmentPaths && ticket.fileAttachmentPaths.length > 0) {
					downloadButton = "<button onclick=\"downloadFiles("+ticket.id+")\">Download Files</button>"
		        }
				let historyButton = "<button onclick=\"viewHistory("+ticket.id+")\">View History</button>"
			    tableBody.append("<tr><td>"+ticket.title+"</td><td>"+ticket.description+"</td><td>"+ticket.createdBy+"</td><td>"+ticket.assignee+"</td><td>"+ticket.priority+"</td><td>"+ticket.status+"</td><td>"+(ticket.ticket_date || '').split('T')[0]+"</td><td>"+ticket.category+"</td><td>"+downloadButton+"</td><td>"+historyButton+"</td></tr>");
		    });
		}
		
		function downloadFiles(ticketId) {
		    const link = document.createElement('a');
		    link.href = "http://localhost:8282/download/"+ticketId;
		    link.download = "ticket_"+ticketId+"_attachments.zip";
		    document.body.appendChild(link);
		    link.click();
		    document.body.removeChild(link);
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
