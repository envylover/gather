package com.example.gather;

import android.content.Context;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gather.Adapter.FriendsAdapter;
import com.example.gather.Interface.Promise;
import com.example.gather.ViewModel.FriendViewModel;
import com.example.gather.ViewModel.FriendsViewModel;
import com.example.gather.ViewModel.LoginViewModel;
import com.example.gather.ViewModel.RoomViewModel;
import com.example.gather.util.CheckRoom;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FriendsGroupView extends Fragment implements SwipeRefreshLayout.OnRefreshListener, FriendsAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private FriendsViewModel friends;
    private SwipeRefreshLayout refreshLayout;
    private Context context;
    private RoomViewModel roomInfo;
    private FriendsAdapter adapter;
    private LoginViewModel useInfo;
    private FriendViewModel friend;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        friends = new ViewModelProvider(requireActivity()).get(FriendsViewModel.class);
        roomInfo = new ViewModelProvider(requireActivity()).get(RoomViewModel.class);
        adapter = new FriendsAdapter();
        adapter.setOnItemClickListener(this);
        useInfo = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        friend = new ViewModelProvider(requireActivity()).get(FriendViewModel.class);
        this.friends.Observe(getActivity(), friends -> {
            if(friends != null) {
                if(adapter != null) {
                    adapter.setFriends((ArrayList<Friend>) friends);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.friends_list, container,false);
         context = inflater.getContext();
         recyclerView = view.findViewById(R.id.friend_list);
         refreshLayout = view.findViewById(R.id.swipeRefresh);
         RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(inflater.getContext());
         recyclerView.setLayoutManager(layoutManager);
         recyclerView.setAdapter(adapter);
         refreshLayout.setOnRefreshListener(this);
         return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<Friend> friends1 = friends.getFriends();
        if(friends1 != null && friends1.size() > 0)
          adapter.setFriends(friends.getFriends());
    }


    @Override
    public void onRefresh() {
        new Thread(() -> {
            if(roomInfo != null) {
                roomInfo.setRoomInfo(roomInfo.getRoomInfo());
                refreshLayout.setRefreshing(false);
            }

        }).start();
    }

    @Override
    public void OnItemClickListener(View v, Friend friend) {
        this.friend.setFriend(friend);
        UserActivity userActivity = (UserActivity) requireActivity();
        userActivity.setBottomMapNavigationSelect();
    }
}
