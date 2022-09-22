package com.example.ugd1

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setTitle("User Menu")

        getSupportActionBar()?.hide()
        var bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)
        val home = HomeFragment()
        val data = DataFragment()
        val profile = ProfileFragment()

        setThatFragments(home)

        bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.navigation_home -> {
                    setThatFragments(home)
                }
                R.id.navigation_data -> {
                    setThatFragments(data)
                }
                R.id.navigation_profile -> {
                    setThatFragments(profile)
                }
            }
            true
        }



    }

    private fun setThatFragments(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.layoutFragment,fragment)
            commit()
        }
    }
}