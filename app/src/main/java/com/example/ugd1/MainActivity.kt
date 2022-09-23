package com.example.ugd1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isEmpty
import com.example.ugd1.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    lateinit var mBundle: Bundle
    var tempUsername: String = "admin"
    var tempPass: String = "admin"

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setTitle("User Login")

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnClear: Button = findViewById(R.id.btnClear)

        if(intent.getBundleExtra("register")!=null){
            mBundle = intent.getBundleExtra("register")!!
            tempUsername = mBundle!!.getString("username")!!
            tempPass = mBundle!!.getString("password")!!
            println(tempUsername)
            inputUsername.editText?.setText(tempUsername)
            inputPassword.editText?.setText(tempPass)
        }
        btnRegister.setOnClickListener {
            val moveRegister = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(moveRegister)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text!")
                checkLogin = false
            }
            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text!")
                checkLogin = false
            }

            if(username == "admin" && password == "admin" || (username == tempUsername && password == tempPass)) {
                checkLogin = true
            }
            else if((username != "admin" || password != "admin") || (username != tempUsername && password != tempPass)){
                checkLogin = false
                Snackbar.make(mainLayout, "Username atau Password salah!", Snackbar.LENGTH_LONG).show()
            }

            val db by lazy{ UserDB(this) }
            val userDao = db.userDao()

            val user = userDao.checkUser(username, password)
            if(user!= null){
                sharedPreferences.edit()
                    .putInt("id", user.id)
                    .apply()

                checkLogin = true
            }
            if(!checkLogin) return@OnClickListener

            val moveHome = Intent(this@MainActivity, HomeActivity::class.java)

            startActivity(moveHome)
        })



        btnClear.setOnClickListener{
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty() && password.isEmpty()){
                Snackbar.make(mainLayout, "Field Masih Kosong", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            inputUsername.editText?.setText("")
            inputPassword.editText?.setText("")

            Snackbar.make(mainLayout, "Success Clear Field", Snackbar.LENGTH_LONG).show()
        }

    }
}