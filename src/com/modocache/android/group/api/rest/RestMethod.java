package com.modocache.android.group.api.rest;


public class RestMethod {
    public static enum RestMethodType { GET, POST, PUT, DELETE }


    public static void execute(RestMethodType type, String path) {
        RestHTTPRequestTask task = new RestHTTPRequestTask(type, null);
        task.execute(path);
    }
}
