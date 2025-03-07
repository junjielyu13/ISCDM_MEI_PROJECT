<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                <th>TÃ­tulo</th>
                <th>Autor</th>
                <th>Fecha de CreaciÃ³n</th>
                <th>DuraciÃ³n (min)</th>
                <th>Reproducciones</th>
                <th>DescripciÃ³n</th>
                <th>Formato</th>
            </tr>
        </thead>
        <tbody>
            <%
                // Get the result set from the request attribute
                ResultSet rs = (ResultSet) request.getAttribute("videoList");
                try {
                    // Iterate through the result set and display each video
                    while (rs.next()) {
                        String identificador = rs.getString("identificador");
                        String titulo = rs.getString("titulo");
                        String autor = rs.getString("autor");
                        String fechaCreacion = rs.getString("fecha_creacion");
                        int duracion = rs.getInt("duracion");
                        int reproducciones = rs.getInt("numero_reproducciones");
                        String descripcion = rs.getString("descripcion");
                        String formato = rs.getString("formato");
            %>
                        <tr>
                            <td><%= identificador %></td>
                            <td><%= titulo %></td>
                            <td><%= autor %></td>
                            <td><%= fechaCreacion %></td>
                            <td><%= duracion %></td>
                            <td><%= reproducciones %></td>
                            <td><%= descripcion %></td>
                            <td><%= formato %></td>
                        </tr>
            <%
                    }
                } catch (SQLException e) {
                    out.println("<p>Error al recuperar los videos: " + e.getMessage() + "</p>");
                }
            %>
        </tbody>
    </table>
</div>

</body>
</html>
