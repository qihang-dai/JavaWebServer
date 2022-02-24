/**
 * CIS 455/555 route-based HTTP framework
 * 
 * V. Liu, Z. Ives
 * 
 * Portions excerpted from or inspired by Spark Framework, 
 * 
 *                 http://sparkjava.com,
 * 
 * with license notice included below.
 */

/*
 * Copyright 2011- Per Wendel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.upenn.cis.cis455.m1.interfaces;

import java.util.Map;
import java.util.Set;

/**
 * Initial (simplified) request API, for Milestone 1
 */
public abstract class Request {
    /**
     * Indicates we have a persistent HTTP 1.1 connection
     */
    boolean persistent = false;
    Map header;
    String requestMethod;
    String host;
    String userAgent;
    int port;
    String path;
    String url;
    String uri;

    public Map getHeader() {
        return header;
    }

    public void setHeader(Map header) {
        this.header = header;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    String protocal;
    String body;
    int length;
    String ip;
    String contentType;

    /**
     * The request method (GET, POST, ...)
     */
    public abstract String requestMethod();

    /**
     * @return The host
     */
    public abstract String host();

    /**
     * @return The user-agent
     */
    public abstract String userAgent();

    /**
     * @return The server port
     */
    public abstract int port();

    /**
     * @return The path
     */
    public abstract String pathInfo();

    /**
     * @return The URL
     */
    public abstract String url();

    /**
     * @return The URI up to the query string
     */
    public abstract String uri();

    /**
     * @return The protocol name and version from the request
     */
    public abstract String protocol();

    /**
     * @return The MIME type of the body
     */
    public abstract String contentType();

    /**
     * @return The client's IP address
     */
    public abstract String ip();

    /**
     * @return The request body sent by the client
     */
    public abstract String body();

    /**
     * @return The length of the body
     */
    public abstract int contentLength();

    /**
     * @return Get the item from the header
     */
    public abstract String headers(String name);

    public abstract Set<String> headers();

    /**
     * Indicates we have a persistent HTTP 1.1 connection
     */
    public boolean persistentConnection() {
        return persistent;
    }

    /**
     * Sets whether we have a persistent HTTP 1.1 connection
     */
    public void persistentConnection(boolean persistent) {
        this.persistent = persistent;
    }
}
