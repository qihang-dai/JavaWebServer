package edu.upenn.cis.cis455.m1.interfaces;

import edu.upenn.cis.cis455.m1.server.HttpListener;
import edu.upenn.cis.cis455.m1.server.HttpWorker;
import edu.upenn.cis.cis455.m1.server.WebService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class RequestHandler {
    final static Logger logger = LogManager.getLogger(RequestHandler.class);
    private Request request;
    private Response response;
    static String control = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Lol</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <h1>Control Panel</h1>\n" +
            "    \n" +
            "</body>\n" +
            "</html>";

    public RequestHandler(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public String WorkerList() {
        StringBuilder tmp = new StringBuilder("<ul>");
        List<HttpWorker> workers = WebService.getPool().getWorkers();
        for (HttpWorker w : workers) {
            tmp.append("<li>");
            tmp.append(w.getId()).append(" ").append(w.getStatus()).append(workers.size());
            logger.info(w.getId() + w.getStatus());
            tmp.append("<li>");
        }
        tmp.append("</ul>");
        return tmp.toString();
    }


    public void handle() {
        String uri = request.uri;
        if (uri.equals("/control")) {
            response.status(200);
            StringBuilder tmp = new StringBuilder();
            tmp.append("HTTP/1.1 200 OK\r\n");
            tmp.append("Content-Type: text/html\r\n");

            String list = WorkerList();
            String button = "\n" +
                    "<button onclick=\"location.href='/shutdown'\" type=\"button\">\n" +
                    "         shutdown</button>";
            String body = control + list + button;

            byte[] content = body.getBytes(StandardCharsets.UTF_8);
            int len = content.length;

            tmp.append("Content-Length: ").append(len).append("\r\n");
            tmp.append("\r\n");
            tmp.append(body);
//            tmp.append("what the hell");
            tmp.append("\r\n");
            response.body(tmp.toString());
        } else if (uri.equals("/shutdown")) {
            logger.info("-----------------shutdown handler");
            response.status(500);
            StringBuilder tmp = new StringBuilder();
            tmp.append("HTTP/1.1 200 OK");
            tmp.append("Content-Type: text/html");
            String body = "Shutdown";
            String list = WorkerList();
            body = body + list;
            byte[] content = body.getBytes(StandardCharsets.UTF_8);
            int len = content.length;
            tmp.append("Content-Length: ").append(len).append("\r\n");
            tmp.append("\r\n");
            tmp.append(body);
            response.body(tmp.toString());
            WebService.stop();
        } else {
            logger.info("get uri.html");
            Path path = Paths.get(WebService.staticFileLocation + "/"+uri);
            logger.info(path);
            logger.info(path.toString());
            logger.info(path.toAbsolutePath());

            if (!Files.exists(path)) {
                logger.info("No such file");
                response.status(404);
                StringBuilder tmp = new StringBuilder();
                tmp.append("HTTP/1.1 404 NOT FOUND");
                tmp.append("Content-Type: text/html\r\n");;
                String st = "<h1> 404 Not Found</h1>";
                int len = st.getBytes(StandardCharsets.UTF_8).length;
                tmp.append("Content-Length: ").append(len).append("\r\n");
                tmp.append(st);
                tmp.append("\r\n");
                response.body(tmp.toString());
            } else {
                logger.info("file found, read the file");
                StringBuilder tmp = new StringBuilder();
                String body = null;
                try {
                    body = Files.readString(path);
                } catch (IOException e) {
                    logger.debug("THE FILE IS NOT FOUND");
                }
                tmp.append("HTTP/1.1 200 OK");
                tmp.append("Content-Type: text/html\r\n");
                byte[] content = body.getBytes(StandardCharsets.UTF_8);
                int len = content.length;
                tmp.append("Content-Length: ").append(len).append("\r\n");
                tmp.append("\r\n");
                tmp.append(body);
                response.body(tmp.toString());
            }
        }

    }
}
