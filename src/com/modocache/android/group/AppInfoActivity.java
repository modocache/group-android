package com.modocache.android.group;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

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
import android.widget.Toast;

public class AppInfoActivity extends Activity {
    DefaultHttpClient httpClient = new DefaultHttpClient();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account account = (Account) intent.getExtras().get(AccountListActivity.intentAccountKey);
        accountManager.getAuthToken(account, "oauth2:https://www.googleapis.com/auth/userinfo.email", false, new GetAuthTokenCallback(), null);
    }
    
    private class GetAuthTokenCallback implements AccountManagerCallback {
        @Override
        public void run(AccountManagerFuture result) {
            Bundle bundle;
            try {
                bundle = (Bundle) result.getResult();
                Intent intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
                if (intent != null) {
                    startActivity(intent);
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
    
    private class GetCookieTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... tokens) {
            try {
                httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
                Log.w("TOKEN", tokens[0]);

                HttpGet httpGet = new HttpGet("http://10.0.2.2:3000/auth/google_apps/android_login?token=" + tokens[0]);
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
            Log.w("executing authenticated request task", "im gonna go for it");
            new AuthenticatedRequestTask().execute("http://10.0.2.2:3000/posts.json");
        }
    }
    
    private class AuthenticatedRequestTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... urlStrings) {
            try {
                Log.w("doInBackground", urlStrings[0]);
                HttpGet httpGet = new HttpGet(urlStrings[0]);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                Log.w("response body", stringBuilder.toString());
                
                httpEntity.consumeContent();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
