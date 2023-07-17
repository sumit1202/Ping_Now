package com.example.ping_now

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ping_now.databinding.ReceivedMsgItemLayBinding
import com.example.ping_now.databinding.SentMsgItemLayBinding
import com.google.firebase.auth.FirebaseAuth

class RecyclerViewMessageAdapter(var context: Context, var list: ArrayList<MessageModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var MSG_ITEM_SENT = 1;
    private var MSG_ITEM_RECEIVED = 2;

    inner class SentViewHolder(view: View): RecyclerView.ViewHolder(view){

        var binding = SentMsgItemLayBinding.bind(view)
    }

    inner class ReceivedViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding = ReceivedMsgItemLayBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType == MSG_ITEM_SENT){
            SentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_msg_item_lay, parent, false))
        }
        else{
            ReceivedViewHolder(LayoutInflater.from(context).inflate(R.layout.received_msg_item_lay, parent, false))
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(FirebaseAuth.getInstance().uid == list[position].senderId) MSG_ITEM_SENT else MSG_ITEM_RECEIVED
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = list[position]

        if(holder.itemViewType == MSG_ITEM_SENT){
            val viewHolder = holder as SentViewHolder
            viewHolder.binding.sentMsgBubbleId.text = message.message

        }else{
            val viewHolder = holder as ReceivedViewHolder
            viewHolder.binding.receivedMsgBubbleId.text = message.message
        }
    }

}