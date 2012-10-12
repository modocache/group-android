package com.modocache.android.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.modocache.android.group.api.GroupAPIEngine;
import com.modocache.android.group.api.db.DatabaseManager;

public class GroupMainActivity extends SherlockFragmentActivity implements TabListener {
    private static enum MenuGroups { ACTION }
    private static enum ActionMenuItems { NEW_POST, RELOAD }
    private static enum FragmentPages { POSTS, USERS }

    ViewPager viewPager;
    GroupMainPagerAdapter pagerAdapter;


    // com.actionbarsherlock.app.SherlockFragmentActivity Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseManager.init(GroupMainActivity.this);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        pagerAdapter = new GroupMainPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int tabPosition = 0; tabPosition < pagerAdapter.getCount(); tabPosition++) {
            Tab tab = actionBar.newTab()
                        .setText(pagerAdapter.getPageTitle(tabPosition))
                        .setTabListener(this);
            if (tabPosition == FragmentPages.POSTS.ordinal()) {
                tab.setIcon(R.drawable.ic_action_posts);
            } else if (tabPosition == FragmentPages.USERS.ordinal()) {
                tab.setIcon(R.drawable.ic_action_users);
            }

            actionBar.addTab(tab);
        }
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

        MenuItem reloadMenuItem = menu.add(MenuGroups.ACTION.ordinal(),
                                           ActionMenuItems.RELOAD.ordinal(),
                                           ActionMenuItems.RELOAD.ordinal(),
                                           getString(R.string.menu_item_title_reload));
        reloadMenuItem.setIcon(R.drawable.ic_action_new_post);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuGroupId = item.getGroupId();
        int menuItemId = item.getItemId();

        if (menuGroupId == MenuGroups.ACTION.ordinal()) {
            if (menuItemId == ActionMenuItems.NEW_POST.ordinal()) {
                startActivity(new Intent(GroupMainActivity.this, PostEditActivity.class));
                return true;
            } else if (menuItemId == ActionMenuItems.RELOAD.ordinal()) {
                GroupAPIEngine.getSharedEngine().fetchPosts();
                GroupAPIEngine.getSharedEngine().fetchUsers();
            }
        }

        return false;
    }


    // com.actionbarsherlock.app.ActionBar.TabListener Interface Methods
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {}


    // Public Interface
    public class GroupMainPagerAdapter extends FragmentPagerAdapter {
        public GroupMainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == FragmentPages.POSTS.ordinal()) {
                return new PostListFragment();
            } else if (position == FragmentPages.USERS.ordinal()) {
                return new UserListFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return FragmentPages.values().length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == FragmentPages.POSTS.ordinal()) {
                return getString(R.string.tab_title_posts);
            } else if (position == FragmentPages.USERS.ordinal()) {
                return getString(R.string.tab_title_users);
            }

            return null;
        }
    }
}
