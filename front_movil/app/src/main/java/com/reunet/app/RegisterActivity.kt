package com.reunet.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.reunet.app.databinding.ActivityRegisterBinding
import com.reunet.app.models.payload.request.RegisterRequest
import com.reunet.app.services.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(ActivityRegisterBinding::inflate) {

    private val authService by lazy {
        AuthService.build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.registerBtn.setOnClickListener {
            val user = RegisterRequest(
                binding.firstname.text.toString(),
                binding.lastname.text.toString(),
                binding.email.text.toString(),
                "ROLE_USER",
                binding.password.text.toString(),
            )
            try {
                register(user)
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun register(user: RegisterRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val call = authService.registerService(user)
            val loginResponse = call.body()
            if (call.isSuccessful){
                if(loginResponse != null) {
                    Log.i("TEST_REGISTER_MESSAGE", "REGISTERED USER")
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                }
            }else {
                throw Exception("credentials are invalid")
            }
        }
    }
}