package edu.upenn.cis.cis455.m2.server;

import edu.upenn.cis.cis455.exceptions.HaltException;
import edu.upenn.cis.cis455.m2.interfaces.Request;
import edu.upenn.cis.cis455.m2.interfaces.Response;
import edu.upenn.cis.cis455.m2.interfaces.Route;

public class MockRequestHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws HaltException {
        response.status(200);
        response.type("text/html");

        return "<html><head><title>Response</title></head><body><h1>Response</h1><p>Test</p></body></html>";
    }
}
