package com.modocache.android.group.api.rest;

import org.json.JSONObject;

import com.modocache.android.group.api.rest.RestMethod.RestMethodType;

public class RestResponse {
    private final RestMethodType type;
    private final Integer status;
    private final JSONObject body;

    public RestResponse(RestMethodType type, Integer status, JSONObject body) {
        this.type = type;
        this.status = status;
        this.body = body;
    }

    public RestMethodType getType() {
        return this.type;
    }

    public Integer getStatus() {
        return this.status;
    }

    public JSONObject getBody() {
        return this.body;
    }
}
