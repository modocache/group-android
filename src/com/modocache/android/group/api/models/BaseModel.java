package com.modocache.android.group.api.models;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseModel { // TODO: Implement validation chain
    protected String getStringAttribute(JSONObject jsonObject,
                                        String attributeName,
                                        String defaultValue) {
        try {
            return jsonObject.getString(attributeName);
        } catch (JSONException e) {
            return defaultValue;
        }
    }
}
