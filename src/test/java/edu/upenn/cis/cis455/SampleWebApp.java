package edu.upenn.cis.cis455;

import static edu.upenn.cis.cis455.SparkController.*;

public class SampleWebApp {
    public static void main() {
        
        // Set the web server path to ./www
        staticFileLocation("./www");
        
        System.out.println("Here");
        
        // At this point, the web server should handle requests for static files from ./www
        // if you open your web browser to the Preview URL
        
        
        // TODO: Uncomment this for milestone 2: it will be a GET handler to a simple lambda function
        // get("index.html", (request, response) -> "Hello World");
    }
}
