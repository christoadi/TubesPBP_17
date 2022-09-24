package com.example.ugd1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ugd1.room.Constant
import com.example.ugd1.room.MemberGym
import com.example.ugd1.room.MemberGymDB
import kotlinx.android.synthetic.main.activity_edit_member_gym.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditMemberGym : AppCompatActivity() {
    val db by lazy { MemberGymDB(this) }
    private var memberGymId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_member_gym)
        supportActionBar?.hide()
        setupView()
        setupListener()
    }

    fun setupView() {
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE-> {
                btnUpdate.visibility = View.GONE
            }
            Constant.TYPE_READ-> {
                btnSave.visibility = View.GONE
                btnUpdate.visibility = View.GONE
                getMemberGym()
            }
            Constant.TYPE_UPDATE-> {
                btnSave.visibility = View.GONE
                getMemberGym()
            }
        }
    }

    private fun setupListener() {
        btnSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.MemberGymDao().addMemberGym(
                    MemberGym(
                        0, etPersonalTrainer.text.toString(),
                        etMembership.text.toString(),
                        etTanggal.text.toString(),
                        etDurasi.text.toString()
                    )
                )
                finish()
            }
        }
        btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.MemberGymDao().updateMemberGym(
                    MemberGym(memberGymId,
                        etPersonalTrainer.text.toString(),
                        etMembership.text.toString(),
                        etTanggal.text.toString(),
                        etDurasi.text.toString())
                )
                finish()
            }
        }
    }
    fun getMemberGym(){
        memberGymId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val memberGym = db.MemberGymDao().getMemberGym(memberGymId)[0]
            etPersonalTrainer.setText(memberGym.personalTrainer)
            etMembership.setText(memberGym.membership)
            etTanggal.setText(memberGym.tanggal)
            etDurasi.setText(memberGym.durasi)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}