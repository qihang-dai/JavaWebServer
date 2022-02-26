package edu.upenn.cis.cis455.m1.interfaces;

import edu.upenn.cis.cis455.exceptions.HaltException;
import edu.upenn.cis.cis455.m2.impl.Cookie;

import java.util.HashMap;
import java.util.Map;

public class HttpRes extends edu.upenn.cis.cis455.m2.interfaces.Response{
    Map<String, String> headers;
    Map<String, String> statusAll;
    Map<String, Cookie> cookieMap;

    public HttpRes(){
        cookieMap = new HashMap<String,Cookie>();
    }
    private final static Map<Integer, String> statusMapper = new HashMap<>(){
        {
            ;
            put(200, "200 OK");
            put(400, "400 Bad Request");
            put(404, "404 Not found");
            put(500, "500 Server Error");
        }
    };

    public Map<String, Cookie> getCookieMap() {
        return cookieMap;
    }

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

    @Override
    public void header(String header, String value) {

    }

    @Override
    public void redirect(String location) {

    }

    @Override
    public void redirect(String location, int httpStatusCode) {

    }

    @Override
    public void cookie(String name, String value) {
        System.out.println("cookie new0");

        Cookie cookie = new Cookie();
        System.out.println("cookie new1");
        cookie.setName(name);
        cookie.setValue(value);
        System.out.println("cookie new1");
        cookieMap.put(name,cookie);
    }

    @Override
    public void cookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie();
        cookie.setName(name);
        cookie.setValue(value);
        cookie.setMaxAge(maxAge);
        cookieMap.put(name,cookie);

    }

    @Override
    public void cookie(String name, String value, int maxAge, boolean secured) {
        Cookie cookie = new Cookie();
        cookie.setName(name);
        cookie.setValue(value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secured);
        cookieMap.put(name,cookie);

    }

    @Override
    public void cookie(String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        Cookie cookie = new Cookie();
        cookie.setName(name);
        cookie.setValue(value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secured);
        cookie.setHttpOnly(httpOnly);
        cookieMap.put(name,cookie);

    }

    @Override
    public void cookie(String path, String name, String value) {
        Cookie cookie = new Cookie();
        cookie.setName(name);
        cookie.setValue(value);
        cookie.setPath(path);
        cookieMap.put(name,cookie);
    }

    @Override
    public void cookie(String path, String name, String value, int maxAge) {
        Cookie cookie = new Cookie();
        cookie.setName(name);
        cookie.setValue(value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookieMap.put(name,cookie);

    }

    @Override
    public void cookie(String path, String name, String value, int maxAge, boolean secured) {
        Cookie cookie = new Cookie();
        cookie.setName(name);
        cookie.setValue(value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secured);
        cookieMap.put(name,cookie);
    }

    @Override
    public void cookie(String path, String name, String value, int maxAge, boolean secured, boolean httpOnly) {
        Cookie cookie = new Cookie();
        cookie.setName(name);
        cookie.setValue(value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secured);
        cookie.setHttpOnly(httpOnly);
        cookieMap.put(name,cookie);
    }

    @Override
    public void removeCookie(String name) {
        cookieMap.remove(name);
    }

    @Override
    public void removeCookie(String path, String name) {

    }
}
