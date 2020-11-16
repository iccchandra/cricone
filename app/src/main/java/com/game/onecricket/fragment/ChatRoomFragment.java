package com.game.onecricket.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.game.onecricket.R;
import com.game.onecricket.chatroom.adapters.ChatRoomAdapter;
import com.game.onecricket.chatroom.models.Message;
import com.game.onecricket.chatroom.utils.ProfanityFilter;
import com.game.onecricket.chatroom.utils.SCUtils;
import com.game.onecricket.utils.NetworkState;
import com.game.onecricket.utils.SessionManager;
import com.game.onecricket.utils.crypto.AlertDialogHelper;

import java.util.ArrayList;

public class ChatRoomFragment extends Fragment {

    public static final int ANTI_FLOOD_SECONDS = 3; //simple anti-flood
    private boolean IS_ADMIN = false; //set this to true for the admin app.
    private String username = ""; //default username
    private boolean PROFANITY_FILTER_ACTIVE = true;
    private FirebaseDatabase database;
    private RecyclerView main_recycler_view;
    private String userID;
    private Context mContext;
    private ChatRoomAdapter adapter;
    private DatabaseReference databaseRef;
    private ImageButton imageButton_send;
    private EditText editText_message;
    private ArrayList<Message> messageArrayList = new ArrayList<>();
    private ProgressBar progressBar;
    private long last_message_timestamp = 0;
    private Context context;
    private AlertDialogHelper alertDialogHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);

        alertDialogHelper = AlertDialogHelper.getInstance();
        username = new SessionManager().getUser(context).getName();
        mContext = context;
        main_recycler_view = view.findViewById(R.id.main_recycler_view);
        imageButton_send = view.findViewById(R.id.imageButton_send);
        editText_message = view.findViewById(R.id.editText_message);
        progressBar = view.findViewById(R.id.progressBar);
        if (NetworkState.isNetworkAvailable(context)) {
            initialiseFirebase();
        }
        else {
            if (!alertDialogHelper.isShowing()) {
                alertDialogHelper.showAlertDialog(context,
                        getString(R.string.internet_error_title),
                        getString(R.string.no_internet_message));
            }
        }


        return view;
    }

    private void initialiseFirebase() {
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        progressBar.setVisibility(View.VISIBLE);
        main_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ChatRoomAdapter(mContext, messageArrayList);
        main_recycler_view.setAdapter(adapter);

        databaseRef.child("the_messages").limitToLast(50).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressBar.setVisibility(View.GONE);
                Message new_message = dataSnapshot.getValue(Message.class);
                messageArrayList.add(new_message);
                adapter.notifyDataSetChanged();
                main_recycler_view.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("REMOVED", dataSnapshot.getValue(Message.class).toString());
                messageArrayList.remove(dataSnapshot.getValue(Message.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageButton_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process_new_message(editText_message.getText().toString().trim(), false);
            }
        });

        editText_message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEND)) {
                    imageButton_send.performClick();
                }
                return false;
            }
        });

        logic_for_username();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    private void process_new_message(String new_message, boolean isNotification) {
        if (new_message.isEmpty()) {
            return;
        }

        //simple anti-flood protection
        if ((System.currentTimeMillis() / 1000L - last_message_timestamp) < ANTI_FLOOD_SECONDS) {
//            SCUtils.showErrorSnackBar(mContext, context.findViewById(android.R.id.content), "You cannot send messages so fast.").show();
            return;
        }

        //yes, admins can swear ;)
        if ((PROFANITY_FILTER_ACTIVE) && (!IS_ADMIN)) {
            new_message = new_message.replaceAll(ProfanityFilter.censorWords(ProfanityFilter.ENGLISH), ":)");
        }

        editText_message.setText("");

        Message xmessage = new Message(userID, username, new_message, System.currentTimeMillis() / 1000L, IS_ADMIN, isNotification);
        String key = databaseRef.child("the_messages").push().getKey();
        databaseRef.child("the_messages").child(key).setValue(xmessage);

        last_message_timestamp = System.currentTimeMillis() / 1000L;
    }


    //Popup message with your username if none found. Change it to your liking
    private void logic_for_username() {
        userID = SCUtils.getUniqueID(context);
        databaseRef.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (!dataSnapshot.exists()) {
                    show_alert_username();
                } else {
                    username = dataSnapshot.getValue(String.class);
                    databaseRef.child("users").child(userID).setValue(username);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("!!!", "username:onCancelled", databaseError.toException());
            }
        });
    }

    public void show_alert_username() {
        AlertDialog.Builder alertDialogUsername = new AlertDialog.Builder(mContext);
        alertDialogUsername.setMessage("Your username");
        final EditText input = new EditText(mContext);
        input.setText(username);
        alertDialogUsername.setView(input);

        alertDialogUsername.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                String new_username = input.getText().toString().trim();
                if ((!new_username.equals(username)) && (!username.equals("anonymous"))) {
                    process_new_message("Welcome to chat " + new_username, true);
                }
                username = new_username;
                databaseRef.child("users").child(userID).setValue(username);
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertDialogUsername.show();
    }


}
