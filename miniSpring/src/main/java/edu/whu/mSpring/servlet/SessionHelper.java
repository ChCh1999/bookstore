package edu.whu.mSpring.servlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHelper {
    private static Map<String,Object> sessions = new ConcurrentHashMap<>();

    public static void putSession(String key,Object value){
        sessions.put(key,value);
    }

    public static Object getSession(String key){
        return sessions.get(key);
    }

    public static void deleteSession(String key){
        sessions.remove(key);
    }

}
