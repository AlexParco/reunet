package com.reunet.app.ui.user.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.reunet.app.ui.BaseActivity
import com.reunet.app.databinding.ActivityLoginBinding
import com.reunet.app.models.payload.request.LoginRequest
import com.reunet.app.services.AuthService
import com.reunet.app.storage.SharedPreferenceUtil
import com.reunet.app.ui.MainActivity
import com.reunet.app.ui.user.account.EditProfileActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    private var USER: String = "USER"
    private var TOKEN: String = "TOKEN"

    private val authService by lazy {
        AuthService.build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferenceUtil = SharedPreferenceUtil().also{
            it.setSharedPreference(this)
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val user = LoginRequest(
                email,
                password
            )
            try{
                if(validateData(email, password)){
                    login(user)
                }
            }catch (e: Exception){
                Log.i("ERROR_LOGIN", e.message.toString())
            }
        }
        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login (user: LoginRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val call = authService.loginService(user)
            if (call.isSuccessful){
                val loginResponse = call.body()
                if(loginResponse != null) {
                    intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra(USER, loginResponse.data.user)
                    intent.putExtra(TOKEN, loginResponse.data.token)
                    startActivity(intent)
                }
            }else if(call.code().equals(400)){
                runOnUiThread{
                    Toast.makeText(this@LoginActivity, "User with this email doesn't exists", Toast.LENGTH_SHORT).show()
                }
            }else if(call.code().equals(500)){
                runOnUiThread{
                    Toast.makeText(this@LoginActivity, "Bad credentials", Toast.LENGTH_SHORT).show()
                }
            }else{
                Log.i("TEST_LOGIN_MESSAGE", "ERROR LOGIN USER")
                throw Exception("error")
            }
        }
    }

    private fun validateData (email: String, password: String):Boolean{
        var validate = false
        if(email.isEmpty() || password.isEmpty()){
            if(password.isEmpty()){
                binding.password.error = "Password is required"
                binding.password.requestFocus()
            }
            if(email.isEmpty()){
                binding.email.error = "Email is required"
                binding.email.requestFocus()
            }
        }else{
            validate = true
        }

        return validate
    }
}