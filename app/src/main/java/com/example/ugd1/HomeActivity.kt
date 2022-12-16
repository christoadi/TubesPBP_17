package com.example.ugd1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        setTitle("User Menu")

        getSupportActionBar()?.hide()
//        var bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)
        val home = HomeFragment()
        val loc = LocationFragment()
        val memberGym = MemberGymFragment()
        val pesanan = PesananFragment()
        val profile = ProfilFragment()

        setThatFragments(home)

        nav_home.setOnItemSelectedListener{
            when(it){
                R.id.navigation_home -> {
                    setThatFragments(home)
                }
                R.id.navigation_location -> {
                    setThatFragments(loc)
                }
                R.id.navigation_memberGym -> {
                    setThatFragments(memberGym)
                }
                R.id.navigation_pesanan -> {
                    setThatFragments(pesanan)
                }
                R.id.navigation_profile -> {
                    setThatFragments(profile)
                }
            }
            true
        }

//        nav_home.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
//            override fun onTabSelected(
//                lastIndex: Int,
//                lastTab: AnimatedBottomBar.Tab?,
//                newIndex: Int,
//                newTab: AnimatedBottomBar.Tab
//            ) {
//
//                //redirecting fragments
//                when(newIndex){
//                    0 -> setThatFragments(home);
//                    1 -> setThatFragments(loc);
//                    2 -> setThatFragments(memberGym);
//                    3 -> setThatFragments(profile);
//                }
//                Log.d("bottom_bar", "Selected index: $newIndex, title: ${newTab.title}")
//            }
//
//            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
//                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
//            }
//        });
    }




    private fun setThatFragments(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.layoutFragment,fragment)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.nav_home,menu)
        return true
    }

    fun getSharedPreferences(): SharedPreferences{
        return sharedPreferences
    }
}