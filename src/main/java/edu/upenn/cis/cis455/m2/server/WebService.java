/**
 * CIS 455/555 route-based HTTP framework
 * 
 * Z. Ives, 8/2017
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
package edu.upenn.cis.cis455.m2.server;

import edu.upenn.cis.cis455.m1.server.HttpListener;
import edu.upenn.cis.cis455.m1.server.HttpWorker;
import edu.upenn.cis.cis455.m2.impl.SessionImpl;
import edu.upenn.cis.cis455.m2.interfaces.Session;
import edu.upenn.cis.cis455.m2.mapper.FilterMap;
import edu.upenn.cis.cis455.m2.mapper.RouteMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.upenn.cis.cis455.m2.interfaces.Route;
import edu.upenn.cis.cis455.m2.interfaces.Filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebService extends edu.upenn.cis.cis455.m1.server.WebService {
    final int port = 45555;
    HttpListener listener;
    HttpWorker worker;
    final static Logger logger = LogManager.getLogger(WebService.class);
    volatile private static WebService webService;
    private FilterMap beforeFilter;
    private FilterMap afterFilter;
    private RouteMap getMap;
    private RouteMap postMap;
    private RouteMap putMap;
    private RouteMap deleteMap;
    private RouteMap headMap;
    private RouteMap optionsMap;
    public Map<String, Session> sessionMap = new HashMap<>();


    public WebService() {
        super();
        buildMapper();
    }

    public void buildMapper(){
        logger.info("mapper build");
        this.getMap = new RouteMap();
        this.postMap = new RouteMap();
        this.putMap = new RouteMap();
        this.deleteMap = new RouteMap();
        this.headMap = new RouteMap();
        this.optionsMap = new RouteMap();
        this.beforeFilter = new FilterMap();
        this.afterFilter = new FilterMap();
    }

    public static WebService getInstance(){
        if(webService == null){
            webService = new WebService();
        }
        return webService;
    }

    public void get(String path, Route route) {
        getMap.register(path, route);
    }

    ///////////////////////////////////////////////////
    // For more advanced capabilities
    ///////////////////////////////////////////////////

    /**
     * Handle an HTTP POST request to the path
     */
    public void post(String path, Route route) {
        postMap.register(path, route);
    }

    /**
     * Handle an HTTP PUT request to the path
     */
    public void put(String path, Route route) {
        putMap.register(path, route);

    }

    /**
     * Handle an HTTP DELETE request to the path
     */
    public void delete(String path, Route route) {
        deleteMap.register(path, route);
    }

    /**
     * Handle an HTTP HEAD request to the path
     */
    public void head(String path, Route route) {
        headMap.register(path, route);
    }

    /**
     * Handle an HTTP OPTIONS request to the path
     */
    public void options(String path, Route route) {
        optionsMap.register(path, route);
    }

    ///////////////////////////////////////////////////
    // HTTP request filtering
    ///////////////////////////////////////////////////

    /**
     * Add filters that get called before a request
     */
    public void before(Filter filter) {
        beforeFilter.register("/*", filter);

    }

    /**
     * Add filters that get called after a request
     */
    public void after(Filter filter) {
        afterFilter.register("/*", filter);

    }

    /**
     * Add filters that get called before a request
     */
    public void before(String path, Filter filter) {
        beforeFilter.register(path,filter);
    }

    /**
     * Add filters that get called after a request
     */
    public void after(String path, Filter filter) {
        afterFilter.register(path, filter);
    }

    public String createSession(){
        SessionImpl session = new SessionImpl();
        sessionMap.put(session.id(), session);
        System.out.println();
        return session.id();
    }
    public Map<String, Session>  getSessionMap(){
        return sessionMap;
    }
    public Session getSession(String id){
        System.out.println("try to get id: " + id);
        Session session = sessionMap.get(id);
        System.out.println("get session " + id);
        System.out.println("sesssion is null? " + session == null);
        //critical, or would runinto error. always do null check.
        if(session == null) return null;
        session.access();
        System.out.println("accessed " + id);
        if(!session.validate()){
            sessionMap.remove(id);
        }
        System.out.println("accessed " + id);
        return sessionMap.get(id);
    }

    public FilterMap getBeforeFilter() {
        return beforeFilter;
    }

    public FilterMap getAfterFilter() {
        return afterFilter;
    }

    public RouteMap getGetMap() {
        return getMap;
    }

    public RouteMap getPostMap() {
        return postMap;
    }

    public RouteMap getPutMap() {
        return putMap;
    }

    public RouteMap getDeleteMap() {
        return deleteMap;
    }

    public RouteMap getHeadMap() {
        return headMap;
    }

    public RouteMap getOptionsMap() {
        return optionsMap;
    }


}
