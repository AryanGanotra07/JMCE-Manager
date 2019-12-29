package com.aryanganotra.jmcemanager.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aryanganotra.jmcemanager.FirebaseAuth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser!=null)
            startActivity(Intent(this@SplashScreen,MainActivity::class.java))
        else
            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
        finish()
    }
}