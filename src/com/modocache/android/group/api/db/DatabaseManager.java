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
        databaseHelper = new DatabaseHelper(context);
    }

    private DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
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

    public User getUserWithId(int id) {
        User user = null;
        try {
            user = getDatabaseHelper().getUserDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserWithUuid(String uuid) {
        if (uuid == null || uuid.equalsIgnoreCase("")) {
            return null;
        }

        User user = null;
        try {
            List<User> users = getDatabaseHelper().getUserDao().queryForEq("uuid", uuid);
            if (users.isEmpty()) {
                return null;
            } else {
                return users.get(0);
            }
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
        User persistedUser = getUserWithUuid(user.getUuid());
        if (persistedUser == null) {
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

    public Post getPostWithId(int id) {
        Post post = null;
        try {
            post = getDatabaseHelper().getPostDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    public Post getPostWithUuid(String uuid) {
        if (uuid == null || uuid.equalsIgnoreCase("")) {
            return null;
        }

        Post post = null;
        try {
            List<Post> posts = getDatabaseHelper().getPostDao().queryForEq("uuid", uuid);
            if (posts.isEmpty()) {
                return null;
            } else {
                return posts.get(0);
            }
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
        Post persistedPost = getPostWithUuid(post.getUuid());
        if (persistedPost == null) {
            addPost(post);
        } else {
            updatePost(post);
        }

        return post;
    }
}
