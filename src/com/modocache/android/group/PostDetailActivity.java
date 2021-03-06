package com.modocache.android.group;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.modocache.android.group.api.db.DatabaseManager;
import com.modocache.android.group.api.models.Post;

public class PostDetailActivity extends SherlockActivity {
    private static enum MenuGroups { ACTION }
    private static enum ActionMenuItems { UPDATE }

    public static final String INTENT_POST_UUID_KEY = "INTENT_POST_UUID_KEY";
    private Post post;
    private TextView titleTextView;
    private TextView bodyTextView;


    // android.app.Activity Overrides
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.titleTextView = (TextView) findViewById(R.id.post_text_view_title);
        this.bodyTextView = (TextView) findViewById(R.id.post_text_view_body);

        String postUuid = this.getIntent().getStringExtra(INTENT_POST_UUID_KEY);
        this.post = DatabaseManager.getSharedManager().getPostWithUuid(postUuid);
        this.titleTextView.setText(this.post.getTitle());
        this.bodyTextView.setText(this.post.getBody());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.post.getCanUpdate()) {
            MenuItem postEditMenuItem = menu.add(MenuGroups.ACTION.ordinal(),
                                                 ActionMenuItems.UPDATE.ordinal(),
                                                 ActionMenuItems.UPDATE.ordinal(),
                                                 getString(R.string.menu_item_title_edit_post));
            postEditMenuItem.setIcon(R.drawable.ic_action_new_post);
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int groupId = item.getGroupId();
        int itemId = item.getItemId();

        if (groupId == MenuGroups.ACTION.ordinal()) {
            if (itemId == ActionMenuItems.UPDATE.ordinal()) {
                Intent intent = new Intent(PostDetailActivity.this,
                                           PostEditActivity.class);
                intent.putExtra(PostEditActivity.INTENT_POST_UUID_KEY, this.post.getUuid());
                startActivity(intent);
                return true;
            }
        }

        if (itemId == android.R.id.home) {
            Intent intent = new Intent(PostDetailActivity.this,
                                       GroupMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return false;
    }
}
