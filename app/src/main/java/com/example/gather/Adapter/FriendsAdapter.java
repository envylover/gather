package com.example.gather.Adapter;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gather.Friend;
import com.example.gather.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;



public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private ArrayList<Friend> friends;

    public interface OnItemClickListener {
        public void OnItemClickListener(View v, Friend friends);
    }
    private OnItemClickListener onItemClickListener;
    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView touxiang;
        private TextView distanceText;
        private TextView locationText;
        private TextView nameText;
        private int position;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView.setOnClickListener(this);
            touxiang = itemView.findViewById(R.id.touxiang);
            distanceText = itemView.findViewById(R.id.distanceText);
            locationText = itemView.findViewById(R.id.locationText);
            nameText = itemView.findViewById(R.id.name);
        }

        @Override
        public void onClick(View view) {
            if(onItemClickListener != null) {
                view.setBackgroundColor(Color.GRAY);
                onItemClickListener.OnItemClickListener(view, friends.get(position));
                view.setBackgroundColor(Color.WHITE);
            }
        }
    }

    public FriendsAdapter(ArrayList<Friend> friends){
        this.friends = friends;
    }

    public FriendsAdapter() {
        friends = new ArrayList<>();
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    @NonNull
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.ViewHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.distanceText.setText("距离您： " + friend.getDistance());
        holder.locationText.setText("所在位置为： " + friend.getLocation());
        holder.nameText.setText(friend.getName());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

