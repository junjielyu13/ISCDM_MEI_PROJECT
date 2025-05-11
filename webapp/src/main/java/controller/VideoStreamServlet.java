/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.VideoStreamServlet to edit this template
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import model.Video;
import service.VideoService;

/**
 *
 * @author alumne
 */
@WebServlet(name = "videoStreamServlet", urlPatterns = {"/jsp/videoStreamServlet"})
public class VideoStreamServlet extends HttpServlet {
    
    private static final String uploadDir = "/home/alumne/ISCDM_MEI_PROJECT/webapp/src/main/webapp/";

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String idstr = request.getParameter("id");

    if (idstr == null) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
    }

    int id;
    try {
        id = Integer.parseInt(idstr);
    } catch (NumberFormatException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
    }

    Video video = new VideoService().findById(id);
    if (video == null) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
    }

    File videoFile = new File(uploadDir + video.getUrl());
    if (!videoFile.exists()) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
    }

    String range = request.getHeader("Range");
    long length = videoFile.length();
    long start = 0;
    long end = length - 1;

    if (range != null && range.startsWith("bytes=")) {
        // 支持 Range 请求
        String[] parts = range.substring(6).split("-");
        try {
            start = Long.parseLong(parts[0]);
            if (parts.length > 1 && !parts[1].isEmpty()) {
                end = Long.parseLong(parts[1]);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (end >= length) {
            end = length - 1;
        }

        if (start > end || start >= length) {
            response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return;
        }

        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
    }

    long contentLength = end - start + 1;

    response.setContentType("video/mp4");
    response.setHeader("Content-Length", String.valueOf(contentLength));
    response.setHeader("Accept-Ranges", "bytes");

    if (range != null) {
        response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + length);
    }

    response.setHeader("Access-Control-Allow-Origin", "*");

    try (InputStream inputStream = new FileInputStream(videoFile);
         var outputStream = response.getOutputStream()) {

        inputStream.skip(start);
        byte[] buffer = new byte[4096];
        long remaining = contentLength;
        int bytesRead;

        while (remaining > 0 && (bytesRead = inputStream.read(buffer, 0, (int)Math.min(buffer.length, remaining))) != -1) {
            outputStream.write(buffer, 0, bytesRead);
            remaining -= bytesRead;
        }
    }
}

}
