package com.modocache.android.group.api.models;

import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Post extends BaseModel {
    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField(index=true)
    private String uuid;
    @DatabaseField
    private String title;
    @DatabaseField
    private String body;
    @DatabaseField // TODO: Use date objects
    private String createdAt;
    @DatabaseField
    private String updatedAt;

    @DatabaseField(foreign=true, foreignAutoRefresh=true)
    User user;


    // Public Interface
    public Post() {}
    public Post(JSONObject jsonObject) {
        this.uuid = getStringAttribute(jsonObject, "id", "");
        this.title = getStringAttribute(jsonObject, "title", "");
        this.body = getStringAttribute(jsonObject, "body", "");
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

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public String getBody() {
        return this.body;
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

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return this.user;
    }
}
