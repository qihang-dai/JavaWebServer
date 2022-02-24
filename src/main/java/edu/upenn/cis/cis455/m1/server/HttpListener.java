package edu.upenn.cis.cis455.m1.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.upenn.cis.cis455.m1.handling.HttpIoHandler;

import java.io.IOException;
import java.net.*;
/**
 * Stub for your HTTP server, which listens on a ServerSocket and handles
 * requests
 */
public class HttpListener implements Runnable {
	final static Logger logger = LogManager.getLogger(HttpListener.class);
	ServerSocket serverSocket;
	int port;

	String dir;
	HttpTaskQueue q;
	volatile static boolean run = true;

	public ServerSocket getServerSocket() {
		return serverSocket;
	}


	public HttpListener(int port, HttpTaskQueue q) {
		this.port = port;
		this.q = q;
		logger.info("Listening!");
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @Override
    public void run() {
        // TODO Auto-generated method stub
			int taskNo = 1;
			while(run) {
				Socket client = null;
				try {
					client = serverSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try{
					HttpTask task = new HttpTask(client);
					this.q.add(task);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.info("Listener Shutdown");
			try {
				this.getServerSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}



	}

    public  void shutdown() {
			run = false;
			logger.info("Shutdown listener");
		}

    	
    }
