package com.modocache.android.group.api;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;

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

public class GroupAPIEngine {

    private static GroupAPIEngine sharedEngine;
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
                }
            }
        }
        return sharedEngine;
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
                    currentActivity.startActivity(intent);
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
            currentCallback.onAuthenticationComplete(result);
        }
    }

    public void fetchPosts(GroupAPICallback callback) {
        RequestTask task = new RequestTask();
        task.setCallback(callback);
        task.execute(getHostURL() + "/posts.json");
    }

    private class RequestTask extends AsyncTask<String, Void, Void> {
        private GroupAPICallback callback;
        public void setCallback(GroupAPICallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(String... urlStrings) {
            try {
                HttpGet httpGet = new HttpGet(urlStrings[0]);
                HttpResponse httpResponse = GroupHTTPClient.getSharedClient().execute(httpGet);
                this.callback.onResponse(httpResponse);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
