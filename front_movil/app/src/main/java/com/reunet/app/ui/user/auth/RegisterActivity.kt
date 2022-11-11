package com.reunet.app.ui.user.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.reunet.app.ui.BaseActivity
import com.reunet.app.databinding.ActivityRegisterBinding
import com.reunet.app.models.payload.request.RegisterRequest
import com.reunet.app.services.AuthService
import com.reunet.app.storage.SharedPreferenceUtil
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

            val firstname = binding.firstname.text.toString()
            val lastname = binding.lastname.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            val passwordconfirm = binding.passwordConfirm.text.toString()

            val user = RegisterRequest(
                firstname,
                lastname,
                email,
                "ROLE_USER",
                password,
            )

            try {
                if(validateData(firstname, lastname, email, password, passwordconfirm)){
                    register(user)
                }
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
            if (call.isSuccessful){
                val loginResponse = call.body()
                if(loginResponse != null) {
                    runOnUiThread{
                        Toast.makeText(this@RegisterActivity, loginResponse.message, Toast.LENGTH_SHORT).show()
                    }
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                }
            }else if(call.code().equals(400)){
                runOnUiThread{
                    Toast.makeText(this@RegisterActivity, "Email is alredy taken", Toast.LENGTH_SHORT).show()
                }
            }else {
                throw Exception("credentials are invalid")
            }
        }
    }

    private fun validateData(firstName: String, lastName: String, email: String, password: String, passwordConfirm:String) :Boolean{
        var validate = false

        if(firstName.isEmpty()){
            binding.firstname.error = "First name is required"
            binding.firstname.requestFocus()
        }else if(firstName.length < 2){
            binding.firstname.error = "Make sure the name is greater than one character"
            binding.firstname.requestFocus()
        }

        if(lastName.isEmpty()){
            binding.firstname.error = "First name is required"
            binding.firstname.requestFocus()
        }

        if(email.isEmpty()){
            binding.email.error = "Email is required"
            binding.email.requestFocus()
        }

        if(password.isEmpty()){
            binding.password.error = "Password is required"
            binding.password.requestFocus()
        }

        if (passwordConfirm.isEmpty()){
            binding.passwordConfirm.error = "The password confirmation is required"
            binding.passwordConfirm.requestFocus()
        }

        if(!password.isEmpty() && !passwordConfirm.isEmpty() && password.equals(passwordConfirm)){
            if(password.length < 5){
                binding.password.error = "The password needs to be longer"
                binding.password.requestFocus()
            }else{
                validate = true
            }
        }else if(!password.equals(passwordConfirm)){
            binding.passwordConfirm.error = "Make sure the passwords match"
            binding.password.error = "Make sure the passwords match"
            binding.passwordConfirm.requestFocus()
        }
        return validate
    }
}