package com.modocache.android.group.api;

import org.json.JSONObject;

public class User extends GroupJSONObject {
	public Integer id;
	public String email;

	public User(JSONObject jsonObject) {
		super(jsonObject);
		this.id = getIntAttribute("id", 0);
		this.email = getStringAttribute("email", "");
	}

	@Override
	public String toString() {
		return this.email;
	}
}
