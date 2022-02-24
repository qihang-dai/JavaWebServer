package edu.upenn.cis.cis455;

import static edu.upenn.cis.cis455.SparkController.*;

import edu.upenn.cis.cis455.m1.server.WebService;
import org.apache.logging.log4j.Level;

/**
 * Initialization / skeleton class.
 * Note that this should set up a basic web server for Milestone 1.
 * For Milestone 2 you can use this to set up a basic server.
 * 
 * CAUTION - ASSUME WE WILL REPLACE THIS WHEN WE TEST MILESTONE 2,
 * SO ALL OF YOUR METHODS SHOULD USE THE STANDARD INTERFACES.
 * 
 * @author zives
 *
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class WebServer {
    private static Logger logger = LogManager.getLogger(WebServer.class);
    static WebService webService ;
    public static void main(String[] args) {
        org.apache.logging.log4j.core.config.Configurator.setLevel("edu.upenn.cis.cis455", Level.DEBUG);
        webService = new WebService();
        // TODO: make sure you parse *BOTH* command line arguments properly
        webService.setPort(Integer.parseInt(args[0]));
        webService.staticFileLocation(args[1]);
        
        // All user routes should go below here...

        // ... and above here. Leave this comment for the Spark comparator tool

        System.out.println("Waiting to handle requests!");
        awaitInitialization();
    }
}
