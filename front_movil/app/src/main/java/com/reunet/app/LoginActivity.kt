package com.reunet.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.reunet.app.databinding.ActivityLoginBinding
import com.reunet.app.models.User
import com.reunet.app.models.payload.request.LoginRequest
import com.reunet.app.models.payload.response.LoginResponse
import com.reunet.app.services.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
    private var USER: String = "USER"

    private val authService by lazy {
        AuthService.build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.loginBtn.setOnClickListener {
            val user = LoginRequest(
                email = binding.email.text.toString(),
                password = binding.password.text.toString()
            )
            Log.i("TEST_USER_FOM", "${user.email} - ${user.password}")
            try{
                login(user)
            }catch (e: Exception){
                Toast.makeText(this, "asdklasdj", Toast.LENGTH_SHORT).show()
            }
        }
        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login (user: LoginRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val call = authService.loginService(user)
            val loginResponse = call.body()
            if (call.isSuccessful){
                if(loginResponse != null) {
                    intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                    intent.putExtra(USER, loginResponse.data.user)
                    startActivity(intent)
                }
            }else{
                Log.i("TEST_LOGIN_MESSAGE", "ERROR LOGIN USER")
                throw Exception("error")
            }
        }
    }

}