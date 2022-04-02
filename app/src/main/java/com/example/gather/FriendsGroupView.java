package com.example.gather;

import android.graphics.LinearGradient;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gather.Adapter.FriendsAdapter;
import com.example.gather.ViewModel.FriendsViewModel;

import java.util.ArrayList;

public class FriendsGroupView extends Fragment {
    private RecyclerView recyclerView;
    private FriendsViewModel friends;
    private FriendsAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.friends_list, container,false);
         recyclerView = view.findViewById(R.id.friend_list);
         friends = new ViewModelProvider(requireActivity()).get(FriendsViewModel.class);
        this.friends.Observe(this, friends -> {
            if(friends != null) {
                if(adapter != null) {
                    adapter.setFriends((ArrayList<Friend>) friends);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(inflater.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
