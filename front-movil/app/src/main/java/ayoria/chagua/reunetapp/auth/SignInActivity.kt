package ayoria.chagua.reunetapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ayoria.chagua.reunetapp.MainActivity
import ayoria.chagua.reunetapp.api.RetrofitClient
import ayoria.chagua.reunetapp.databinding.ActivityMainBinding
import ayoria.chagua.reunetapp.databinding.ActivitySignInBinding
import ayoria.chagua.reunetapp.models.LoginResponse
import ayoria.chagua.reunetapp.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val intent = Intent(this, MainActivity::class.java)

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
                        Toast.makeText(applicationContext, "huvo un error onfailure" + t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if((response.body()?.user)!!.id > 0){
                            SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.user!!)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        }else{
                            Toast.makeText(applicationContext, "Hubo un error onResponse" + response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                        }
                    }



                })
        }
    }

    override fun onStart() {
        super.onStart()
        if(SharedPrefManager.getInstance(this).isLoggedIn){
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}