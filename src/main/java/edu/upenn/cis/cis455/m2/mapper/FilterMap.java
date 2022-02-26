package edu.upenn.cis.cis455.m2.mapper;

import edu.upenn.cis.cis455.m2.interfaces.Filter;
import edu.upenn.cis.cis455.m2.interfaces.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FilterMap {
    final static Logger logger = LogManager.getLogger(FilterMap.class);
    Map<String, Filter> map;

    public FilterMap(){
        map = new HashMap<>();
    }

    public void register(String path, Filter filter){
        if(path == null || filter == null){
            logger.error("Not valid input");
            return;
        }
        String key = path;
        if(!map.containsKey(key)){
            map.put(key, filter);
        }else{
            logger.info("path already exsit");
        }
    }
    public Map getMap(){
        return map;
    }
}
