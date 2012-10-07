package com.modocache.android.group;

import com.modocache.android.group.api.GroupAPIEngine;
import com.modocache.android.group.api.GroupAPIEngine.GroupAPIEngineDelegate;
import com.modocache.android.group.api.Post;
import com.modocache.android.group.api.User;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PostListFragment extends ListFragment implements GroupAPIEngineDelegate {
    private Post[] posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GroupAPIEngine.getSharedEngine().setDelegate(this);
        GroupAPIEngine.getSharedEngine().fetchPosts();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getActivity().getBaseContext(),
                       this.posts[position].toString(),
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEngineDidLoadPosts(Post[] posts) {
        this.posts = posts;
        setListAdapter(new ArrayAdapter<Post>(getActivity(),
                                              android.R.layout.simple_list_item_1,
                                              posts));
    }

    @Override
    public void onEngineError(Error error) {
        Toast.makeText(getActivity().getBaseContext(),
                       "An error occurred.",
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEngineDidLoadUsers(User[] users) {}
}