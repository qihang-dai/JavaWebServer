package edu.upenn.cis.cis455.m2.interfaces;

import java.util.Set;

public abstract class Session {
    public abstract String id();

    public abstract boolean validate();
    /**
     * Time the session was created
     */
    public abstract long creationTime();

    /**
     * Time the session was last accessed
     */
    public abstract long lastAccessedTime();

    /**
     * Invalidate the session
     */
    public abstract void invalidate();

    /**
     * Get the inactivity timeout
     */
    public abstract int maxInactiveInterval();

    /**
     * Set the inactivity timeout
     */
    public abstract void maxInactiveInterval(int interval);

    /**
     * Notify the session that it was just accessed -- should update the last
     * accessed time
     */
    public abstract void access();

    /**
     * Store an object under the name
     */
    public abstract void attribute(String name, Object value);

    /**
     * Get an object associated with the name
     */
    public abstract Object attribute(String name);

    /**
     * Get all objects bound to the session
     */
    public abstract Set<String> attributes();

    /**
     * Delete an object from the session
     */
    public abstract void removeAttribute(String name);
}
