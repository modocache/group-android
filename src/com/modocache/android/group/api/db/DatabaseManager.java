package com.modocache.android.group.api.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.modocache.android.group.api.models.Post;
import com.modocache.android.group.api.models.User;

public class DatabaseManager {
    static private DatabaseManager sharedManager;
    static public void init(Context context) {
        if (sharedManager == null) {
            sharedManager = new DatabaseManager(context);
        }
    }

    static public DatabaseManager getSharedManager() {
        return sharedManager;
    }

    private final DatabaseHelper databaseHelper;
    private DatabaseManager(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    private DatabaseHelper getDatabaseHelper() {
        return this.databaseHelper;
    }


    // Public Interface
    public List<User> getAllUsers() {
        List<User> users = null;
        try {
            users = getDatabaseHelper().getUserDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getUserWithUuid(String uuid) {
        User user = null;
        try {
            user = getDatabaseHelper().getUserDao().queryForId(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void addUser(User user) {
        try {
            getDatabaseHelper().getUserDao().create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try {
            getDatabaseHelper().getUserDao().update(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User addOrUpdateUser(User user) {
        if (getUserWithUuid(user.getUuid()) == null) {
            addUser(user);
        } else {
            updateUser(user);
        }

        return user;
    }

    public List<Post> getAllPosts() {
        List<Post> posts = null;
        try {
            posts = getDatabaseHelper().getPostDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public Post getPostWithUuid(String uuid) {
        Post post = null;
        try {
            post = getDatabaseHelper().getPostDao().queryForId(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    public void addPost(Post post) {
        try {
            getDatabaseHelper().getPostDao().create(post);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePost(Post post) {
        try {
            getDatabaseHelper().getPostDao().update(post);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Post addOrUpdatePost(Post post) {
        if (getPostWithUuid(post.getUuid()) == null) {
            addPost(post);
        } else {
            updatePost(post);
        }

        return post;
    }


}
