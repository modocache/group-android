package com.modocache.android.group.api.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.modocache.android.group.api.models.Post;
import com.modocache.android.group.api.models.User;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "GroupDB.sqlite";
    private static final int DATABASE_VERSION = 1;

    private Dao<User, String> userDao = null;
    private Dao<Post, String> postDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // OrmLiteSqliteOpenHelper Overrides
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Post.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(),
                  "An error occurred when creating the database.",
                  e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource,
                          int oldVersion,
                          int newVersion) {}


    // Public Interface
    public Dao<User, String> getUserDao() {
        if (this.userDao == null) {
            try {
                this.userDao = getDao(User.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.userDao;
    }

    public Dao<Post, String> getPostDao() {
        if (this.postDao == null) {
            try {
                this.postDao = getDao(Post.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.postDao;
    }
}
