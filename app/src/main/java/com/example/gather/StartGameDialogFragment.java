package com.example.gather;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gather.Interface.Promise;
import com.example.gather.ViewModel.RoomViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class StartGameDialogFragment extends DialogFragment {

    private TextView roomName;
    private TextView password;
    public interface OnEntryRoomClickListener{
        public void OnEntryRoomClick (String name, String password);
        public void OnCancelClick();
    }
    OnEntryRoomClickListener onEntryRoomClickListener;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.join_room, null))
                // Add action buttons
                .setPositiveButton("进入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        if(onEntryRoomClickListener != null) {
                            onEntryRoomClickListener.OnEntryRoomClick(roomName.getText().toString(), password.getText().toString());
                        }

                    };
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(onEntryRoomClickListener != null) {
                            onEntryRoomClickListener.OnCancelClick();
                        }
                       dismiss();
                    }
                });
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.join_room, container, false);
        roomName = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        return view;
    }
    public void setEntryRoomClick(OnEntryRoomClickListener listener) {
        onEntryRoomClickListener = listener;
    }
}
