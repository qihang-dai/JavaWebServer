package edu.upenn.cis.cis455.m1.handling;

import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.upenn.cis.cis455.exceptions.HaltException;
import edu.upenn.cis.cis455.m1.interfaces.Request;
import edu.upenn.cis.cis455.m1.interfaces.Response;

/**
 * Handles marshaling between HTTP Requests and Responses
 */
public class HttpIoHandler {
    final static Logger logger = LogManager.getLogger(HttpIoHandler.class);

    /**
     * Sends an exception back, in the form of an HTTP response code and message.
     * Returns true if we are supposed to keep the connection open (for persistent
     * connections).
     */
    public static boolean sendException(Socket socket, Request request, HaltException except) {
        return true;
    }

    /**
     * Sends data back. Returns true if we are supposed to keep the connection open
     * (for persistent connections).
     */
    public static boolean sendResponse(Socket socket, Request request, Response response) {
        return true;
    }
}
