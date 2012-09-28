package com.modocache.android.group.api;

import org.apache.http.impl.client.DefaultHttpClient;

public class GroupHTTPClient extends DefaultHttpClient {
    private static GroupHTTPClient sharedClient;
    private GroupHTTPClient() {
        super();
    }

    public static GroupHTTPClient getSharedClient() {
        if (sharedClient == null) {
            synchronized (GroupHTTPClient.class) {
                if (sharedClient == null) {
                    sharedClient = new GroupHTTPClient();
                }
            }
        }
        return sharedClient;
    }
}
