package com.modocache.android.group.api;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.modocache.android.group.api.rest.RestMethod.RestMethodType;

public class APIService extends Service {
    public static final String API_RESOURCE_USERS_KEY = "API_RESOURCE_USERS_KEY";
    public static final String API_RESOURCE_POSTS_KEY = "API_RESOURCE_POSTS_KEY";
    public static final String API_METHOD_GET_KEY = "API_METHOD_GET_KEY";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RestMethodType methodType = RestMethodType.GET;
        if (intent.getBooleanExtra(API_RESOURCE_USERS_KEY, false)) {
            executeRestMethodForUsers(methodType);
        } else if (intent.getBooleanExtra(API_RESOURCE_POSTS_KEY, false)) {
            executeRestMethodForPosts(methodType);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    private void executeRestMethodForUsers(RestMethodType type) {

    }

    private void executeRestMethodForPosts(RestMethodType type) {

    }
}
