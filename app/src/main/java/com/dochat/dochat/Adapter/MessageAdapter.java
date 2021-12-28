package com.dochat.dochat.Adapter;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dochat.dochat.R;
import com.dochat.dochat.databinding.ItemReceiveBinding;
import com.dochat.dochat.databinding.ItemSendBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<Message>messages;

    final int ITEM_SENT =1;
    final int ITEM_RECEIVE =2;

    String senderRoom;
    String receiverRoom;

    FirebaseRemoteConfig remoteConfig;
    public MessageAdapter(Context context, ArrayList<Message> messages)
    {
        this.context = context;
        this.messages  = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType ==ITEM_SENT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.item_send,parent,false);
            return new  SendViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive,parent,false);
            return new ReceiveViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      Message message = messages.get(position);
     if(holder.getClass() == SendViewHolder.class)
     {
         SendViewHolder viewHolder = (SendViewHolder) holder;
         viewHolder.binding.message.setText(message.getMessage());
     }
     else {
         ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
         viewHolder.binding.message.setText(message.getMessage());
     }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SendViewHolder extends RecyclerView.ViewHolder{

        ItemSendBinding binding;
        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSendBinding.bind(itemView);
        }
    }

    public  class ReceiveViewHolder extends RecyclerView.ViewHolder{

        ItemReceiveBinding binding;
        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveBinding.bind(itemView);
        }
    }
}
