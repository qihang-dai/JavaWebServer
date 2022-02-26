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
	private ServerSocket serverSocket;
	private int port;

	private String dir;
	private HttpTaskQueue q;
	volatile static boolean run = true;

	public ServerSocket getServerSocket() {
		return serverSocket;
	}


	public HttpListener(int port, HttpTaskQueue taskQueue) {
		this.port = port;
		this.q = taskQueue;
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
				logger.info("listneing----------------");
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
		synchronized (q){
			run = false;
			logger.info("Shutdown listener");
			q.notifyAll();
		}

		}

    	
    }
