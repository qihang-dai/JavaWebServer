package edu.upenn.cis.cis455.m2.mapper;

import edu.upenn.cis.cis455.m2.interfaces.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class RouteMap {
    final static Logger logger = LogManager.getLogger(RouteMap.class);
    private Map<String, Route> map;

    public RouteMap(){
        map = new HashMap<>();
    }

    public void register(String path, Route route){
        if(path == null || route == null){
            logger.error("Not valid input");
            return;
        }
        String key = path;
        if(!map.containsKey(key)){
            map.put(key, route);
        }else{
            logger.info("path already exsit");
        }
    }

    public Map getMap(){
        return map;
    }

}
