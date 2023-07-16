package com.example.ping_now

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ping_now.databinding.ActivityOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        //creating alert dialog
        val builder = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle("OTP verification")
            .setMessage("Hang on...")

        dialog = builder.create()
        dialog.show()

        val phoneNumber = "+91"+intent.getStringExtra("phone_number")

        Log.d("Phone1202", ""+phoneNumber)

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setActivity(this@OTPActivity)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setCallbacks(object: OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Log.d("1onVerificationCompleted1202", "verification completed...")
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.d("1202:" , p0.toString())
                    dialog.dismiss()
                    Log.d("1onVerificationFailed1202", "verification completed...")
                    Toast.makeText(this@OTPActivity, "Verification failed, please try again...", Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    dialog.dismiss()
                    verificationId = p0
                }
            }).build()



        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.verifyBtnId.setOnClickListener {
            if(binding.enterOtpEdtTxtId.text!!.isEmpty()){
                Toast.makeText(this@OTPActivity, "Please enter your OTP...", Toast.LENGTH_LONG).show()
            }else{
                dialog.show()
                val credential = PhoneAuthProvider.getCredential(verificationId, binding.enterOtpEdtTxtId.text!!.toString())

                auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            dialog.dismiss()
                            Log.d("2isSuccessful1202", "Successful...")
                            Log.d("1202 verification OTP from firebase auth: ", verificationId)

                            Toast.makeText(this@OTPActivity, "Great, you're in...", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@OTPActivity,ProfileActivity::class.java))
                            finish()
                        }else{
                            Log.d("2isNotSuccessful1202", "Not Successful...")
                            dialog.dismiss()
                            Toast.makeText(this@OTPActivity, "Great, you're in...${it.exception}", Toast.LENGTH_LONG).show()


                        }
                    }


            }
        }


    }
}