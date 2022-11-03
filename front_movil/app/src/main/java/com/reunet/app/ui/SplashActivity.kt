package com.reunet.app.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.reunet.app.ui.user.auth.LoginActivity
import com.reunet.app.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}