package com.modocache.android.group.api.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.modocache.android.group.api.rest.RestMethod.RestMethodType;

public class RestHTTPRequestTask extends AsyncTask<String, Void, RestResponse> {
    private final RestMethodType type;
    private final HttpParams httpParams;
    private List<RestMethodCallbackListener> listeners;

        public RestHTTPRequestTask(RestMethodType type, HttpParams httpParams) {
            this.type = type;
            this.httpParams = httpParams;
        }

        public void addListener(RestMethodCallbackListener listener) {
            this.listeners.add(listener);
        }

        public void removeListener(RestMethodCallbackListener listener) {
            this.listeners.remove(listener);
        }

        @Override
        protected RestResponse doInBackground(String... urlStrings) {
            String urlString = urlStrings[0];

            HttpUriRequest httpUriRequest = getHttpUriRequestForType(this.type, urlString);
            httpUriRequest.setParams(this.httpParams);

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(httpUriRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (httpResponse == null) {
                return null;
            }

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            InputStream inputStream = null;
            try {
                inputStream = httpResponse.getEntity().getContent();
                Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
                if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                    inputStream = new GZIPInputStream(inputStream);
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (inputStream == null) {
                return new RestResponse(this.type, statusCode, null);
            }

            StringBuilder stringBuilder = new StringBuilder();
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            String lineString;
            try {
                while ((lineString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(lineString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String responseBody = stringBuilder.toString();
            JSONObject responseObject = null;
            try {
                responseObject = new JSONObject(responseBody);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (responseObject == null) {
                return new RestResponse(this.type, statusCode, null);
            }

            return new RestResponse(this.type, statusCode, responseObject);
        }

        @Override
        protected void onPostExecute(RestResponse result) {
            super.onPostExecute(result);
            for (RestMethodCallbackListener listener : this.listeners) {
                listener.onPostExecute(result);
            }
        }

        private static HttpUriRequest getHttpUriRequestForType(RestMethodType type, String urlString) {
            HttpUriRequest httpUriRequest = null;
            switch (type) {
            case GET:
                httpUriRequest = new HttpGet(urlString);
                break;
            case POST:
                httpUriRequest = new HttpPost(urlString);
                break;
            case PUT:
                httpUriRequest = new HttpPut(urlString);
            case DELETE:
                httpUriRequest = new HttpDelete(urlString);
                break;
            default:
                String message = String.format("Unrecognized RestMethodType: %s", type.toString());
                throw new IllegalArgumentException(message);
            }

            httpUriRequest.setHeader("Content-Type", "application/json");
            httpUriRequest.setHeader("Accept-Encoding", "gzip");

            return httpUriRequest;
        }
}
