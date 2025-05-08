<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Ticket Form</title>
</head>
<body>
    <security:authorize access="isAuthenticated()">
        
		<h2>Create Ticket</h2>

	    <form action="${pageContext.request.contextPath}/createTicket" method="post" enctype="multipart/form-data">
	        <label for="title">Title:</label><br/>
	        <input type="text" name="title" id="title" required/><br/><br/>

	        <label for="description">Description:</label><br/>
	        <input type="text" name="description" id="description" required/><br/><br/>

	        <label for="category">Category:</label><br/>
	        <input type="text" name="category" id="category" required/><br/><br/>
			
			<label for="category">Priority:</label><br/>
			<input type="text" name="priority" id="priority" required/><br/><br/>

	        <label for="file">Attach File:</label><br/>
	        <input type="file" name="file" id="file"/><br/><br/>

	        <input type="submit" value="Submit"/>
	    </form>
	
    </security:authorize>
</body>
</html>
