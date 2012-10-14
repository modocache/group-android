package com.modocache.android.group.api.models;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseModel { // TODO: Implement validation chain


    // Protected Interface
    protected String getStringAttribute(JSONObject jsonObject,
                                        String attributeName,
                                        String defaultValue) {
        try {
            return jsonObject.getString(attributeName);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    protected Boolean getBooleanAttribute(JSONObject jsonObject,
                                          String attributeName,
                                          Boolean defaultValue) {
        try {
            return jsonObject.getBoolean(attributeName);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
