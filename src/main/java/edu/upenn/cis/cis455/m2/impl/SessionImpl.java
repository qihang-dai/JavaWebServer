package edu.upenn.cis.cis455.m2.impl;

import edu.upenn.cis.cis455.m2.interfaces.Session;
import org.eclipse.jetty.websocket.api.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SessionImpl extends Session {
    Map<String, Object> attribute;
    private long creationTime;
    private int maxInactiveInterval = (int) Duration.ofMinutes(10).toMillis();;
    private long lastAcessTime;
    boolean valid;
    String id;


    public SessionImpl(){
        this.id = randomIdmaker();
        this.creationTime = System.currentTimeMillis();
        this.valid = true;
        this.attribute = new HashMap<>();
    }
    public String randomIdmaker(){
        long id = new Random().nextLong();
        return String.valueOf(id);

    }
    @Override
    public String id() {
        access();

        return id;
    }

    @Override
    public boolean validate() {
        return valid;
    }

    @Override
    public long creationTime() {
        access();

        return creationTime;
    }

    @Override
    public long lastAccessedTime() {
        access();
        return lastAcessTime;
    }

    @Override
    public void invalidate() {
        this.valid = false;
    }

    @Override
    public int maxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public void maxInactiveInterval(int interval) {
        access();
        this. maxInactiveInterval = interval;

    }

    @Override
    public void access() {
        this.lastAcessTime = System.currentTimeMillis();
        if(!valid){
            return;
        }else if(System.currentTimeMillis() >= this.lastAcessTime + this.maxInactiveInterval){
            valid = false;
        }else{
            this.lastAcessTime = System.currentTimeMillis();
        }
    }

    @Override
    public void attribute(String name, Object value) {
        access();
        this.attribute.put(name,value);

    }

    @Override
    public Object attribute(String name) {
        access();
        return this.attribute.get(name);
    }

    @Override
    public Set<String> attributes() {
        access();

        return this.attribute.keySet();
    }

    @Override
    public void removeAttribute(String name) {
        if(!this.attribute.containsKey(name)){
            return;
        }
        access();
        this.attribute.remove(name);
    }
}
