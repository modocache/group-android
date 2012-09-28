package com.modocache.android.group;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.modocache.android.group.api.GroupAPICallback;
import com.modocache.android.group.api.GroupAPIEngine;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class PostListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroupAPIEngine.getSharedEngine().fetchPosts(new PostsCallback());
    }

    public void displayPosts(final String postsString) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), postsString, Toast.LENGTH_LONG).show();
            }
        });
    }

    private class PostsCallback implements GroupAPICallback {
        @Override
        public void onResponse(HttpResponse response) {
            try {
                HttpEntity httpEntity = response.getEntity();
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                httpEntity.consumeContent();
                displayPosts(stringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
