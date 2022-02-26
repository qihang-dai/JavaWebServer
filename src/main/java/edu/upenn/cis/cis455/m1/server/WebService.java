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
package edu.upenn.cis.cis455.m1.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.upenn.cis.cis455.exceptions.HaltException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class WebService {
    final static Logger logger = LogManager.getLogger(WebService.class);
    public  static String staticFileLocation = "./www";
    int port = 45555;
    private HttpListener listener;
    private ThreadPool pool;
    private String ipAddress;
    private HttpTaskQueue taskQueue;
    private  int size = 10;
    private Thread curThread;

    public ThreadPool getPool() {
        return pool;
    }


    public WebService(){}
    /**
     * Launches the Web server thread pool and the listener
     */

    /**
     * Gracefully shut down the server
     */
    public void stop()  {
        pool.shutdown();
        listener.shutdown();
        logger.info("shuttdown!!!!!!!!!");

//        System.exit(0);

    }

    /**
     * Hold until the server is fully initialized.
     * Should be called after everything else.
     */
    public void awaitInitialization() {
        this.taskQueue = new HttpTaskQueue(size);
        this.listener = new HttpListener(port,taskQueue);
        this.pool = new ThreadPool(size, taskQueue);
        this.curThread = new Thread(listener);
        logger.info("Initializing server");
        this.curThread.start();
        this.pool.start();
    }

    /**
     * Triggers a HaltException that terminates the request
     */
    public HaltException halt() {
        throw new HaltException();
    }

    /**
     * Triggers a HaltException that terminates the request
     */
    public HaltException halt(int statusCode) {
        throw new HaltException(statusCode);
    }

    /**
     * Triggers a HaltException that terminates the request
     */
    public HaltException halt(String body) {
        throw new HaltException(body);
    }

    /**
     * Triggers a HaltException that terminates the request
     */
    public HaltException halt(int statusCode, String body) {
        throw new HaltException(statusCode, body);
    }

    ////////////////////////////////////////////
    // Server configuration
    ////////////////////////////////////////////

    /**
     * Set the root directory of the "static web" files
     */
    public void staticFileLocation(String directory) {
        this.staticFileLocation = directory;
    }

    /**
     * Set the IP address to listen on (default 0.0.0.0)
     */
    public void ipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Set the TCP port to listen on (default 45555)
     */
    public void port(int port) {
        this.port = port;
    }

    /**
     * Set the size of the thread pool
     */
    public void threadPool(int threads) {
        this.size = threads;
    }

}
