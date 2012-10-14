package com.modocache.android.group.api.models.processors;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.modocache.android.group.api.db.DatabaseManager;
import com.modocache.android.group.api.models.User;
import com.modocache.android.group.api.rest.RestHTTPRequestTask;
import com.modocache.android.group.api.rest.RestMethod.RestMethodType;
import com.modocache.android.group.api.rest.RestResponse;

public class UserProcessor extends BaseProcessor {
    public static final String USERS_PATH = "users";
    public static final String USERS_JSON_ARRAY_KEY = "users";
    public static final String USERS_JSON_OBJECT_KEY = "user";

    public static interface UserProcessorCallbackListener {
        public void onOperationCompleted();
    }

    public void getUsers() {
        RestMethodType methodType = RestMethodType.GET;
        RestHTTPRequestTask task = new RestHTTPRequestTask(methodType, null);
        task.addListener(this);
        task.execute(this.getPathForRestMethodType(methodType, null));
    }

    // BaseProcessor Overrides
    @Override
    public String getPathForRestMethodType(RestMethodType type, String uuid) {
       switch (type) {
           case GET:
           case POST:
               return String.format("%s.json", USERS_PATH);
           case PUT:
           case DELETE:
               return String.format("%s/%s.json", USERS_PATH, uuid);
       }

       String message = String.format("Unrecognized RestMethodType: %s", type.toString());
       throw new IllegalArgumentException(message);
    }


    // RestMethodCallbackListener Interface
    @Override
    public void onPostExecute(RestResponse response) {
        switch (response.getType()) {
        case GET:
            List<User> users = getUsersFromResponse(response);
            for (User user : users) {
                DatabaseManager.getSharedManager().addOrUpdateUser(user);
            }
            break;
        default:
            break;
        }
    }


    // Private Interface
    private List<User> getUsersFromResponse(RestResponse response) {
        if (response.getType() != RestMethodType.GET) {
            throw new IllegalArgumentException("List of users can only be retreived from GET method.");
        }

        List<User> users = new ArrayList<User>();
        JSONArray usersArray = null;
        try {
            usersArray = response.getBody().getJSONArray(USERS_JSON_ARRAY_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
            return users;
        }

        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject userJsonObject = null;
            try {
                userJsonObject = usersArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (userJsonObject != null) {
                User user = new User(userJsonObject);
                users.add(user);
            }
        }

        return users;
    }

    private User getUserFromResponse(RestResponse response) {
        if (response.getType() == RestMethodType.GET) {
            throw new IllegalArgumentException("Single user cannot be retreived from GET method.");
        }

        try {
            JSONObject userObject = response.getBody().getJSONObject(USERS_JSON_OBJECT_KEY);
            return new User(userObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
