package com.example.ugd1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.ugd1.databinding.ActivityEditMemberGymBinding
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
    private var binding: ActivityEditMemberGymBinding? = null
    private val MEMBER_GYM_1 = "notif_member_gym_1"
    private val notificationId = 1

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


    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(MEMBER_GYM_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }


            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotification2() {
//        val intent : Intent = Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
//        broadcastIntent.putExtra("toastMessage", "Register Succesful, Happy Gym "+binding?.inputUsername?.text.toString())
//        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val logo = BitmapFactory.decodeResource(resources, R.drawable.gym)
        val builder = NotificationCompat.Builder(this, MEMBER_GYM_1)
            .setSmallIcon(R.drawable.gym)
            .setContentTitle("Edit Member Gym")
            .setContentText("Success Edit as "+ binding?.etMembership?.text.toString())
//            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setLargeIcon(logo)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigLargeIcon(null)
                    .bigPicture(logo)
            )
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
//            .setContentIntent(pendingIntent)
//            .addAction(R.mipmap.ic_launcher, "Welcome", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    private fun sendNotification1() {
//        val intent : Intent = Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
//        broadcastIntent.putExtra("toastMessage", "Register Succesful, Happy Gym "+binding?.inputUsername?.text.toString())
//        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val logo = BitmapFactory.decodeResource(resources, R.drawable.gym)
        val builder = NotificationCompat.Builder(this, MEMBER_GYM_1)
            .setSmallIcon(R.drawable.gym)
            .setContentTitle("Add Member Gym")
            .setContentText("Success Add as "+ binding?.etMembership?.text.toString())
//            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setLargeIcon(logo)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigLargeIcon(null)
                    .bigPicture(logo)
            )
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
//            .setContentIntent(pendingIntent)
//            .addAction(R.mipmap.ic_launcher, "Welcome", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
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
                createNotificationChannel()
                sendNotification1()
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
                createNotificationChannel()
                sendNotification2()
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