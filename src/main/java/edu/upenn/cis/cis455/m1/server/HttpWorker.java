package edu.upenn.cis.cis455.m1.server;

import edu.upenn.cis.cis455.m1.handling.HttpIoHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Stub class for a thread worker that handles Web requests
 */
public class HttpWorker implements Runnable {
    private final static Logger logger = LogManager.getLogger(HttpWorker.class);
    HttpTaskQueue q;
    HttpIoHandler hdl;
    private String status;
    String Id;
    volatile boolean run = true;
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public HttpWorker(HttpTaskQueue q) {
        this.q = q;
        this.status = "Working";
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
            while(run){
                this.status = "waiting for job";
                try {
                    HttpTask t = q.poll();
                    logger.info("polling!");
                    this.status = "processing job";
                    HttpIoHandler hdl = new HttpIoHandler(t);
                    logger.debug(t.getSocket() +"\r\n" + Id);
                    hdl.produce();
                    hdl = null;
                    t.getSocket().close();

                } catch (IOException | InterruptedException e) {
                    logger.error("Bad Worker");
                    this.status = "Shutdown";
                }
            }

    }
    public  void shutdown() {
        run = false;
        q.wakeupShutDown();

    }
    public String getStatus() {
        return status;
    }
}
