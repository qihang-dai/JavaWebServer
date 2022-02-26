package edu.upenn.cis.cis455.m1.interfaces;

import edu.upenn.cis.cis455.m1.handling.HttpIoHandler;
import edu.upenn.cis.cis455.m1.server.HttpTask;
import edu.upenn.cis.cis455.m1.server.HttpWorker;
import edu.upenn.cis.cis455.m2.server.WebService;
import edu.upenn.cis.cis455.m2.impl.Cookie;
import edu.upenn.cis.cis455.m2.interfaces.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpRequest extends edu.upenn.cis.cis455.m2.interfaces.Request{
    final static Logger logger = LogManager.getLogger(HttpRequest.class);
    private Map<String,String> headers;
    private Map<String,String> params;
    private Map<String, String> cookieMap;
    private Map<String, Object> attributeMap;
    private String sessionId;
    private HttpTask task;
    boolean persistent = false;
    String requestMethod;
    String host;
    String userAgent;
    int port;
    String path;
    String url;
    String uri;
    String protocal;
    String body;
    int length;
    String ip;
    String contentType;
    String contentLength;

    public HttpRequest(HttpTask task, Map<String, String> headers, String uri){
        super();
        this.task = task;
        this.port = task.getSocket().getPort();
        this.ip = task.getSocket().getInetAddress().toString();
        this.uri = uri;
        this.headers = headers;
        this.cookieMap = new HashMap<>();
        this.attributeMap = new HashMap<>();
        for(Map.Entry<String,String> entry : headers.entrySet()){
            String key = entry.getKey();
            String val = entry.getValue();
            switch (key){
                case "host":
                    this.host = val;
                    break;
                case "Method":
                    this.requestMethod = val;
                    break;
                case "user-agent":
                    this.userAgent = val;
                    break;
                case "protocolVersion":
                    this.protocal = val;
                    break;
                case "Content-Length":
                    this.contentLength = val;
                    break;
                case "Content-Type":
                    this.contentType = val;
                    break;
                case "Set-Cookie":
                    String[] cookieString = val.split(";");
                    for(String prop: cookieString){
                        int pos = prop.indexOf("=");
                        if(pos == -1){
                            cookieMap.put(prop, "true");
                            continue;
                        }
                        cookieMap.put(prop.substring(0, pos), prop.substring(pos + 1));
                    }
                    this.sessionId = cookieMap.getOrDefault("JSESSIONID","");

            }
        }

    }

    public String getSessionId() {
        return sessionId;
    }

    public Session session() {
        return session(true);
    }

    public Session session(boolean create) {
        System.out.println("find session");
        Session session = WebService.getInstance().getSession(sessionId);
        System.out.println("found session");
        if(session == null){
            System.out.println("session null");
            if(create){
                sessionId = WebService.getInstance().createSession();
                logger.info("session Id: " + sessionId);
                session = WebService.getInstance().getSession(sessionId);
                logger.info("session is still null??? " + session == null);
                logger.info("session is created");
            }
        }
        return session;
    }
    public void attribute(String attrib, Object val) {
        System.out.println("try attribute added");
        attributeMap.put(attrib, val);
        System.out.println("attribute added");

    }

    public Object attribute(String attrib) {
        return attributeMap.get(attrib);
    }

    public Set<String> attributes() {
        return attributeMap.keySet();
    }
    @Override
    public String requestMethod() {
        return requestMethod;
    }

    @Override
    public String host() {
        return host;
    }

    @Override
    public String userAgent() {
        return userAgent;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public String pathInfo() {
        return host + uri;
    }

    @Override
    public String url() {
        return host + uri;
    }

    @Override
    public String uri() {return uri;}

    @Override
    public String protocol() {
        return protocal;
    }

    @Override
    public String contentType() {
        return contentType;
    }

    @Override
    public String ip() {
        return ip;
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public int contentLength() {
        return 0;
    }

    @Override
    public String headers(String name) {
        return null;
    }

    @Override
    public Set<String> headers() {
        return null;
    }




    public Map<String, String> params() {
        return params;
    }

    public void setparams(Map<String, String> params) {
        this.params = params;
    }

    public String queryParams(String param) {
        return null;
    }

    public List<String> queryParamsValues(String param) {
        return null;
    }

    public Set<String> queryParams() {
        return null;
    }

    public String queryString() {
        return null;
    }



    public Map<String, String> cookies() {
        return cookieMap;
    }
}
