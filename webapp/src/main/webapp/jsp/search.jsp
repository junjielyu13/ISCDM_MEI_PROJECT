<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="jakarta.servlet.http.HttpSession" %>
<%@page import="java.util.Objects" %>
<%@page import="model.User" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Search Video</title>
        <link rel="stylesheet" href="../assets/index.css">
    </head>
    <body class="container-list-video">
        <div class="user-info">
            <%
                HttpSession sessionObj = request.getSession();
                User user = (User) sessionObj.getAttribute("user");
                if (Objects.isNull(user)) {
                    response.sendRedirect("privacy.jsp");
                    return;
                }
            %>
            <p>Welcome, <%= user.getUsername() %>!  <a href="login.jsp">change user</a> </p>
        </div>


        <div>
            <div style="display: flex;
                 flex-direction: row;
                 align-items: center;
                 justify-content: space-between;">
                <div class="register-btn">
                    <a href="listVideo.jsp" class="btn">Back Video List</a>
                </div>

                <div class="register-btn">
                    <h2>Search Video</h2>  
                </div>
                <div>
                </div>
            </div>
        </div>

        <div class="search-form-container">
            <form onsubmit="event.preventDefault(); searchVideos();">
                <label for="criterio">Buscar por:</label>
                <select name="criterio" id="criterio">
                    <option value="autor">Author</option>
                    <option value="titulo">Title</option>
                    <option value="fecha">Date</option>
                </select>

                <input type="text" name="valorBusqueda" id="textInput" placeholder="Escriba su búsqueda">
                <input type="date" name="valorBusqueda" id="dateInput" style="display:none;">

                <input type="submit" value="Buscar" class="btn">
            </form>
        </div>
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
                <tr><td colspan="9" style="text-align:center;">No video found...</td></tr>
            </tbody>
        </table>

        <script>
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

            function playVideo(id) {
                window.location.href = "reproduction.jsp?id=" + id;
            }

            function editVideo(id) {
                window.location.href = "editVideo.jsp?id=" + id;
            }

            function deleteVideo(id) {
                if (confirm("Are you sure you want to delete this video?")) {
                    fetch("deleteVideoServlet?id=" + id, {
                        method: 'GET'
                    })
                            .then(response => {
                                if (response.ok) {
                                    location.reload();
                                } else {
                                    alert("Failed to delete video");
                                }
                            });
                }
            }
        </script>
    </body>
</html>
