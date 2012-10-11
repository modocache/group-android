package com.modocache.android.group.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class User extends GroupJSONObject {
    public String id;
    public String email;


    // Constructors
    public User(JSONObject jsonObject) {
        super(jsonObject);
    }

    private User(Parcel parcel) throws JSONException {
        super(parcel);
    }


    // java.lang.Object Overrides
    @Override
    public String toString() {
        return this.email;
    }

    // GroupJSONObject Overrides
    @Override
    protected void setJSONObjectValues() {
        this.id = getStringAttribute("id", "");
        this.email = getStringAttribute("email", "");
    }


    // Parcelable Interface Methods
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            try {
                return new User(in);
            } catch (JSONException e) {
                return null;
            }
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
