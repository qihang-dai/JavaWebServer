package edu.upenn.cis.cis455.m2.impl;

import edu.upenn.cis.cis455.m2.interfaces.Filter;
import edu.upenn.cis.cis455.m2.interfaces.Request;
import edu.upenn.cis.cis455.m2.interfaces.Response;

public class Filterimpl implements Filter {

    /**
     * A Filter is called by the Web server to process data before or after the
     * Route Handler is called. This is typically used to attach attributes or to
     * call the HaltException, e.g., if the user is not authorized.
     */
    @Override
    public void handle(Request request, Response response) throws Exception {

    }
}
