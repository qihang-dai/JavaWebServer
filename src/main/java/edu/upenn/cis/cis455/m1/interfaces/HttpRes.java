package edu.upenn.cis.cis455.m1.interfaces;

import edu.upenn.cis.cis455.exceptions.HaltException;

import java.util.HashMap;
import java.util.Map;

public class HttpRes extends Response{
    Map<String, String> headers;
    Map<String, String> statusAll;
    private final static Map<Integer, String> statusMapper = new HashMap<>(){
        {
            ;
            put(200, "200 OK");
            put(400, "400 Bad Request");
            put(404, "404 Not found");
            put(500, "500 Server Error");
        }
    };

    @Override
    public String getHeaders() {
        return null;
    }


    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public byte[] responexception(HaltException except) {
        StringBuilder sb = new StringBuilder();
        String statuscode =  statusMapper.get(except.statusCode());
        sb.append("HTTP/1.1 ").append(statuscode).append("\r\n").append("Content-Length: 0 \r\n");
        return sb.toString().getBytes();
    }
}
