package com.example.pizzaapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //hide title bar
        supportActionBar?.hide()

        //instance text
        val txtUsername:EditText = findViewById(R.id.editTextEmail)
        val txtPassword:EditText = findViewById(R.id.editTextPassword)
        //instance button login
        val btnLogin:Button = findViewById(R.id.buttonLogin)
        val registerTv:TextView = findViewById(R.id.registerBtn)

        //event button login
        btnLogin.setOnClickListener {
            val databaseHelper = DatabaseHelper(this)

            val email = txtUsername.text.toString().trim()
            val password = txtPassword.text.toString().trim()
            val data: String = databaseHelper.checkData("stevi.ema@amikom.ac.id")
            //check login
            val result: Boolean = databaseHelper.checkLogin(email, password)
            if (result == true) {
                Toast.makeText(
                    this@LoginActivity, "Login Success",
                    Toast.LENGTH_SHORT
                ).show()
                val intentLogin = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intentLogin)
            } else {
                Toast.makeText(
                    this@LoginActivity, "Login Failed, Try Again !!",
                    Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(this@LoginActivity, "Result : $data",
                Toast.LENGTH_SHORT).show()

        }
        registerTv.setOnClickListener{
            val intentRegister = Intent (this@LoginActivity, RegisterActivity::class.java)
            startActivity(intentRegister)
        }
    }

}