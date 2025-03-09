<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Listado de Videos Registrados</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 80%;
            margin: 50px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Listado de Videos Registrados</h2>

    <table>
        <thead>
            <tr>
                <th>Identificador</th>
                <th>Título</th>
                <th>Autor</th>
                <th>Fecha de Creación</th>
                <th>Duración (min)</th>
                <th>Reproducciones</th>
                <th>Descripción</th>
                <th>Formato</th>
            </tr>
        </thead>
        <tbody id="videoTableBody">
            <tr><td colspan="8" style="text-align:center;">Cargando videos...</td></tr>
        </tbody>
    </table>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        fetch("videoListServlet") 
            .then(response => response.text()) 
            .then(html => {
                document.getElementById("videoTableBody").innerHTML = html;  
            })
            .catch(error => {
                console.error("Error al cargar videos:", error);
                document.getElementById("videoTableBody").innerHTML = "<tr><td colspan='8' style='text-align:center;color:red;'>Error al cargar videos</td></tr>";
            });
    });
</script>

</body>
</html>
