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

import edu.upenn.cis.cis455.exceptions.HaltException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public abstract class Response {
    protected int statusCode = 200;
    protected byte[] body;

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    protected String contentType = null; // e.g., "text/plain";

    public int status() {
        return statusCode;
    }

    public void status(int statusCode) {
        this.statusCode = statusCode;
    }

    public String body() {
        try {
            return body == null ? "" : new String(body, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public byte[] bodyRaw() {
        return body;
    }

    public void bodyRaw(byte[] b) {
        body = b;
    }

    public void body(String body) {
        this.body = body == null ? null : body.getBytes(StandardCharsets.UTF_8);
    }

    public String type() {
        return contentType;
    }

    public void type(String contentType) {
        this.contentType = contentType;
    }

    public abstract String getHeaders();


}
