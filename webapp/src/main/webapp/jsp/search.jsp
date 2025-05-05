<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Buscar Documentos</title>
</head>
<body>
    <h1>Buscar Documentos</h1>
    
    <form onsubmit="event.preventDefault(); searchVideos();">
        <label for="criterio">Buscar por:</label>
        <select name="criterio" id="criterio">
            <option value="autor">Autor</option>
            <option value="titulo">Título</option>
            <option value="fecha">Fecha de Creación</option>
        </select>

        <!-- Inputs para búsqueda -->
        <input type="text" name="valorBusqueda" id="textInput" placeholder="Escriba su búsqueda" required>
        <input type="date" name="valorBusqueda" id="dateInput" style="display:none;" required>

        <input type="submit" value="Buscar">
    </form>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Author</th>
                <th>Creation Date</th>
                <th>Duration (segons)</th>
                <th>Views</th>
                <th>Description</th>
                <th>Format</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody id="searchResults">
            <tr><td colspan="9" style="text-align:center;">Loading videos...</td></tr>
        </tbody>
    </table>

    <script>
        // Cambiar el tipo de input si seleccionan "fecha"
        document.getElementById('criterio').addEventListener('change', function () {
            const criterio = this.value;
            const textInput = document.getElementById('textInput');
            const dateInput = document.getElementById('dateInput');

            if (criterio === 'fecha') {
                textInput.style.display = 'none';
                textInput.removeAttribute('required');
                
                dateInput.style.display = 'inline';
                dateInput.setAttribute('required', 'true');
            } else {
                dateInput.style.display = 'none';
                dateInput.removeAttribute('required');
                
                textInput.style.display = 'inline';
                textInput.setAttribute('required', 'true');
            }
        });

        // Ejecutar la búsqueda con AJAX
        function searchVideos() {
            const criterio = document.getElementById('criterio').value;
            let valorBusqueda;

            if (criterio === 'fecha') {
                valorBusqueda = document.getElementById('dateInput').value;
            } else {
                valorBusqueda = document.getElementById('textInput').value;
            }
            console.log(valorBusqueda);
            fetch("searchVideoServlet?criterio=" + criterio + "&valorBusqueda=" + valorBusqueda, {
                method: 'GET'
            })
            .then(response => response.text())
            .then(html => {
                document.getElementById("searchResults").innerHTML = html;
            })
            .catch(error => console.error('Error fetching search results:', error));
        }
    </script>
</body>
</html>
