package com.example.ping_now

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ping_now.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var realTimeDatabase: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedProfilePic: Uri
    private lateinit var dialog: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = AlertDialog.Builder(this).setTitle("Profile Setup").setMessage("Hang on...")
            .setCancelable(false)

        realTimeDatabase = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        //edit profile pic code
        binding.profilePicId.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        //setup button
        binding.setupBtnId.setOnClickListener {
            if (binding.enterNameEdtTxtId.text.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_LONG).show()
            //} else if (selectedProfilePic == null) {
                //Toast.makeText(this, "Please set your profile picture", Toast.LENGTH_LONG).show()
            } else {
                storeDataOnStorage()
            }
        }

    }

    private fun storeDataOnStorage() {
        //upload user profile pic
        val reference = storage.reference.child("profiles").child(Date().time.toString())
        reference.putFile(selectedProfilePic).addOnCompleteListener {
            if (it.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfoOnRealtimeDb(task.toString()) //upload other user info
                }
            }
        }
    }

    private fun uploadInfoOnRealtimeDb(imgUrl: String) {
        val user = UserModel(auth.uid.toString(), binding.enterNameEdtTxtId.text.toString(), auth.currentUser!!.phoneNumber.toString(), imgUrl)
        realTimeDatabase.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "You are all set to ping now :)", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                selectedProfilePic = data.data!!
                binding.profilePicId.setImageURI(selectedProfilePic)

            }
        }
    }
}