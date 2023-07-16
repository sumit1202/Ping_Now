package com.example.ping_now

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ping_now.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        //if user already signed in
        if(auth.currentUser != null){
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            finish()
        }

        //else for user NOT signed in
        binding.signInBtnId.setOnClickListener {
            if(binding.enterPhoneEdtTxtId.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your phone number...",Toast.LENGTH_LONG).show()
            }else{
                var intent = Intent(this, OTPActivity::class.java)
                intent.putExtra("phone_number", binding.enterPhoneEdtTxtId.text!!.toString())
                startActivity(intent)
            }
        }




    }
}