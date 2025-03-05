<%-- 
    Document   : ejemplo
    Created on : 21 feb 2024, 18:41:28
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="../assets/index.css">
    </head>
    <body>
        <form action="userServlet" method="post">
            <input type="text" name="user"/>
            <input type="password" name="passwd"/>
            <input type="submit" value="Enviar"/>            
        </form>
    </body>
</html>
