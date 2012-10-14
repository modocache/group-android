package com.modocache.android.group.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.json.JSONArray;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.modocache.android.group.api.db.DatabaseManager;
import com.modocache.android.group.api.models.Post;
import com.modocache.android.group.api.models.User;

public class GroupAPIEngine {

    public interface GroupAPIEngineDelegate {
        public void onEngineDidLoadPosts();
        public void onEngineDidLoadUsers();
        public void onEngineError(Error error);
    }

    private static GroupAPIEngine sharedEngine;
    private ArrayList<GroupAPIEngineDelegate> delegates;
    private String apiHost;
    private String apiPort;
    private Activity currentActivity;
    private GroupAPIAuthenticationCallback currentCallback;

    public enum Environment {
        DEVELOPMENT_EMULATOR,
        DEVELOPMENT_DEVICE
    }

    private GroupAPIEngine() {}
    public static GroupAPIEngine getSharedEngine() {
        if (sharedEngine == null) {
            synchronized (GroupAPIEngine.class) {
                if (sharedEngine == null) {
                    sharedEngine = new GroupAPIEngine();
                    sharedEngine.setEnvironment(Environment.DEVELOPMENT_DEVICE);
                    sharedEngine.delegates = new ArrayList<GroupAPIEngineDelegate>();
                }
            }
        }
        return sharedEngine;
    }

    public void addDelegate(GroupAPIEngineDelegate delegate) {
        this.delegates.add(delegate);
    }

    public void removeDelegate(GroupAPIEngineDelegate delegate) {
        this.delegates.remove(delegate);
    }

    public void setEnvironment(Environment environment) {
        switch (environment) {
        case DEVELOPMENT_EMULATOR:
            this.apiHost = "10.0.2.2";
            this.apiPort = "3000";
            break;
        case DEVELOPMENT_DEVICE:
            this.apiHost = "192.168.1.3";
            this.apiPort = "3000";
            break;
        default:
            break;
        }
    }

    private String getHostURL() {
        return String.format("http://%s:%s", this.apiHost, this.apiPort);
    }

    private String getApiURL() {
        return String.format("%s/api/v1", getHostURL());
    }

    private String getLoginURL(String token) {
        return String.format("%s/auth/google_apps/android_login?token=%s",
                getHostURL(), token);
    }

    @SuppressWarnings("deprecation")
    public void authenticateAccount(Activity activity,
                                    Account account,
                                    GroupAPIAuthenticationCallback callback) {
        this.currentActivity = activity;
        this.currentCallback = callback;
        AccountManager accountManager = AccountManager.get(activity.getApplicationContext());
        accountManager.getAuthToken(account,
                "oauth2:https://www.googleapis.com/auth/userinfo.email",
                false,
                new GetAuthTokenCallback(),
                null);
    }

    private class GetAuthTokenCallback implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            try {
                Bundle bundle = result.getResult();
                Intent intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
                if (intent != null) {
                    GroupAPIEngine.this.currentActivity.startActivity(intent);
                } else {
                    onGetAuthToken(bundle);
                }
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            } catch (AuthenticatorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onGetAuthToken(Bundle bundle) {
        String authTokenString = bundle.getString(AccountManager.KEY_AUTHTOKEN);
        new GetCookieTask().execute(authTokenString);
    }

    public class GetCookieTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... tokens) {
            Log.w("GETCOOKIE", "GetCookieTask.doInBackground, token = " + tokens[0]);
            GroupHTTPClient httpClient = GroupHTTPClient.getSharedClient();
            try {
                httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
                Log.w("TOKEN", tokens[0]);

                HttpGet httpGet = new HttpGet(getLoginURL(tokens[0]));
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpResponse.getStatusLine().getStatusCode() != 302) {
                    Log.w("RESPONSE", "Received redirect response.");
                    httpEntity.consumeContent();
                    return false;
                }

                for (Cookie cookie : httpClient.getCookieStore().getCookies()) {
                    Log.w("COOKIE", cookie.toString());
                }
                httpEntity.consumeContent();
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            GroupAPIEngine.this.currentCallback.onAuthenticationComplete(result);
        }
    }

    public void fetchPosts() {
        new ReadPostsTask().execute(getApiURL() + "/posts.json");
    }

    private class ReadPostsTask extends AsyncTask<String, Post, String> {
        @Override
        protected String doInBackground(String... urls) {
            return readJSONFeed(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                JSONArray postsJSONArray = resultObject.getJSONArray("posts");
                for (int i = 0; i < postsJSONArray.length(); i++) {
                    Post post = new Post(postsJSONArray.getJSONObject(i));
                    DatabaseManager.getSharedManager().addOrUpdatePost(post);
                }

                for (GroupAPIEngineDelegate delegate : GroupAPIEngine.this.delegates) {
                    delegate.onEngineDidLoadPosts();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void fetchUsers() {
        new ReadUsersTask().execute(getApiURL() + "/users.json");
    }

    private class ReadUsersTask extends AsyncTask<String, User, String> {
        @Override
        protected String doInBackground(String... urls) {
            return readJSONFeed(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                JSONArray usersJSONArray = resultObject.getJSONArray("users");
                for (int i = 0; i < usersJSONArray.length(); i++) {
                    User user = new User(usersJSONArray.getJSONObject(i));
                    DatabaseManager.getSharedManager().addOrUpdateUser(user);
                }

                for (GroupAPIEngineDelegate delegate : GroupAPIEngine.this.delegates) {
                    delegate.onEngineDidLoadUsers();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String readJSONFeed(String urlString) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpGet httpGet = new HttpGet(urlString);

        try {
            HttpResponse httpResponse = GroupHTTPClient.getSharedClient().execute(httpGet);
            StatusLine statusLine = httpResponse.getStatusLine();

            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStreamReader streamReader = new InputStreamReader(httpEntity.getContent());
                BufferedReader bufferedReader = new BufferedReader(streamReader);

                String lineString;
                while ((lineString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(lineString);
                }
            } else {
                Error error = new Error("Could not access resource.");
                for (GroupAPIEngineDelegate delegate : this.delegates) {
                    delegate.onEngineError(error);
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
