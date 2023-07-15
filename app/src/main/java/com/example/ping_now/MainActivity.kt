package com.example.ping_now

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.ping_now.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val fragmentArrList = ArrayList<Fragment>()
        fragmentArrList.add(ChatsFragment())
        fragmentArrList.add(StatusFragment())
        fragmentArrList.add(CallsFragment())

        val VpAdapter = ViewPagerAdapter(this@MainActivity,supportFragmentManager, fragmentArrList)
        binding!!.viewPgrId.adapter = VpAdapter
        binding!!.tabLayId.setupWithViewPager(binding!!.viewPgrId)



    }
}