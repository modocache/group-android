package com.modocache.android.group.api;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupJSONObject {
    protected JSONObject jsonObject;

    public GroupJSONObject(JSONObject jsonObject) {
        super();
        this.jsonObject = jsonObject;
    }

    protected Integer getIntAttribute(String attributeName, Integer defaultValue) {
        try {
            return jsonObject.getInt(attributeName);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    protected String getStringAttribute(String attributeName, String defaultValue) {
        try {
            return jsonObject.getString(attributeName);
        } catch (JSONException e) {
            return defaultValue;
        }
    }
}