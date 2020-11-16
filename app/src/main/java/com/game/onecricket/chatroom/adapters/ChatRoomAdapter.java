package com.game.onecricket.chatroom.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.game.onecricket.R;
import com.game.onecricket.chatroom.models.Message;
import com.game.onecricket.chatroom.utils.SCUtils;

import java.util.ArrayList;


public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MessagesViewHolder> {
  private ArrayList<Message> data;
  private Context mContext;

  public ChatRoomAdapter(Context context, ArrayList<Message> data) {
    this.data = data;
    this.mContext = context;
  }

  @Override
  public MessagesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_main, null);

    return new MessagesViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MessagesViewHolder messagesViewHolder, int i) {
    Message message        = data.get(i);
    String formatted_date = SCUtils.formatted_date(message.getTimestamp());
    if (message.isNotification()) {
      messagesViewHolder.textViewMessage.setText(Html.fromHtml("<small><i><font color=\"#FFBB33\">" + " " + message.getMessage() + "</font></i></small>"));
    } else {
      messagesViewHolder.textViewMessage.setText(
              Html.fromHtml("<font color=\"#403835\">" + "<b>"+message.getUsername()+"</b>" + "</font>" + " " +
                                   "<font color=\"#000000\">" +"&nbsp;&nbsp;"+ message.getMessage() + "</font>"));

    }
  }

  @Override
  public int getItemCount() {
    return (null != data ? data.size() : 0);
  }

  public static class MessagesViewHolder extends RecyclerView.ViewHolder {
    protected TextView textViewMessage;

    public MessagesViewHolder(View view) {
      super(view);
      this.textViewMessage = (TextView) view.findViewById(R.id.message);
    }
  }
}