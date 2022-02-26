package edu.upenn.cis.cis455.m2.mapper;

import edu.upenn.cis.cis455.m1.interfaces.HttpRequest;
import edu.upenn.cis.cis455.m2.interfaces.Filter;
import edu.upenn.cis.cis455.m2.server.WebService;
import edu.upenn.cis.cis455.m2.interfaces.Request;
import edu.upenn.cis.cis455.m2.interfaces.Route;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathMap {
    final static Logger logger = LogManager.getLogger(PathMap.class);
    WebService webService = WebService.getInstance();

    public Route getRouteMap(HttpRequest request){

        RouteMap routeMap = new RouteMap();
        String type = request.requestMethod();
        logger.info("the method is: " +type);
        switch(type){
            case "GET":
                routeMap = webService.getGetMap();
                break;
            case "PUT":
                routeMap = webService.getPutMap();
                break;

            case "POST":
                routeMap = webService.getPostMap();
                break;

            case "DELET":
                routeMap = webService.getDeleteMap();
                break;

            case "HEAD":
                routeMap = webService.getHeadMap();
                break;

            case "OPTION":
                routeMap = webService.getOptionsMap();
                break;
            default:
                logger.error("Method not support");
        }

        Map<String, Route> map = routeMap.getMap();
        logger.debug("***************" + request.uri());
        String realPath = request.uri();
        String[] realUris = realPath.split("/");
        logger.info("realUris: " + realUris);
        Map<String, String> params = new HashMap<>();

        Route res = null; // if found matching, return matched route.
        for(Map.Entry<String, Route> entry : map.entrySet()){
            String key = entry.getKey();
            Route val = entry.getValue();
            // if the registered path == request uri, return
            if(key.equals(realPath)||key.equals("*")){
                return val;
            }

            //split current registered uri
            logger.info("***********urikey:" +key);
            String[] uris = key.split("/", 0);
            for(String uri : uris){
                System.out.print("///" + uri);
            }
            System.out.println();
            logger.info("uris.length" + uris.length+ " !=" +realUris.length +  "realUris.length");

            //if registered uri length bigger, then even wild card cant match, continue to next registered key
            if(uris.length > realUris.length) continue;

            //if equal, loop the registered (key) uris
            for(int i = 0; i < uris.length; i++){
                String uri = uris[i];
                String parameter = realUris[i];
                if(uri.length() == 0) continue;

                logger.info("********************: loop number: " + i);
                logger.warn(uri);
                Character indicator = uri.charAt(0);
                logger.debug("indicator: " + indicator);

                boolean flag = indicator == ':';
                logger.debug("indicator is colon: " + flag);

                //if current Key.uri[i] has colon:, then we put the colon - parameter
                if(flag){
                    params.put(uri, parameter);
                    logger.info("____________adding_________________________________________________");
                    logger.info("param: " +parameter + "uri: " + uri);
                }else{
                    //if not flag, check if the non colon uri split match
                    logger.info("if not flag, check if the non colon uri split match");
                    if(!uri.equals(parameter) && !uri.equals("*")){
                        //if not match, then check if wild card. if also not wild card, this
                        break;
                    }

                }

                //if we match all the part except flagged part : colon one, we set res = val, means route found.
                if(i >= uris.length - 1){
                    //match the end of registered uri. the real uri maybe longer
                    if(uri.equals("*") || uris.length == realUris.length){ //if wild card, we match everything,
                        // if they are equal and we match to the end, we get a match too
                        request.setparams(params);
                        logger.info("val--------------->" + val.toString());
                        res = val;
                        //and set the params into the map.
                        request.setparams(params);
                    }else{//not wild card, no equal length, no match
                        logger.warn("No Route match");
                    }
                }
            }
        }

        return res;
    }

    public List<Filter> getFilterMap(HttpRequest request, boolean before){
        Map<String, Filter> map = webService.getAfterFilter().getMap();
        if(before){
            map = webService.getBeforeFilter().getMap();
        }

        String realPath = request.uri();
        logger.debug("request uri: " + realPath);


        String[] realUris = realPath.split("/");
        logger.info("realUris: " + realUris);

        Map<String, String> params = new HashMap<>();

        List<Filter> res = new ArrayList<>(); // if found matching, return matched route.
        for(Map.Entry<String, Filter> entry : map.entrySet()){
            String key = entry.getKey();
            Filter val = entry.getValue();
            // if the registered path == request uri, return
            if(key.equals(realPath)||key.equals("/*")){
                res.add(val);
                continue;
            }

            //split current registered uri
            logger.info("***********urikey:" +key);
            String[] uris = key.split("/", 0);
            for(String uri : uris){
                System.out.print("///" + uri);
            }
            System.out.println();
            logger.info("uris.length" + uris.length+ " !=" +realUris.length +  "realUris.length");

            //if registered uri length bigger, then even wild card cant match, continue to next registered key
            if(uris.length > realUris.length) continue;

            //if equal, loop the registered (key) uris
            for(int i = 0; i < uris.length; i++){
                String uri = uris[i];
                String parameter = realUris[i];
                if(uri.length() == 0) continue;

                logger.info("********************: loop number: " + i);
                logger.warn(uri);
                Character indicator = uri.charAt(0);
                logger.debug("indicator: " + indicator);

                boolean flag = indicator == ':';
                logger.debug("indicator is colon: " + flag);

                //if current Key.uri[i] has colon:, then we put the colon - parameter
                if(flag){
                    params.put(uri, parameter);
                    logger.info("____________adding_________________________________________________");
                    logger.info("param: " +parameter + "uri: " + uri);
                }else{
                    //if not flag, check if the non colon uri split match
                    logger.info("if not flag, check if the non colon uri split match");
                    if(!uri.equals(parameter) && !uri.equals("*")){
                        //if not match, then check if wild card. if also not wild card, this
                        break;
                    }

                }

                //if we match all the part except flagged part : colon one, we set res = val, means route found.
                if(i >= uris.length - 1){
                    //match the end of registered uri. the real uri maybe longer
                    if(uri.equals("*") || uri.length() == realUris.length){ //if wild card, we match everything,
                        // if they are equal and we match to the end, we get a match too
                        request.setparams(params);
                        logger.info("val--------------->" + val.toString());
                        res.add(val);
                        //and set the params into the map.
                        request.setparams(params);
                    }else{//not wild card, no equal length, no match
                        logger.warn("No Route match");
                    }
                }
            }
        }

        return res;
    }

}
