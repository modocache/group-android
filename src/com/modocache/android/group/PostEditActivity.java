package com.modocache.android.group;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.modocache.android.group.api.db.DatabaseManager;
import com.modocache.android.group.api.models.Post;

public class PostEditActivity extends SherlockActivity {

    public static final String INTENT_POST_UUID_KEY = "INTENT_POST_UUID_KEY";
    private static final int MENU_SAVE_POST_ITEM_ID = 1;
    private EditText titleEditText;
    private EditText bodyEditText;


    // android.app.Activity Overrides
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.titleEditText = (EditText) findViewById(R.id.edit_text_title);
        this.bodyEditText = (EditText) findViewById(R.id.edit_text_body);

        String postUuid = this.getIntent().getStringExtra(INTENT_POST_UUID_KEY);
        Post post = DatabaseManager.getSharedManager().getPostWithUuid(postUuid);
        if (post != null) {
            this.titleEditText.setText(post.getTitle());
            this.bodyEditText.setText(post.getBody());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem saveMenuItem = menu.add(0,
                                         MENU_SAVE_POST_ITEM_ID,
                                         0,
                                         getString(R.string.menu_item_title_save_post));
        saveMenuItem.setIcon(R.drawable.ic_action_save);
        saveMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            Intent intent = new Intent(PostEditActivity.this,
                                       GroupMainActivity.class);
            startActivity(intent);
            return true;
        case MENU_SAVE_POST_ITEM_ID:
            ArrayList<String> errors = getValidationErrors();
            if (errors.size() <= 0) {
                Toast.makeText(PostEditActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
            } else {
                for (String error : errors) {
                    Toast.makeText(PostEditActivity.this,
                                   error,
                                   Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        default:
            return false;
        }
    }


    // Private Interface
    private ArrayList<String> getValidationErrors() {
        ArrayList<String> errors = new ArrayList<String>();
        if (this.titleEditText.length() <= 0) {
            errors.add(getString(R.string.post_validation_error_no_title));
        }
        if (this.bodyEditText.length() <= 0) {
            errors.add(getString(R.string.post_validation_error_no_body));
        }
        return errors;
    }
}
