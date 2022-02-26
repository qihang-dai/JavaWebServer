//package edu.upenn.cis.cis455;
//
//import static edu.upenn.cis.cis455.SparkController.*;
//
//import org.apache.logging.log4j.Level;
//
///**
// * Initialization / skeleton class.
// * Note that this should set up a basic web server for Milestone 1.
// * For Milestone 2 you can use this to set up a basic server.
// *
// * CAUTION - ASSUME WE WILL REPLACE THIS WHEN WE TEST MILESTONE 2,
// * SO ALL OF YOUR METHODS SHOULD USE THE STANDARD INTERFACES.
// *
// * @author zives
// *
// */
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//public class WebServer {
//    private static Logger logger = LogManager.getLogger(WebServer.class);
//    public static void main(String[] args) {
//        org.apache.logging.log4j.core.config.Configurator.setLevel("edu.upenn.cis.cis455", Level.DEBUG);
//        // TODO: make sure you parse *BOTH* command line arguments properly
//        port(45555);
////        webService.staticFileLocation(args[1]);
//        // All user routes should go below here...
//        // ... and above here. Leave this comment for the Spark comparator tool
//        get("/test", (req, res) -> "testcase");
//        get("/test/:mingzi", (req, res) -> "GoodJob: " + req.params(":mingzi"));
//        get("/:name/test", (req, res) -> "GoodJob: " + req.params(":name"));
//        get("/:name1/:name2", (req, res) -> "GoodJob: " + req.params(":name1") + "" + req.params(":name2"));
//
//        awaitInitialization();
//        System.out.println("Waiting to handle requests!");
//    }
//}
package edu.upenn.cis.cis455;

import static edu.upenn.cis.cis455.SparkController.*;
import java.util.Map;
import java.util.List;
import java.util.Set;

import edu.upenn.cis.cis455.m2.interfaces.Session;
import org.apache.logging.log4j.Level;

public class WebServer {
    public static void main(String[] args) {
        org.apache.logging.log4j.core.config.Configurator.setLevel("edu.upenn.cis.cis455", Level.DEBUG);
        staticFileLocation("./");
        port(45555);
        getInstance();
        get("/testRoute", (request, response) -> {
            return "testRoute content";
        });

        get("/:name/hello", (request, response) -> {
            return "Hello: " + request.params("name");
        });

        get("/testCookie1", (request, response) -> {
            String body = "<HTML><BODY><h3>Cookie Test 1</h3>";
            System.out.println("testSession body defined");
            response.cookie("TestCookie1", "1");
            System.out.println("testSesssion cookie add");
            body += "Added cookie (TestCookie,1) to response.";
            response.type("text/html");
            System.out.println("testSesssion type add");

            response.body(body);
            System.out.println("testSesssion body add");

            return response.body();
        });

        get("/testSession1", (request, response) -> {
            String body = "<HTML><BODY><h3>Session Test 1</h3>";
            System.out.println("testSession body defined");
            request.session(true).attribute("Attribute1", "Value1");
            System.out.println("attribute completed");

            body += "</BODY></HTML>";
            response.type("text/html");
            response.body(body);
            return response.body();
        });

        before((request, response) -> {
            request.attribute("attribute1", "everyone");
        });

        get("/testFilter1", (request, response) -> {
            String body = "<HTML><BODY><h3>Filters Test</h3>";

            for(String attribute : request.attributes()) {
                body += "Attribute: " + attribute + " = " + request.attribute(attribute) + "\n";
            }

            body += "</BODY></HTML>";
            response.type("text/html");
            response.body(body);
            return response.body();
        });

        after((req, res) ->{});

        awaitInitialization();

        System.out.println("Waiting to handle requests!");
    }

}

