package edu.upenn.cis.cis455.m1.server;

import java.util.LinkedList;
import java.util.Queue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Stub class for implementing the queue of HttpTasks
 */
public class HttpTaskQueue{
	static final Logger logger = LogManager.getLogger(HttpTaskQueue.class);
	Queue<HttpTask> q;
	int size;
	public int size(){
		return q.size();
	}
	public HttpTaskQueue(int size){
		this.q = new LinkedList<>();
		this.size = size;
	}

	public void wakeupShutDown(){
		synchronized (q){
			q.notifyAll();
		}
	}

	public void add(HttpTask t) throws InterruptedException {
//		logger.info("Adding element to queue");
		while (true) {
			synchronized (q) {
				if (q.size() == size) {
					// Synchronizing on the sharedQueue to make sure no more than one
					// thread is accessing the queue same time.
//					logger.debug("Queue is full!");
					q.wait();
					// We use wait as a way to avoid polling the queue to see if
					// there was any space for the producer to push.
				} else {
					//Adding element to queue and notifying all waiting consumers
					q.add(t);
//					logger.debug("Notifying after add");//This would be logged in the log file created and to the console.
					q.notifyAll();
					break;
				}
			}
		}
	}
	public HttpTask poll() throws InterruptedException {
		while (true) {
			synchronized (q) {
				if (q.isEmpty()) {
					//If the queue is empty, we push the current thread to waiting state. Way to avoid polling.
//					logger.debug("Queue is currently empty");
					q.wait();
				} else {
					HttpTask result = q.poll();
//					logger.debug("Notifying everyone we are removing an item");
					q.notifyAll();
//					logger.debug("Exiting queue with return");
					return result;
				}
			}
		}
		
	}
	
	
}
