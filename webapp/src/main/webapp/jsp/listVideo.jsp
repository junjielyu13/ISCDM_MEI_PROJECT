<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>List of Registered Videos</title>
    <link rel="stylesheet" href="../assets/index.css">
</head>
<body>

<div class="container-list-video">
    <h2>List of Registered Videos</h2>

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
            </tr>
        </thead>
        <tbody id="videoTableBody">
            <tr><td colspan="8" style="text-align:center;">Loading videos...</td></tr>
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
