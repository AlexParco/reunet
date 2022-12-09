package com.reunet.app.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.reunet.app.ui.view.user.LoginActivity
import com.reunet.app.databinding.ActivitySplashBinding
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if(sharedPreferencesUtil.getToken().toString().isNotEmpty()){
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 2000)
    }
}