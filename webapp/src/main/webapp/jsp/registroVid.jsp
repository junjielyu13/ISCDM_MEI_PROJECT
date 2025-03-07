<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar Video</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }
        .form-container {
            width: 50%;
            margin: 50px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            background-color: #fff;
        }
        .form-container h2 {
            text-align: center;
        }
        .form-container label {
            display: block;
            margin: 10px 0 5px;
        }
        .form-container input, .form-container textarea, .form-container select {
            width: 100%;
            padding: 8px;
            margin: 5px 0 15px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .form-container input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
        }
        .form-container input[type="submit"]:hover {
            background-color: #45a049;
        }
        .error {
            color: red;
            font-weight: bold;
            text-align: center;
        }
    </style>
</head>
<body>

    <div class="form-container">
        <h2>Registrar Video</h2>

        <!-- Error Message (only show if there is an error) -->
        <div class="error" id="errorMessage" style="display: none;">
            Error: Ya existe un video con el mismo Identificador. Por favor, intente con otro ID.
        </div>

        <form id="videoForm" action="/submitVideo" method="post">
            <!-- Identificador -->
            <label for="identificador">Identificador:</label>
            <input type="text" id="identificador" name="identificador" required>

            <!-- TÃ­tulo -->
            <label for="titulo">TÃ­tulo:</label>
            <input type="text" id="titulo" name="titulo" required>

            <!-- Autor -->
            <label for="autor">Autor:</label>
            <input type="text" id="autor" name="autor" required>

            <!-- Fecha de CreaciÃ³n -->
            <label for="fecha_creacion">Fecha de CreaciÃ³n:</label>
            <input type="date" id="fecha_creacion" name="fecha_creacion" required>

            <!-- DuraciÃ³n -->
            <label for="duracion">DuraciÃ³n (en minutos):</label>
            <input type="number" id="duracion" name="duracion" min="1" required>

            <!-- NÃºmero de Reproducciones -->
            <label for="numero_reproducciones">NÃºmero de Reproducciones:</label>
            <input type="number" id="numero_reproducciones" name="numero_reproducciones" min="0" value="0" required>

            <!-- DescripciÃ³n -->
            <label for="descripcion">DescripciÃ³n:</label>
            <textarea id="descripcion" name="descripcion" rows="4" required></textarea>

            <!-- Formato -->
            <label for="formato">Formato:</label>
            <select id="formato" name="formato" required>
                <option value="MP4">MP4</option>
                <option value="AVI">AVI</option>
                <option value="MKV">MKV</option>
                <option value="MOV">MOV</option>
                <option value="FLV">FLV</option>
                <option value="WMV">WMV</option>
            </select>

            <!-- Submit Button -->
            <input type="submit" value="Registrar Video">
        </form>
    </div>

    <script>
        // Assuming the form is submitted via Ajax (fetch, XMLHttpRequest, etc.)
        document.getElementById("videoForm").addEventListener("submit", function(event) {
            event.preventDefault(); // Prevent the form from submitting normally

            const formData = new FormData(this);
            
            // Example of a fake API call to check for duplicate ID (you should implement this on the backend)
            fetch("/checkVideoId", {
                method: "POST",
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    // If there's an error (duplicate ID), display the error message
                    document.getElementById("errorMessage").style.display = "block";
                } else {
                    // If no error, proceed to submit the form
                    // You can submit the form here or use Ajax to save the data
                    this.submit(); // Proceed with normal form submission
                }
            })
            .catch(error => console.error('Error:', error));
        });
    </script>

</body>
</html>
