package com.example.ugd1

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ugd1.room.Constant
import com.example.ugd1.room.MemberGym
import com.example.ugd1.room.MemberGymDB
import kotlinx.android.synthetic.main.activity_member_gym.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemberGymActivity : AppCompatActivity() {
    val db by lazy{ MemberGymDB(this) }
    lateinit var memberGymAdapter: MemberGymAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_gym)
        supportActionBar?.hide()
        setupListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        memberGymAdapter = MemberGymAdapter(arrayListOf(), object :
            MemberGymAdapter.OnAdapterListener {
            override fun onClick(memberGym : MemberGym) {
                Toast.makeText(applicationContext, memberGym.personalTrainer,
                    Toast.LENGTH_SHORT).show()
                intentEdit(memberGym.id, Constant.TYPE_READ)
            }
            override fun onUpdate(memberGym: MemberGym) {
                intentEdit(memberGym.id, Constant.TYPE_UPDATE)
            }
            override fun onDelete(memberGym : MemberGym) {
                deleteDialog(memberGym)
            }
        })
        list_gym.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = memberGymAdapter
        }
    }

    private fun deleteDialog(memberGym: MemberGym){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From ${memberGym.personalTrainer}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.MemberGymDao().deleteMemberGym(memberGym)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }
    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val membergym = db.MemberGymDao().getMemberGym()
            Log.d("MemberGymActivity","dbResponse: $membergym")
            withContext(Dispatchers.Main){
                memberGymAdapter.setData( membergym )
            }
        }
    }
    fun setupListener() {
        btnAdd.setOnClickListener{
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }
    fun intentEdit(memberGymId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, EditMemberGym::class.java)
                .putExtra("intent_id", memberGymId)
                .putExtra("intent_type", intentType)
        )
    }
}