package edu.upenn.cis.cis455.m1.handling;

import java.io.BufferedReader;
import java.net.Socket;

import edu.upenn.cis.cis455.m1.interfaces.*;
import edu.upenn.cis.cis455.m1.server.HttpTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.upenn.cis.cis455.exceptions.HaltException;
import edu.upenn.cis.cis455.m2.interfaces.Response;
import java.io.*;
import java.net.*;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles marshaling between HTTP Requests and Responses
 */
public class HttpIoHandler {
    final static Logger logger = LogManager.getLogger(HttpIoHandler.class);
    private Map<String, String> headers = new HashMap<>();
    private Map<String, List<String>> parms = new HashMap<>();
    private HttpRequest req;
    private  HttpRes res;
    private HttpTask task;

    public HttpIoHandler(HttpTask task){
        this.task = task;
        res = new HttpRes();

    }
    public  Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, List<String>> getParms() {
        return parms;
    }

    public  HttpRequest getReq() {
        return req;
    }

    public  Response getRes() {
        return res;
    }


    public void produce()  {
        Socket socket = task.getSocket();
//        logger.debug(socket);
        InputStream in = null;
        try {
            in = socket.getInputStream();
        } catch (IOException e) {
            logger.debug("socket isclosed?: " + socket.isClosed());
            logger.debug("Inputstream shutdown?: "+socket.isInputShutdown());
        }
        logger.info("socket port" + socket.getPort());
        logger.info("socket innet address" + socket.getInetAddress());
        InetAddress inetAddress = socket.getInetAddress();
        String IP = inetAddress.toString() == null ? "" : inetAddress.toString();
        String uri = null;
        try {
            assert in != null;
            uri = HttpParsing.parseRequest(IP, in, headers, parms);
        } catch (IOException e) {
            logger.debug("no input so no parse");
        }
        logger.warn(uri);
        req = new HttpRequest(task,headers,uri);
//        for(Map.Entry<String,String> entry : headers.entrySet()){
//            System.out.println( entry.getKey() + "  " + entry.getValue());
//        }
//
//        logger.info("--------------------param map-----------------");
//        for(Map.Entry<String,List<String>> entry : parms.entrySet()){
//            System.out.println( entry.getKey() + "  " + entry.getValue());
//        }
//        logger.info("--------------------^^^^param map ^^^^-----------------");
        RequestHandler rh = new RequestHandler(req, res);
        rh.handle();
        sendResponse(task.getSocket(),req,res);
    }
    /**
     * Sends an exception back, in the form of an HTTP response code and message.
     * Returns true if we are supposed to keep the connection open (for persistent
     * connections).
     */
    public static boolean sendException(Socket socket, Request request, HaltException except) throws IOException {
        HttpRes res = new HttpRes();
        byte[] out = res.responexception(except);
        socket.getOutputStream().write(out);
        return true;
    }

    /**
     * Sends data back. Returns true if we are supposed to keep the connection open
     * (for persistent connections).
     */
    public static boolean sendResponse(Socket socket, Request request, Response response) {
        byte[] out = response.getBody();
        try {
            OutputStream output = socket.getOutputStream();
            output.write(out);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
