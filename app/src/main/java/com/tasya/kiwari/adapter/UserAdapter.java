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
     public UserAdapter(Context mContext, List<Users> mUsers){
         this.mContext = mContext;
         this.mUsers = mUsers;
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
            holder.avatar.setImageResource(R.drawable.ic_face);
        }else{
            Glide.with(mContext)
                    .load(users.getAvatar())
                    .into(holder.avatar);
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
         public ViewHolder(View itemView){
             super(itemView);
             name= itemView.findViewById(R.id.name);
             avatar= itemView.findViewById(R.id.avatar);
         }
    }



}
