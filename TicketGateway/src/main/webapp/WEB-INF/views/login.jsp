<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="frm" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Login - Ticket Gateway</title>
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
	  color: #f7f9fc;
      flex: 0 0 40%;
	  display: flex;
	  align-items: center;
	  justify-content: center;
	  padding: 40px;
    }

    .right-pane {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #f7f9fc;
      padding: 40px;
    }

    .login-box {
      width: 100%;
      max-width: 400px;
    }

    .login-box h3 {
      margin-bottom: 30px;
      color: #2c3e50;
      font-weight: 600;
    }

	.btn-custom {
      background-color: #16a085;
      color: white;
    }

    .btn-custom:hover {
      background-color: #12806c;
    }

    .form-control {
      border-radius: 6px;
    }
  </style>
</head>
<body>
  <div class="split-container">
    <div class="left-pane">
		<h1>Welcome</h1>
	</div>
    <div class="right-pane">
      <div class="login-box">
        <h3>Login to Ticket Gateway</h3>

        <c:if test="${not empty Message}">
          <div class="alert alert-danger">${Message}</div>
        </c:if>

        <frm:form action="login" method="post">
          <div class="form-group">
            <label for="email">Email</label>
            <input class="form-control" type="email" name="email" id="email" required />
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <input class="form-control" type="password" name="password" id="password" required />
          </div>

          <div class="mb-3">
            <span>Don't have an account? <a href="/register">Register here</a></span>
          </div>

          <input type="submit" class="btn btn-custom btn-block" value="Log In" />
          <sec:csrfInput />
        </frm:form>
      </div>
    </div>
  </div>
</body>
</html>
