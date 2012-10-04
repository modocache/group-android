package com.modocache.android.group.api;

import org.json.JSONException;
import org.json.JSONObject;

public class Post extends GroupJSONObject {
	public User user;
	public Integer id;
	public String title;
	public String body;
	public String created_at;
	public String updated_at;

	public Post(JSONObject jsonObject) {
		super(jsonObject);
		this.user = getUser();
		this.id = getIntAttribute("id", 0);
		this.title = getStringAttribute("title", "");
		this.body = getStringAttribute("body", "");
		this.created_at = getStringAttribute("created_at", "");
		this.updated_at = getStringAttribute("updated_at", "");
	}

	private User getUser() {
        try {
            return new User(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            return null;
        }
    }

	@Override
	public String toString() {
		return String.format("%s, by %s", this.title, this.user);
	}
}
