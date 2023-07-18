package com.example.ping_now

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.ping_now.databinding.ChatItemRowBinding

class RecyclerViewChatAdapter(var context: Context, var list: ArrayList<UserModel>): RecyclerView.Adapter<RecyclerViewChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var binding: ChatItemRowBinding = ChatItemRowBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_item_row, parent, false))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        var user = list[position]
        Glide.with(context).load(user.imgUrl).into(holder.binding.profileThumbnailId)
        holder.binding.chatRowUsernameId.text = user.name

        //on chat item click open to chat activity
        holder.itemView.setOnClickListener {
            val intent = Intent( context, ChatActivity::class.java)
            intent.putExtra("uid", user.uid)
            //////
            intent.putExtra("receiverName", user.name)
            context.startActivity(intent)

        }

    }
}