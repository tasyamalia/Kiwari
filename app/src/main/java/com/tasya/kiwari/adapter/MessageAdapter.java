package com.tasya.kiwari.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tasya.kiwari.R;
import com.tasya.kiwari.model.Chats;

import org.w3c.dom.Text;

import java.util.Calendar;
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
        if (position == 0) {
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(chat.getDate());
        } else {
            String prev_date_time = mChat.get(position - 1).getDate();
            Log.d("INII :", prev_date_time);
            //String prev_date[] = prev_date_time[0].split("-");
            if (chat.getDate().equals(prev_date_time)) {
                holder.date.setVisibility(View.GONE);
            } else {
                holder.date.setVisibility(View.VISIBLE);
                holder.date.setText(chat.getDate());
            }
        }

        holder.show_message.setText(chat.getMessage());
        holder.time.setText(chat.getTime());

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
        public TextView time;
        public TextView date;
        public ImageView avatar;
        public ViewHolder(View itemView){
            super(itemView);
            show_message= itemView.findViewById(R.id.show_message);
            avatar= itemView.findViewById(R.id.avatar);
            time= itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);
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
