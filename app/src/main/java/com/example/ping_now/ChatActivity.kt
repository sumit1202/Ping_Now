package com.example.ping_now

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ping_now.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var realtimeDatabase: FirebaseDatabase

    private lateinit var senderUid: String
    private lateinit var receiverUid: String
    private lateinit var senderRoomUid: String
    private lateinit var receiverRoomUid: String

    private lateinit var list: ArrayList<MessageModel>
    private lateinit var list1: ArrayList<UserModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        senderUid = FirebaseAuth.getInstance().uid.toString()
        receiverUid = intent.getStringExtra("uid")!!
        senderRoomUid = senderUid + receiverUid
        receiverRoomUid = receiverUid + senderUid

        list = ArrayList()
        list1 = ArrayList()

        realtimeDatabase = FirebaseDatabase.getInstance()

        binding.sendBtnId.setOnClickListener {
            if (binding.msgBoxId.text.isNotEmpty()) {
                val message = MessageModel(binding.msgBoxId.text.toString(), senderUid, Date().time)
                val randomKey = realtimeDatabase.reference.push().key
                realtimeDatabase.reference.child("chats").child(senderRoomUid).child("message")
                    .child(randomKey!!).setValue(message).addOnSuccessListener {

                        realtimeDatabase.reference.child("chats").child(receiverRoomUid)
                            .child("message").child(randomKey).setValue(message)
                            .addOnSuccessListener {

                                binding.msgBoxId.text = null
                                Toast.makeText(this, "Message sent", Toast.LENGTH_LONG).show()

                            }

                    }


            }
        }

        realtimeDatabase.reference.child("chats").child(senderRoomUid).child("message")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (snapshot1 in snapshot.children) {
                        val data = snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)

                        binding.chatRecyclerViewMsgListId.adapter =
                            RecyclerViewMessageAdapter(this@ChatActivity, list)

                    }
                    val receiverName = intent.getStringExtra("receiverName")
                    binding.msgBoxId.hint = "Ping " + receiverName + " .....✍( ▀̿ ̿ ‿ ▀̿ ̿  )"
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, "Error: $error", Toast.LENGTH_SHORT).show()
                }

            })

    }
}