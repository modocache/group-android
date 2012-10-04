package com.modocache.android.group;

import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class GroupSherlockActivity extends SherlockActivity {
    private static enum MenuGroups { NAVIGATION }
    private static enum NavigationMenuItems { POSTS, USERS }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(MenuGroups.NAVIGATION.ordinal(),
                 NavigationMenuItems.POSTS.ordinal(),
                 NavigationMenuItems.POSTS.ordinal(),
                 getString(R.string.menu_item_title_posts));
        menu.add(MenuGroups.NAVIGATION.ordinal(),
                 NavigationMenuItems.USERS.ordinal(),
                 NavigationMenuItems.USERS.ordinal(),
                 getString(R.string.menu_item_title_users));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == NavigationMenuItems.POSTS.ordinal()) {
            Toast.makeText(getBaseContext(), "Clicked POSTS", Toast.LENGTH_SHORT).show();
            return true;
        } else if (menuItemId == NavigationMenuItems.USERS.ordinal()) {
            Toast.makeText(getBaseContext(), "Clicked USERS", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
