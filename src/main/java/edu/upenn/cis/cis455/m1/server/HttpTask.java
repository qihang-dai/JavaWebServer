package edu.upenn.cis.cis455.m1.server;

import java.net.Socket;

public class HttpTask {
    private Socket requestSocket;
    public HttpTask(Socket socket) {
        requestSocket = socket;
    }

    public Socket getSocket() {
        return requestSocket;
    }
}
