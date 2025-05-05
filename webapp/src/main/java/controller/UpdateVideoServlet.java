package controller;

import dao.VideoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import model.Video;

@WebServlet(name = "updateVideoServlet", urlPatterns = {"/jsp/updateVideoServlet"})
public class UpdateVideoServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = parseFormData(request);

        int id = Integer.parseInt(params.get("id"));
        String title = params.get("title");
        String description = params.get("description");

        VideoDAO dao = new VideoDAO();
        Video video = dao.findById(id);

        if (video != null) {
            video.setTitle(title);
            video.setDescription(description);
            dao.update(video);
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content for successful PUT
    }

    private Map<String, String> parseFormData(HttpServletRequest request) throws IOException {
        Map<String, String> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        String line;

        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String[] pairs = sb.toString().split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name());
                params.put(key, value);
            }
        }
        return params;
    }
}