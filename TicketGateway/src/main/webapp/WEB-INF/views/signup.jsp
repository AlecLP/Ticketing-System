<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ page isELIgnored="false" %> 
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c" %>
 <%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<script src="https://code.jquery.com/jquery-3.6.3.min.js" integrity="sha256-pvPw+upLPUjgMXY0G+8O0xUf+/Im1MZjXxxgOcBQBXU=" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<head>
<meta charset="UTF-8">
<title>Register User</title>
</head>
	<body>
		<form id="registerForm">	
			Email: <input type='text' name='email'/></br>
			Password: <input type='password' name='password'/></br>
			Name: <input type='text' name='name'/></br>
			Department: <input type='text' name='department'/></br>
			Project: <input type='text' name='project'/></br>
			Manager Email: <input type='text' name='managerEmail'/></br>
			<input type='submit' value='Register' id='register'>
		</form>
		<button onclick="history.back()">Back</button>
		
		<script>
			$('#registerForm').submit(function(e) {
				e.preventDefault();

				const formData = {
					email: $('input[name=email]').val(),
					password: $('input[name=password]').val(),
					name: $('input[name=name]').val(),
					department: $('input[name=department]').val(),
					project: $('input[name=project]').val(),
					managerEmail: $('input[name=managerEmail]').val()
				};

				$.ajax({
					url: "/signup",
					method: 'POST',
					contentType: "application/json",
					data: JSON.stringify({ 
						email: formData.email, 
						password: formData.password 
					}),
					success: function () {
						$.ajax({
							url: "http://localhost:8282/createEmployee",
							method: "POST",
							contentType: "application/json",
							data: JSON.stringify({
								email: formData.email, 
								name: formData.name, 
								department: formData.department, 
								project: formData.project, 
								managerEmail: formData.managerEmail, 
							}),
							success: function(){
								alert('Registration complete!')
							},
							error: function() {
								alert('Employee creation failed.')
							}
						})
					},
					error: function() {
						alert('Account creation failed.')
					}
				});
			});
		</script>
	</body>
</html>