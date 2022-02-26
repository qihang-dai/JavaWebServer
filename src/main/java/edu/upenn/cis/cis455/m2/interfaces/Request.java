package edu.upenn.cis.cis455.m2.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Expanded version of the request API for Milestone 2
 */
public abstract class Request extends edu.upenn.cis.cis455.m1.interfaces.Request {
    /**
     * @return Gets the session associated with this request
     */
    public abstract Session session();

    /**
     * @return Gets or creates a session for this request
     */
    public abstract Session session(boolean create);

    /**
     * @return a map containing the route parameters
     */
    public abstract Map<String, String> params(

    );

    /**
     * @return the named parameter Example: parameter 'name' from the following
     *         pattern: (get '/hello/:name')
     */
    public String params(String param) {
        if (param == null)
            return null;

        if (param.startsWith(":"))
            return params().get(param.toLowerCase());
        else
            return params().get(':' + param.toLowerCase());
    }

    /**
     * @return Query parameter from the URL
     */
    public abstract String queryParams(String param);

    public String queryParamOrDefault(String param, String def) {
        String ret = queryParams(param);

        return (ret == null) ? def : ret;
    }

    /**
     * @return Get the list of values for the query parameter
     */
    public abstract List<String> queryParamsValues(String param);

    public abstract Set<String> queryParams();

    /**
     * @return The raw query string
     */
    public abstract String queryString();

    /**
     * Add an attribute to the request (eg in a filter)
     */
    public abstract void attribute(String attrib, Object val);

    /**
     * @return Gets an attribute attached to the request
     */
    public abstract Object attribute(String attrib);

    /**
     * @return All attributes attached to the request
     */
    public abstract Set<String> attributes();

    public abstract Map<String, String> cookies();

    public String cookie(String name) {
        if (name == null || cookies() == null)
            return null;
        else
            return cookies().get(name);
    }

}
