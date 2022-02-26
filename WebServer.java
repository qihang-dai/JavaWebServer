package edu.upenn.cis.cis455;

import static edu.upenn.cis.cis455.SparkController.*;
import java.util.Map;
import java.util.List;
import java.util.Set;

import edu.upenn.cis.cis455.m2.interfaces.Session;

public class WebServer {
    public static void main(String[] args) {   

        staticFileLocation("./");          
        port(45555);

        get("/testRoute", (request, response) -> {
            return "testRoute content";
        });
            
        get("/:name/hello", (request, response) -> {
            return "Hello: " + request.params("name");
        });

        get("/testCookie1", (request, response) -> {
          String body = "<HTML><BODY><h3>Cookie Test 1</h3>";
          
          response.cookie("TestCookie1", "1");

          body += "Added cookie (TestCookie,1) to response.";
          response.type("text/html");
          response.body(body);
          return response.body();
        });

        get("/testSession1", (request, response) -> {
          String body = "<HTML><BODY><h3>Session Test 1</h3>";

          request.session(true).attribute("Attribute1", "Value1");

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
