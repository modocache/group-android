package com.modocache.android.group;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.modocache.android.group.api.GroupAPIEngine;
import com.modocache.android.group.api.GroupAPIEngine.GroupAPIEngineDelegate;
import com.modocache.android.group.api.db.DatabaseManager;
import com.modocache.android.group.api.models.User;

public class UserListFragment extends ListFragment implements GroupAPIEngineDelegate {
    private List<User> users;


    // android.support.v4.app.ListFragment Overrides
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_list_fragment, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        GroupAPIEngine.getSharedEngine().removeDelegate(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        onEngineDidLoadUsers();
        GroupAPIEngine.getSharedEngine().addDelegate(this);
        GroupAPIEngine.getSharedEngine().fetchPosts();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getActivity().getBaseContext(),
                       this.users.get(position).toString(),
                       Toast.LENGTH_SHORT).show();
    }


    // GroupAPIEngineDelegate Interface Methods
    @Override
    public void onEngineError(Error error) {
        Toast.makeText(getActivity().getBaseContext(),
                       "An error occurred.",
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEngineDidLoadUsers() {
        this.users = DatabaseManager.getSharedManager().getAllUsers();
        setListAdapter(new ArrayAdapter<User>(getActivity(),
                                              android.R.layout.simple_list_item_1,
                                              this.users));
    }

    @Override
    public void onEngineDidLoadPosts() {}
}
