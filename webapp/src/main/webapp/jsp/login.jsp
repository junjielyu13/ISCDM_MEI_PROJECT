<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar SesiÃ³n</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 40%;
            margin: 100px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        label {
            margin: 10px 0 5px;
        }
        input[type="text"], input[type="password"] {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        .btn {
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .btn:hover {
            background-color: #45a049;
        }
        .error {
            color: red;
            font-size: 14px;
            margin-bottom: 10px;
        }
        .register-btn {
            text-align: center;
            margin-top: 10px;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Login</h2>

    <!-- Login form -->
    <form action="userServlet" method="post">
        <input type="hidden" name="action" value="login" />
        <!-- Usuario -->
        <label for="usuario">Username:</label>
        <input type="text" id="username" name="username" required />

        <!-- ContraseÃ±a -->
        <label for="password">Password</label>
        <input type="password" id="password" name="password" required />

        <!-- Login Button -->
        <button type="submit" class="btn">Login</button>
    </form>

    <!-- Register Button -->
    <div class="register-btn">
        <a href="registroUsu.jsp" class="btn">Registrarse</a>
    </div>
	
	<!-- Display error message if login failed -->
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>
</div>

</body>
</html>
