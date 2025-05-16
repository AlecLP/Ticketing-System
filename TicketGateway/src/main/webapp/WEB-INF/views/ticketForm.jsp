<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<script src="https://code.jquery.com/jquery-3.6.3.min.js" integrity="sha256-pvPw+upLPUjgMXY0G+8O0xUf+/Im1MZjXxxgOcBQBXU=" crossorigin="anonymous"></script>
<head>
    <title>Ticket Form</title>
</head>
<body>
    <security:authorize access="isAuthenticated()">
        
		<h2>Create Ticket</h2>

		<form id="ticketForm" enctype="multipart/form-data">
	        <label for="title">Title:</label><br/>
	        <input type="text" name="title" id="title" required/><br/><br/>

	        <label for="description">Description:</label><br/>
	        <input type="text" name="description" id="description" required/><br/><br/>

	        <label for="category">Category:</label><br/>
	        <input type="text" name="category" id="category" required/><br/><br/>
			
			<label for="priority">Priority:</label><br/>
			<select name="priority" id="priority" required>
				<option value="" disabled selected>Select priority</option>
			    <c:forEach var="priority" items="${priorities}">
			        <option value="${priority}">${priority}</option>
			    </c:forEach>
			</select><br/><br/>

	        <label for="files">Attach File(s):</label><br/>
	        <input type="file" name="files" id="files" multiple/><br/><br/>

	        <input type="submit" value="Submit"/>
	    </form>
		<button onclick="history.back()">Back</button>
	
    </security:authorize>
	<script>
		$('#ticketForm').submit(function(e) {
			e.preventDefault()
			
			const currentUser = "${pageContext.request.userPrincipal.name}"
			
			var formData = new FormData()
			
			var fileInput = document.getElementById("files")
			for (let i = 0; i < fileInput.files.length; i++) {
				formData.append('files', fileInput.files[i]);
			}
			
			const jsonData = {
				title: $('input[name=title]').val(),
				description: $('input[name=description]').val(),
				category: $('input[name=category]').val(),
				priority: $('#priority').val(),
				createdBy: currentUser,
			}
			
			formData.append('ticketData', new Blob([JSON.stringify(jsonData)], { type: "application/json" }))
			
			$.ajax({
				url: "http://localhost:8282/createTicket",
				type: "POST",
				data: formData,
				contentType: false,
				processData: false,
				success: function(response) {
					alert("Ticket Created Successfully!")
				},
				error: function(xhr, status, error){
					alert("Error Creating Ticket: ", error)
				}
			})
		})
	</script>
</body>
</html>
