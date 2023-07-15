package com.example.ping_now

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.ping_now.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val fragmentArrList = ArrayList<Fragment>()
        fragmentArrList.add(ChatsFragment())
        fragmentArrList.add(StatusFragment())
        fragmentArrList.add(CallsFragment())


        //user not logged in
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        val vpAdapter = ViewPagerAdapter(this@MainActivity,supportFragmentManager, fragmentArrList)
        binding!!.viewPgrId.adapter = vpAdapter
        binding!!.tabLayId.setupWithViewPager(binding!!.viewPgrId)



    }
}