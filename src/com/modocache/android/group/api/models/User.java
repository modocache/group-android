package com.modocache.android.group.api.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class User extends BaseModel {
    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField(index=true)
    private String uuid;
    @DatabaseField
    private String email;
    @DatabaseField // TODO: Use date objects
    private String createdAt;
    @DatabaseField
    private String updatedAt;

    @ForeignCollectionField
    private ForeignCollection<Post> posts;


    // java.lang.Object Overrides
    @Override
    public String toString() {
        return this.getEmail() + ", " + Integer.toString(this.getId()) + ", " + this.getUuid();
    }


    // Public Interface
    public User() {}
    public User(JSONObject jsonObject) {
        super();
        this.uuid = getStringAttribute(jsonObject, "id", "");
        this.email = getStringAttribute(jsonObject, "email", "");
        this.createdAt = getStringAttribute(jsonObject, "created_at", "");
        this.updatedAt = getStringAttribute(jsonObject, "updated_at", "");
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getUuid() {
        return this.uuid;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setPosts(ForeignCollection<Post> posts) {
        this.posts = posts;
    }
    public List<Post> getPosts() {
        ArrayList<Post> postList = new ArrayList<Post>();
        for (Post post : this.posts) {
            postList.add(post);
        }
        return postList;
    }
}
