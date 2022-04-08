package com.example.gather;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class RegisterDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private EditText roomName;

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(onEntryRoomClickListener != null) {
            onEntryRoomClickListener.OnEntryRoomClick(roomName.getText().toString(),null);
        }
    }

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
        View view = inflater.inflate(R.layout.register_layout, null);
        roomName = view.findViewById(R.id.user_name);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("注册", this)
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
        View view = inflater.inflate(R.layout.register_layout, container, false);

        return view;
    }



    public void setEntryRoomClick(OnEntryRoomClickListener listener) {
        onEntryRoomClickListener = listener;
    }
}