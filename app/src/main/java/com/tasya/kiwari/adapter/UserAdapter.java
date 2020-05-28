package com.tasya.kiwari.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.tasya.kiwari.MainActivity;
import com.tasya.kiwari.MessageActivity;
import com.tasya.kiwari.R;
import com.tasya.kiwari.model.Chats;
import com.tasya.kiwari.model.Users;

import org.w3c.dom.Text;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<Users> mUsers;
    private boolean ischat;
    String theLastMessage;
    String theLastTime;
     public UserAdapter(Context mContext, List<Users> mUsers, boolean ischat){
         this.mContext = mContext;
         this.mUsers = mUsers;
         this.ischat = ischat;
     }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users users = mUsers.get(position);
        holder.name.setText(users.getName());
        if(users.getAvatar().equals("default")){
            holder.avatar.setImageResource(R.drawable.icon_nopic);
        }else{
            Glide.with(mContext)
                    .load(users.getAvatar())
                    .into(holder.avatar);
        }
        if(ischat){
            lastMessage(users.getId(),holder.last_msg, holder.lastTime);
        }else{
            holder.last_msg.setVisibility(View.GONE);
            holder.lastTime.setVisibility(View.GONE);
        }

        if(ischat){
            if(users.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }else{
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else{
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", users.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
         public TextView name;
         public ImageView avatar;
         private ImageView img_on;
         private ImageView img_off;
         private TextView last_msg;
         private TextView lastTime;
         public ViewHolder(View itemView){
             super(itemView);
             name= itemView.findViewById(R.id.name);
             avatar= itemView.findViewById(R.id.avatar);
             img_off = itemView.findViewById(R.id.img_off);
             img_on = itemView.findViewById(R.id.img_on);
             last_msg = itemView.findViewById(R.id.last_msg);
             lastTime = itemView.findViewById(R.id.lastTime);
         }
    }
    private void lastMessage(final String userid, final TextView last_msg, final TextView lastTime){
         theLastMessage = "default";
         theLastTime = "default";
         final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
         DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

         if(firebaseUser != null ){

             reference.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                         Chats chat = snapshot.getValue(Chats.class);

                         if(chat.getReceiver().equals(firebaseUser.getUid())&& chat.getSender().equals(userid)||
                                 chat.getReceiver().equals(userid)&&chat.getSender().equals(firebaseUser.getUid())){
                             theLastMessage = chat.getMessage();
                             theLastTime = chat.getTime();
                         }
                     }
                     switch (theLastMessage){
                         case "default":
                             last_msg.setText("No message");
                             lastTime.setText("");
                             break;
                         default:
                             last_msg.setText(theLastMessage);
                             lastTime.setText(theLastTime);
                             break;
                     }
                     theLastMessage = "default";
                     theLastTime = "default";
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });

         }


    }



}
