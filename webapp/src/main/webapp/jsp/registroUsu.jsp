<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Registration</title>
    <link rel="stylesheet" href="../assets/index.css"> 
</head>
<body>
    <div class="container">
        <h2>Registration Form</h2>

        <form action="/jsp/userServlet" method="post">
            <input type="hidden" name="action" value="Register" />

            <!-- First Name -->
            <label for="nombre">First Name</label>
            <input type="text" id="nombre" name="name" required />

            <!-- Last Name -->
            <label for="apellidos">Last Name</label>
            <input type="text" id="apellidos" name="surname" required />

            <!-- Email -->
            <label for="correo">Email</label>
            <input type="email" id="correo" name="email" required />

            <!-- Username -->
            <label for="usuario">Username</label>
            <input type="text" id="usuario" name="username" required />

            <!-- Password -->
            <label for="password">Password</label>
            <input type="password" id="password" name="passwd" required />

            <!-- Confirm Password -->
            <label for="confirm_password">Confirm Password</label>
            <input type="password" id="confirm_password" name="confirm_password" required />

            <!-- Submit Button -->
            <button type="submit" class="btn">Register</button>
        </form>

        <!-- Display error message if it exists -->
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>

        <!-- Display success message if it exists -->
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>
    </div>

    <script>
        document.querySelector("form").addEventListener("submit", function(event) {
            var password = document.getElementById("password").value;
            var confirmPassword = document.getElementById("confirm_password").value;
            
            if (password !== confirmPassword) {
                alert("Passwords do not match!");
                event.preventDefault();
            }
        });
    </script>
</body>
</html>
