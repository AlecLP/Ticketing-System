<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Register - Ticket Gateway</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">

  <style>
    body, html {
      height: 100%;
      margin: 0;
      font-family: 'Segoe UI', sans-serif;
    }

    .split-container {
      display: flex;
      height: 100vh;
    }

    .left-pane {
      background-color: #2c3e50;
      flex: 0 0 40%;
	  display: flex;
  	  align-items: center;
  	  justify-content: center;
  	  padding: 40px;
	  color: #f7f9fc;
    }

    .right-pane {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #f7f9fc;
      padding: 40px;
    }

    .signup-box {
      width: 100%;
      max-width: 500px;
    }

    .signup-box h3 {
      margin-bottom: 25px;
      color: #2c3e50;
      font-weight: 600;
    }

    .form-control {
      border-radius: 6px;
    }

    .btn-custom {
      background-color: #16a085;
      color: white;
    }

    .btn-custom:hover {
      background-color: #12806c;
    }
  </style>
</head>
<body>
  <div class="split-container">
    <div class="left-pane">
		<h1>Register</h1>
	</div>
    <div class="right-pane">
      <div class="signup-box">
        <h3>Create Your Account</h3>
        <form id="registerForm">
          <div class="form-group">
            <label>Email</label>
            <input class="form-control" type="email" name="email" required />
          </div>

          <div class="form-group">
            <label>Password</label>
            <input class="form-control" type="password" name="password" required />
          </div>

          <div class="form-group">
            <label>Name</label>
            <input class="form-control" type="text" name="name" required />
          </div>

          <div class="form-group">
            <label>Department</label>
            <input class="form-control" type="text" name="department" />
          </div>

          <div class="form-group">
            <label>Project</label>
            <input class="form-control" type="text" name="project" />
          </div>

          <div class="form-group">
            <label>Manager Email</label>
            <input class="form-control" type="email" name="managerEmail" />
          </div>

          <button class="btn btn-custom btn-block mt-3" type="submit">Register</button>
          <button class="btn btn-secondary btn-block mt-2" type="button" onclick="history.back()">Back</button>
        </form>
      </div>
    </div>
  </div>

  <script src="https://code.jquery.com/jquery-3.6.3.min.js"></script>
  <script>
    $('#registerForm').submit(function(e) {
      e.preventDefault();
      const data = {
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
        data: JSON.stringify({ email: data.email, password: data.password }),
        success: function () {
          $.ajax({
            url: "http://localhost:8282/createEmployee",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function () {
              alert('Registration complete!');
              window.location.href = "/login";
            },
            error: function () {
              alert('Employee creation failed.');
            }
          });
        },
        error: function () {
          alert('Account creation failed.');
        }
      });
    });
  </script>
</body>
</html>
