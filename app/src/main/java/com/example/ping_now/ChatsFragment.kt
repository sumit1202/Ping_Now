package com.example.ping_now

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ping_now.databinding.FragmentChatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var realTimeDatabase: FirebaseDatabase
    private lateinit var userArrList: ArrayList<UserModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatsBinding.inflate(layoutInflater)

        realTimeDatabase = FirebaseDatabase.getInstance()
        userArrList = ArrayList()

        realTimeDatabase.reference.child("users")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userArrList.clear()

                    for(snapshot1 in snapshot.children){
                        val user = snapshot1.getValue(UserModel::class.java)
                        if(user!!.uid!=FirebaseAuth.getInstance().uid){
                            userArrList.add(user)
                        }
                    }

                    binding.chatRecyclerViewListId.adapter = RecyclerViewChatAdapter(requireContext(),userArrList)



                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        return binding.root

    }
}