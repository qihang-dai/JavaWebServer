package edu.upenn.cis.cis455.m1.interfaces;

import edu.upenn.cis.cis455.SparkController;
import edu.upenn.cis.cis455.m1.server.HttpListener;
import edu.upenn.cis.cis455.m1.server.HttpWorker;
import edu.upenn.cis.cis455.m2.interfaces.Filter;
import edu.upenn.cis.cis455.m2.interfaces.Session;
import edu.upenn.cis.cis455.m2.server.WebService;
import edu.upenn.cis.cis455.m2.impl.Cookie;
import edu.upenn.cis.cis455.m2.interfaces.Route;
import edu.upenn.cis.cis455.m2.mapper.PathMap;
import edu.upenn.cis.cis455.m2.mapper.RouteMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.upenn.cis.cis455.m2.interfaces.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class RequestHandler {
    final static Logger logger = LogManager.getLogger(RequestHandler.class);
    private HttpRequest request;
    private HttpRes response;
    String uri;
    String protocol;
    String method;
    String host;
    String type = "text/html";

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

    public RequestHandler(HttpRequest request, HttpRes response) {
        this.request = request;
        this.response = response;
        uri = request.uri();
        protocol= request.protocol();
        method = request.requestMethod();
        host = request.host();

    }

    public String WorkerList() {
        StringBuilder tmp = new StringBuilder("<ul>");
        List<HttpWorker> workers = SparkController.getInstance().getPool().getWorkers();
        for (HttpWorker w : workers) {
            tmp.append("<li>");
            tmp.append(w.getId()).append(" ").append(w.getStatus()).append(workers.size());
            logger.info(w.getId() + w.getStatus());
            tmp.append("<li>");
        }
        tmp.append("</ul>");
        return tmp.toString();
    }


    public void handle()  {

        logger.info("protoco: "+ protocol);
        logger.info("method: " + method);
        logger.info("host: " + host);
            if (uri.equals("/control")) {
                logger.info("control-------------------------------------");
                response.status(200);
                StringBuilder tmp = new StringBuilder();
                tmp.append(protocol).append(" ");
                tmp.append("200 OK\r\n");
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
                tmp.append(protocol).append(" ");
                tmp.append("200 OK\r\n");
                tmp.append("Content-Type: text/html\r\n");
                String body = "Shutdown";
                String list = WorkerList();
                body = body + list;
                byte[] content = body.getBytes(StandardCharsets.UTF_8);
                int len = content.length;
                tmp.append("Content-Length: ").append(len).append("\r\n");
                tmp.append("\r\n");
                tmp.append(body);
                response.body(tmp.toString());
                SparkController.getInstance().stop();
            } else {
                //get path mapping tools
                PathMap pathMap = new PathMap();

                //check before Filter
                setFilters(pathMap,true);


                //check if route is registered. if not, try getStatic file
                Route route = pathMap.getRouteMap(request);
                if (route != null) {
                    Object object = null;
                    try {
                        logger.info("route:***********" + route);
                        object = route.handle(request,response);
                    } catch (Exception e) {
                        logger.error("cant route handle******************************************");
                    }
                    //check after Filter
                    setFilters(pathMap,false);

                    boolean indicator = object == null;
                    logger.error("object null? " + indicator);
                    logger.info("object null?" );
                    logger.info(indicator);
                    logger.info("the request uri: " + request.uri());
                    if (object != null) {
                        StringBuilder tmp = new StringBuilder();
                        tmp.append(protocol).append(" ");
                        tmp.append("200 OK\r\n");

                        //check if type is defined in response
                        tmp.append("Content-Type: ");
                        logger.info("response.type():--------> "+ response.type());
                        if(response.type() != null){
                            type = response.type();
                        }
                        tmp.append(type).append("\r\n");

                        //check if webserver contains the active session(sessionId)
                        Session session = request.session();
                        logger.info("session id " + request.getSessionId());
                        Map<String, Session> sessionMap = WebService.getInstance().sessionMap;
                        Session session1 = sessionMap.get(request.getSessionId());
                        if(session != null){
                            if(WebService.getInstance().sessionMap.containsKey(session.id())){
                                response.cookie("JSESSIONID", session.id());
                                System.out.println(">>>>>>>>>>>session and cookie configured<<<<<<<<<<<<<<<<");
                            }else{
                                logger.warn("No such session id fail");
                            }
                        }else{
                            logger.info("In Request handler session is null");
                        }

                        //check if response has cookie, add all cookies
                        Map<String, Cookie> cookieMap = response.getCookieMap();
                        if(cookieMap != null){
                            for(Map.Entry<String,Cookie> entry : cookieMap.entrySet()){
                                Cookie cookie = entry.getValue();
                                tmp.append("Set-Cookie: ").append(cookie);
                                logger.info("Set-Cookie: " + cookie);
                                tmp.append("\r\n");
                            }
                        }


                        //processing the body
                        tmp.append("Content-Length: ").append(object.toString().length()).append("\r\n");
                        tmp.append("\r\n");
                        tmp.append(object);
                        logger.debug("object exsits*******************");
                        logger.debug(object.toString());
                        response.body(tmp.toString());
                    }else{
                        Error404();
                    }
                } else {
                    getStaticFile(uri, protocol);
                    setFilters(pathMap,false);
                }
            }
        }

    public void setFilters(PathMap pathMap, boolean isbefore){
        //check after Filter
        List<Filter> Filters = pathMap.getFilterMap(request,isbefore);
        for(Filter filter : Filters){
            try {
                filter.handle(request,response);
            } catch (Exception e) {
                logger.warn("Fail to handle filter");
            }
        }
    }



    public void getStaticFile(String uri, String protocol){
        logger.info("get uri.html");
        Path path = Paths.get(WebService.staticFileLocation + "/"+uri);
        logger.info(path);
        logger.info(path.toString());
        logger.info(path.toAbsolutePath());
        String type ="text/html";
        try {
            type = Files.probeContentType(path);
            logger.info("type changed");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(type);
        if (!Files.exists(path)) {
            Error404();
        } else {
            logger.info("file found, read the file");
            StringBuilder tmp = new StringBuilder();
            byte[] body = null;
            try {
                body = Files.readAllBytes(path);
            } catch (IOException e) {
                logger.debug("THE FILE IS NOT FOUND");
            }
            tmp.append(protocol).append(" ");
            tmp.append("200 OK\r\n");
            tmp.append("Content-Type: ");
            tmp.append(type).append("\r\n");
            tmp.append("Content-Length: ").append(body.length).append("\r\n");
            tmp.append("\r\n");
            byte[] header = tmp.toString().getBytes(StandardCharsets.UTF_8);
            body = addByteArray(header, body);
            response.bodyRaw(body);
        }
    }

    public static byte[] addByteArray(byte[] a, byte[] b){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write(a);
            outputStream.write(b);
        } catch (IOException e) {
            logger.warn("append byte array fail");
        }
        byte c[] = outputStream.toByteArray( );
        return c;
    }

    public void Error404(){
        String st = "<h1> 404 Not Found</h1>";
        int len = st.getBytes(StandardCharsets.UTF_8).length;
        logger.info("No such file");
        response.status(404);
        StringBuilder tmp = new StringBuilder();
        tmp.append(protocol).append(" ");
        tmp.append("404 NOT FOUND");
        tmp.append("Content-Type: ");
        tmp.append(type);
        tmp.append("Content-Length: ").append(len).append("\r\n");
        tmp.append("\r\n");
        tmp.append(st);
        response.body(tmp.toString());
    }

}
