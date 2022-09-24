package com.example.ugd1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.ugd1.memberGym.MemberGymActivity

class MemberGymFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_member_gym, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAddPeminjam: Button = view.findViewById(R.id.btnAdd)


        btnAddPeminjam.setOnClickListener(View.OnClickListener {
            val movePeminjam = Intent(this@MemberGymFragment.context, MemberGymActivity::class.java)
            startActivity(movePeminjam)
        })
    }


}