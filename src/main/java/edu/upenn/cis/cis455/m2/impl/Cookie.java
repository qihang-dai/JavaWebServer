package edu.upenn.cis.cis455.m2.impl;

import java.nio.file.Paths;

public class Cookie {
    private String path;
    private String name;
    private String value;
    private int maxAge;
    private boolean secure;
    private boolean httpOnly;
    public Cookie(){}


    public Cookie(String path, String name, String value, int maxAge, boolean secure, boolean httpOnly) {
        if(path == null) path = "/";
        this.path = Paths.get(path).normalize().toString();
        this.name = name;
        this.value = value;
        this.maxAge = maxAge;
        this.secure = secure;
        this.httpOnly = httpOnly;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    @Override
    public String toString(){
        StringBuilder cookie = new StringBuilder();
        cookie.append(name + "=" + value +";")
                .append("path=" + path + ";");
        if(maxAge > 0) cookie.append("Max-Age=" + maxAge + ";");
        if(httpOnly) cookie.append("HttpOnly;");
        if(secure) cookie.append("Secure;");

        return cookie.toString();

    }
}
