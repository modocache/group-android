package com.modocache.android.group.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Post extends GroupJSONObject {
    public User user;
    public String id;
    public String title;
    public String body;
    public String created_at;
    public String updated_at;


    // Constructors
    public Post(JSONObject jsonObject) {
        super(jsonObject);
    }

    private Post(Parcel parcel) throws JSONException {
        super(parcel);
    }


    // java.lang.Object Overrides
     @Override
     public String toString() {
         return String.format("%s, by %s", this.title, this.user);
     }


    // GroupJSONObject Overrides
    @Override
    protected void setJSONObjectValues() {
        this.user = getUser();
        this.id = getStringAttribute("id", "");
        this.title = getStringAttribute("title", "");
        this.body = getStringAttribute("body", "");
        this.created_at = getStringAttribute("created_at", "");
        this.updated_at = getStringAttribute("updated_at", "");
    }


     // Parcelable Interface Methods
    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel in) {
            try {
                return new Post(in);
            } catch (JSONException e) {
                return null;
            }
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };


    // Private Interface
    private User getUser() {
        try {
            return new User(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            return null;
        }
    }
}
