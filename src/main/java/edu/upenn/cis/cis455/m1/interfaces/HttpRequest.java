package edu.upenn.cis.cis455.m1.interfaces;

import edu.upenn.cis.cis455.m1.server.HttpTask;

import java.util.Map;
import java.util.Set;

public class HttpRequest extends Request{


    public HttpRequest(HttpTask task, Map<String, String> headers, String uri){
        super();
        this.port = task.getSocket().getPort();
        this.ip = task.getSocket().getRemoteSocketAddress().toString();
        this.uri = uri;
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
            }
        }

    }


    @Override
    public String requestMethod() {
        return null;
    }

    @Override
    public String host() {
        return null;
    }

    @Override
    public String userAgent() {
        return null;
    }

    @Override
    public int port() {
        return 0;
    }

    @Override
    public String pathInfo() {
        return null;
    }

    @Override
    public String url() {
        return null;
    }

    @Override
    public String uri() {
        return null;
    }

    @Override
    public String protocol() {
        return null;
    }

    @Override
    public String contentType() {
        return null;
    }

    @Override
    public String ip() {
        return null;
    }

    @Override
    public String body() {
        return null;
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
}
