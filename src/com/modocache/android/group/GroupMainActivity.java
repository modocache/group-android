package com.modocache.android.group;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class GroupMainActivity extends SherlockFragmentActivity {
    private static enum MenuGroups { ACTION, NAVIGATION }
    private static enum ActionMenuItems { NEW_POST }
    private static enum NavigationMenuItems { POSTS, USERS }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceListFragment(new PostListFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem newPostMenuItem = menu.add(MenuGroups.ACTION.ordinal(),
                                            ActionMenuItems.NEW_POST.ordinal(),
                                            ActionMenuItems.NEW_POST.ordinal(),
                                            getString(R.string.menu_item_title_new_post));
        newPostMenuItem.setIcon(R.drawable.ic_action_new_post);
        newPostMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem postsMenuItem = menu.add(MenuGroups.NAVIGATION.ordinal(),
                                           NavigationMenuItems.POSTS.ordinal(),
                                           NavigationMenuItems.POSTS.ordinal(),
                                           getString(R.string.menu_item_title_posts));
        postsMenuItem.setIcon(R.drawable.ic_action_posts);

        MenuItem usersMenuItem = menu.add(MenuGroups.NAVIGATION.ordinal(),
                                           NavigationMenuItems.USERS.ordinal(),
                                           NavigationMenuItems.USERS.ordinal(),
                                           getString(R.string.menu_item_title_users));
        usersMenuItem.setIcon(R.drawable.ic_action_users);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuGroupId = item.getGroupId();
        int menuItemId = item.getItemId();

        if (menuGroupId == MenuGroups.ACTION.ordinal()) {
            if (menuItemId == ActionMenuItems.NEW_POST.ordinal()) {
                Toast.makeText(getBaseContext(), "Clicked NEW POST", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else if (menuGroupId == MenuGroups.NAVIGATION.ordinal()) {
            if (menuItemId == NavigationMenuItems.POSTS.ordinal()) {
                replaceListFragment(new PostListFragment());
                return true;
            } else if (menuItemId == NavigationMenuItems.USERS.ordinal()) {
                replaceListFragment(new UserListFragment());
                return true;
            }
        }

        return false;
    }

    private void replaceListFragment(ListFragment listFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, listFragment);
        fragmentTransaction.commit();
    }
}
