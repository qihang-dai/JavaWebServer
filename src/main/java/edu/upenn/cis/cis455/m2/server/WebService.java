/**
 * CIS 455/555 route-based HTTP framework
 * 
 * Z. Ives, 8/2017
 * 
 * Portions excerpted from or inspired by Spark Framework, 
 * 
 *                 http://sparkjava.com,
 * 
 * with license notice included below.
 */

/*
 * Copyright 2011- Per Wendel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.upenn.cis.cis455.m2.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.upenn.cis.cis455.m2.interfaces.Route;
import edu.upenn.cis.cis455.m2.interfaces.Filter;

public class WebService extends edu.upenn.cis.cis455.m1.server.WebService {
    final static Logger logger = LogManager.getLogger(WebService.class);

    public WebService() {
        super();
    }

    ///////////////////////////////////////////////////
    // For more advanced capabilities
    ///////////////////////////////////////////////////

    /**
     * Handle an HTTP POST request to the path
     */
    public void post(String path, Route route) {}

    /**
     * Handle an HTTP PUT request to the path
     */
    public void put(String path, Route route) {}

    /**
     * Handle an HTTP DELETE request to the path
     */
    public void delete(String path, Route route) {}

    /**
     * Handle an HTTP HEAD request to the path
     */
    public void head(String path, Route route) {}

    /**
     * Handle an HTTP OPTIONS request to the path
     */
    public void options(String path, Route route) {}

    ///////////////////////////////////////////////////
    // HTTP request filtering
    ///////////////////////////////////////////////////

    /**
     * Add filters that get called before a request
     */
    public void before(Filter filter) {}

    /**
     * Add filters that get called after a request
     */
    public void after(Filter filter) {}

    /**
     * Add filters that get called before a request
     */
    public void before(String path, Filter filter) {}

    /**
     * Add filters that get called after a request
     */
    public void after(String path, Filter filter) {}

}
