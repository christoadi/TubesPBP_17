package com.example.ugd1

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Intent
import com.example.ugd1.databinding.ActivityRegisterBinding
import com.example.ugd1.room.User
import com.example.ugd1.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ugd1.databinding.ActivityMainBinding

class RegisterActivity: AppCompatActivity() {
    private lateinit var tilUsername: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilTanggalLahir: TextInputLayout
    private lateinit var tilNomorTelepon: TextInputLayout
    private lateinit var btnRegister: Button
    private lateinit var btnClear: Button
    private lateinit var registerLayout: ConstraintLayout
    private lateinit var btnDatePicker: Button
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val CHANNEL_ID_2 = "channel_notification_02"
    private val notificationId1 = 101

    private lateinit var binding: ActivityRegisterBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        val db by lazy{ UserDB( this) }
        val userDao = db.userDao()

        tilUsername = findViewById(R.id.etUsername)
        tilPassword = findViewById(R.id.etPassword)
        tilEmail = findViewById(R.id.etEmail)
        tilTanggalLahir = findViewById(R.id.etTanggalLahir)
        tilNomorTelepon = findViewById(R.id.etNomorTelepon)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val btnClear: Button = findViewById(R.id.btnClear)
        var checkLogin = true

        binding.btnRegister.setOnClickListener(View.OnClickListener {
            val mBundle = Bundle()
            val intent = Intent(this, MainActivity::class.java)

            val username: String = binding.etUsername.getEditText()?.getText().toString()
            val password: String = binding.etPassword.getEditText()?.getText().toString()
            val email: String = binding.etEmail.getEditText()?.getText().toString()
            val tanggalLahir: String = binding.etTanggalLahir.getEditText()?.getText().toString()
            val nomorTelepon: String = binding.etNomorTelepon.getEditText()?.getText().toString()

            if(username.isEmpty()){
                binding.etUsername.setError("Username masih Kosong")
                checkLogin = false
            }

            if(password.isEmpty()){
                binding.etPassword.setError("Password masih Kosong")
                checkLogin = false
            }

            if(email.isEmpty()) {
                binding.etEmail.setError("Email masih Kosong")
                checkLogin = false
            }

            if(tanggalLahir.isEmpty()){
                binding.etTanggalLahir.setError("Tanggal Lahir masih Kosong")
                checkLogin = false
            }

            if(nomorTelepon.isEmpty()){
                binding.etNomorTelepon.setError("Nomor Telepon masih Kosong")
                checkLogin = false
            }

            if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !tanggalLahir.isEmpty() && !nomorTelepon.isEmpty()){
                checkLogin = true
            }

            if(binding.etUsername.getEditText()?.getText()==null){
                binding.etUsername.getEditText()?.setText("")
            }

            if(binding.etPassword.getEditText()?.getText()==null){
                binding.etPassword.getEditText()?.setText("")
            }

            if(checkLogin == true){
                val moveRegister = Intent(this@RegisterActivity, MainActivity::class.java)
                mBundle.putString("username", binding.etUsername.editText?.text.toString())
                mBundle.putString("password", binding.etPassword.editText?.text.toString())
                mBundle.putString("email", binding.etEmail.editText?.text.toString())
                mBundle.putString("TanggalLahir", binding.etTanggalLahir.editText?.text.toString())
                mBundle.putString("NomorTelepon", binding.etNomorTelepon.editText?.text.toString())

                createNotificationChannel()

                sendNotification1()

                moveRegister.putExtra("register", mBundle)
                startActivity(moveRegister)
            }
            if(!checkLogin) return@OnClickListener

            val user = User(0, username, password, email, tanggalLahir, nomorTelepon)
            userDao.addUser(user)
        })

        btnDatePicker = findViewById(R.id.btnDatePicker)
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLable(myCalendar)
        }

        btnDatePicker.setOnClickListener{
            DatePickerDialog(this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnClear.setOnClickListener{
            binding.etUsername.editText?.setText("")
            binding.etPassword.editText?.setText("")
            binding.etEmail.editText?.setText("")
            binding.etTanggalLahir.editText?.setText("")
            binding.etNomorTelepon.editText?.setText("")

        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }


            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotification1() {
        val intent : Intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", "Register Succesful, Happy Gym "+binding?.inputUsername?.text.toString())
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val logo = BitmapFactory.decodeResource(resources, R.drawable.gym)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.gym)
            .setContentTitle("Register Succesful !!")
            .setContentText("Registered as "+binding?.inputUsername?.text.toString())
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setLargeIcon(logo)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigLargeIcon(null)
                    .bigPicture(logo)
            )
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Welcome", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId1, builder.build())
        }
    }


    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etTanggalLahir.editText?.setText(sdf.format(myCalendar.time))
    }

}