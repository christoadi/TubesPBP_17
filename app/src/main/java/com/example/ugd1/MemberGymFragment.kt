package com.example.ugd1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment

class MemberGymFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member_gym, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAdd: Button = view.findViewById(R.id.btnAdd)


        btnAdd.setOnClickListener(View.OnClickListener {
            val moveMemberGym = Intent(this@MemberGymFragment.context, MemberGymActivity::class.java)
            startActivity(moveMemberGym)
        })
    }


}