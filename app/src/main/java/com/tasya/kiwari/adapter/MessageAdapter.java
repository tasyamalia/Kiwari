package com.tasya.kiwari.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tasya.kiwari.R;
import com.tasya.kiwari.model.Chats;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    FirebaseUser firebaseUser;
    private Context mContext;
    private List<Chats> mChat;
    private String imageurl;
    public MessageAdapter(Context mContext, List<Chats> mChat, String imageurl){
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageurl = imageurl;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chart_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chats chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());
        Log.d(chat.getMessage().toString(), "TEST: ");
        if(imageurl.equals("default")){
            holder.avatar.setImageResource(R.drawable.ic_face);
        }else{
            Glide.with(mContext)
                    .load(imageurl)
                    .into(holder.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView avatar;
        public ViewHolder(View itemView){
            super(itemView);
            show_message= itemView.findViewById(R.id.show_message);
            avatar= itemView.findViewById(R.id.avatar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
