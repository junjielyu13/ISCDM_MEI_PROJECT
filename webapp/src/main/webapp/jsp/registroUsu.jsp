<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Usuario</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 40%;
            margin: 50px auto;
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
        input[type="text"], input[type="email"], input[type="password"] {
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
        .success {
            color: green;
            font-size: 14px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Formulario de Registro</h2>
      
    <form action="RegistroServlet" method="post">
        <!-- Nombre -->
        <label for="nombre">Nombre</label>
        <input type="text" id="nombre" name="nombre" required />

        <!-- Apellidos -->
        <label for="apellidos">Apellidos</label>
        <input type="text" id="apellidos" name="apellidos" required />

        <!-- Correo Electrónico -->
        <label for="correo">Correo Electrónico</label>
        <input type="email" id="correo" name="correo" required />

        <!-- Nombre de Usuario -->
        <label for="usuario">Nombre de Usuario</label>
        <input type="text" id="usuario" name="usuario" required />

        <!-- Contraseña -->
        <label for="password">Contraseña</label>
        <input type="password" id="password" name="password" required />

        <!-- Confirmación de Contraseña -->
        <label for="confirm_password">Confirmación de Contraseña</label>
        <input type="password" id="confirm_password" name="confirm_password" required />

        <!-- Submit Button -->
        <button type="submit" class="btn">Registrar</button>
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

</body>
</html>
