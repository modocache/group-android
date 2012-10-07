package com.modocache.android.group.api;

import org.json.JSONException;
import org.json.JSONObject;

import com.modocache.android.group.api.GroupPermissions.PermissionType;

import android.os.Parcel;
import android.os.Parcelable;

abstract public class GroupJSONObject implements Parcelable {
    protected JSONObject jsonObject;


    // Constructors
    public GroupJSONObject(JSONObject jsonObject) {
        super();
        this.jsonObject = jsonObject;
        setJSONObjectValues();
    }

    protected GroupJSONObject(Parcel parcel) throws JSONException {
        this.jsonObject = new JSONObject(parcel.readString());
        setJSONObjectValues();
    }


    // Parcelable Interface Methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jsonObject.toString());
    }


    // Public Interface
    public Boolean can(PermissionType type) {
        JSONObject permissions = null;
        try {
            permissions = this.jsonObject.getJSONObject("permissions");
        } catch (JSONException e) {
            return false;
        }

        try {
            return permissions.getBoolean(type.toString());
        } catch (JSONException e) {
            return false;
        }
    }


    // Protected Interface
    abstract protected void setJSONObjectValues();

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