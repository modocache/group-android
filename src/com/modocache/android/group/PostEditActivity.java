package com.modocache.android.group;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.modocache.android.group.api.Post;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class PostEditActivity extends SherlockActivity {

    public static final String INTENT_POST_KEY = "INTENT_POST_KEY";
    private static final int MENU_SAVE_POST_ITEM_ID = 1;
    private EditText titleEditText;
    private EditText bodyEditText;


    // android.app.Activity Overrides
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_edit);

        titleEditText = (EditText) findViewById(R.id.edit_text_title);
        bodyEditText = (EditText) findViewById(R.id.edit_text_body);

        Post post = (Post) this.getIntent().getParcelableExtra(INTENT_POST_KEY);
        if (post != null) {
            titleEditText.setText(post.title);
            bodyEditText.setText(post.body);
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
        if (titleEditText.length() <= 0) {
            errors.add(getString(R.string.post_validation_error_no_title));
        }
        if (bodyEditText.length() <= 0) {
            errors.add(getString(R.string.post_validation_error_no_body));
        }
        return errors;
    }
}