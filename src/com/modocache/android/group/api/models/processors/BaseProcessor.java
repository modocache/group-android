package com.modocache.android.group.api.models.processors;

import com.modocache.android.group.api.rest.RestMethod.RestMethodType;
import com.modocache.android.group.api.rest.RestMethodCallbackListener;

public abstract class BaseProcessor implements RestMethodCallbackListener {
    public abstract String getPathForRestMethodType(RestMethodType type, String uuid);
}
