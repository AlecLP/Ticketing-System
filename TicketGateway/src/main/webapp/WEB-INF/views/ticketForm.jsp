<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Create Ticket</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <script src="https://code.jquery.com/jquery-3.6.3.min.js"
            integrity="sha256-pvPw+upLPUjgMXY0G+8O0xUf+/Im1MZjXxxgOcBQBXU="
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" />
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>

    <style>
        body, html {
            height: 100%;
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .split-left {
            background-color: #2c3e50;
            color: #f7f9fc;
            width: 40%;
            height: 100%;
            float: left;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2rem;
            font-weight: bold;
        }

        .split-right {
            width: 60%;
            height: 100%;
            float: right;
            background-color: #f7f9fc;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .form-container {
            width: 100%;
            max-width: 500px;
        }

        .form-group label {
            font-weight: 600;
        }

        .btn-custom {
            background-color: #16a085;
            color: white;
            font-weight: 600;
            border: none;
            transition: background-color 0.3s ease;
        }

        .btn-custom:hover {
            background-color: #13856b;
        }

        .btn-back {
            background-color: #6c757d;
            color: white;
            border: none;
        }

        .btn-back:hover {
            background-color: #495057;
        }
    </style>
</head>
<body>
<security:authorize access="isAuthenticated()">
    <div class="split-left">
        Ticket Form
    </div>

    <div class="split-right">
        <form id="ticketForm" enctype="multipart/form-data" class="form-container">
            <h4 class="mb-4">Create Ticket</h4>

            <div class="form-group">
                <label for="title">Title:</label>
                <input type="text" name="title" id="title" class="form-control" required />
            </div>

            <div class="form-group">
                <label for="description">Description:</label>
                <input type="text" name="description" id="description" class="form-control" required />
            </div>

            <div class="form-group">
                <label for="category">Category:</label>
                <input type="text" name="category" id="category" class="form-control" required />
            </div>

            <div class="form-group">
                <label for="priority">Priority:</label>
                <select name="priority" id="priority" class="form-control" required>
                    <option value="" disabled selected>Select priority</option>
                    <c:forEach var="priority" items="${priorities}">
                        <option value="${priority}">${priority}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="files">Attach File(s):</label>
                <input type="file" name="files" id="files" class="form-control-file" multiple />
            </div>

            <button type="submit" class="btn btn-custom btn-block mt-3">Submit</button>
            <button type="button" onclick="history.back()" class="btn btn-back btn-block mt-2">Back</button>
        </form>
    </div>
</security:authorize>

<script>
    $('#ticketForm').submit(function(e) {
        e.preventDefault();

        const currentUser = "${pageContext.request.userPrincipal.name}";
        var formData = new FormData();

        var fileInput = document.getElementById("files");
        for (let i = 0; i < fileInput.files.length; i++) {
            formData.append('files', fileInput.files[i]);
        }

        const jsonData = {
            title: $('#title').val(),
            description: $('#description').val(),
            category: $('#category').val(),
            priority: $('#priority').val(),
            createdBy: currentUser,
        };

        formData.append('ticketData', new Blob([JSON.stringify(jsonData)], { type: "application/json" }));

        $.ajax({
            url: "http://localhost:8282/createTicket",
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            success: function() {
                alert("Ticket Created Successfully!");
            },
            error: function(xhr, status, error) {
                alert("Error Creating Ticket: " + error);
            }
        });
    });
</script>
</body>
</html>
