package com.modocache.android.group.api.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.modocache.android.group.api.db.DatabaseManager;

@DatabaseTable
public class Post extends BaseModel {
    @DatabaseField(id=true, index=true)
    private String uuid;
    @DatabaseField
    private String title;
    @DatabaseField
    private String body;
    @DatabaseField
    private Boolean canUpdate;
    @DatabaseField
    private Boolean canDestroy;
    @DatabaseField // TODO: Use date objects
    private String createdAt;
    @DatabaseField
    private String updatedAt;

    @DatabaseField(foreign=true, foreignAutoRefresh=true, canBeNull=false)
    private User user;


    // java.lang.Object Overrides
    @Override
    public String toString() {
        return String.format("%s, by %s", this.getTitle(), this.getUser());
    }


    // Public Interface
    public Post() {}
    public Post(JSONObject jsonObject) {
        this.setUuid(getStringAttribute(jsonObject, "id", ""));
        this.setTitle(getStringAttribute(jsonObject, "title", ""));
        this.setBody(getStringAttribute(jsonObject, "body", ""));
        this.setCreatedAt(getStringAttribute(jsonObject, "created_at", ""));
        this.setUpdatedAt(getStringAttribute(jsonObject, "updated_at", ""));

        JSONObject userJsonObject = null;
        try {
            userJsonObject = jsonObject.getJSONObject("user");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (userJsonObject != null) {
            User user = new User(userJsonObject);
            DatabaseManager.getSharedManager().addOrUpdateUser(user);
            this.setUser(user);
        }

        JSONObject permissionsJsonObject = null;
        try {
            permissionsJsonObject = jsonObject.getJSONObject("permissions");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (permissionsJsonObject == null) {
            this.setCanUpdate(false);
            this.setCanDestroy(false);
        } else {
            this.setCanUpdate(getBooleanAttribute(permissionsJsonObject, "update", false));
            this.setCanDestroy(getBooleanAttribute(permissionsJsonObject, "destroy", false));
        }
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

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }
    public Boolean getCanUpdate() {
        return this.canUpdate;
    }

    public void setCanDestroy(Boolean canDestroy) {
        this.canDestroy = canDestroy;
    }
    public Boolean getCanDestroy() {
        return this.canDestroy;
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
