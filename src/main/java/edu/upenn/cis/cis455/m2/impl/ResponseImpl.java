package edu.upenn.cis.cis455.m2.impl;

import edu.upenn.cis.cis455.m2.interfaces.Response;

public class ResponseImpl extends Response {
    @Override
    public String getHeaders() {
        return null;
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

    }

    @Override
    public void cookie(String name, String value, int maxAge) {

    }

    @Override
    public void cookie(String name, String value, int maxAge, boolean secured) {

    }

    @Override
    public void cookie(String name, String value, int maxAge, boolean secured, boolean httpOnly) {

    }

    @Override
    public void cookie(String path, String name, String value) {

    }

    @Override
    public void cookie(String path, String name, String value, int maxAge) {

    }

    @Override
    public void cookie(String path, String name, String value, int maxAge, boolean secured) {

    }

    @Override
    public void cookie(String path, String name, String value, int maxAge, boolean secured, boolean httpOnly) {

    }

    @Override
    public void removeCookie(String name) {

    }

    @Override
    public void removeCookie(String path, String name) {

    }
}
