package ayoria.chagua.reunetapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ayoria.chagua.reunetapp.BaseActivity
import ayoria.chagua.reunetapp.MainActivity
import ayoria.chagua.reunetapp.api.RetrofitClient
import ayoria.chagua.reunetapp.databinding.ActivityMainBinding
import ayoria.chagua.reunetapp.databinding.ActivitySignInBinding
import ayoria.chagua.reunetapp.databinding.ActivitySignUpBinding
import ayoria.chagua.reunetapp.models.LoginResponse
import ayoria.chagua.reunetapp.models.User
import ayoria.chagua.reunetapp.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SignInActivity : BaseActivity<ActivitySignInBinding>(ActivitySignInBinding::inflate) {

    private lateinit var sharedPreference: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreference = SharedPrefManager().also{
            it.setSharedPreference(this)
        }

        binding.goSignUp.setOnClickListener {
            val intentUp = Intent(this, SignUpActivity::class.java)

            startActivity(intentUp)
        }

        binding.signInBtn.setOnClickListener{
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            if(email.isEmpty()){
                binding.email.error = "Email is required"
                binding.email.requestFocus()
            }
            if(password.isEmpty()){
                binding.password.error = "Password is required"
                binding.password.requestFocus()
            }
            RetrofitClient.instance.loginUser(email, password)
                .enqueue(object : Callback<LoginResponse>{

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "huvo un error en la funcion onfailure " + t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        startLoginPref(email, password)
                    }
                })
        }
    }

    fun startLoginPref(email: String, password: String){
        val user: User? = sharedPreference.getUser()
        if(email == user?.email && password == user.password){
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            Toast.makeText(this, "Error with email or password", Toast.LENGTH_LONG).show()
        }
    }
}