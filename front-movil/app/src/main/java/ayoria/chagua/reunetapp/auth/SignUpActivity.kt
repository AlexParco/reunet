package ayoria.chagua.reunetapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ayoria.chagua.reunetapp.MainActivity
import ayoria.chagua.reunetapp.api.RetrofitClient
import ayoria.chagua.reunetapp.databinding.ActivitySignUpBinding
import ayoria.chagua.reunetapp.models.DefaultResponseRegister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.signUpBtn.setOnClickListener {
            val firstName = binding.firstname.text.toString().trim()
            val lastName = binding.lastname.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val role = binding.role.text.toString().trim()

            val intent = Intent(this, MainActivity::class.java)

            if(firstName.isEmpty()){
                binding.firstname.error = "First name is required"
                binding.firstname.requestFocus()
            }
            if(lastName.isEmpty()){
                binding.lastname.error = "Last name is required"
                binding.lastname.requestFocus()
            }
            if(email.isEmpty()){
                binding.email.error = "Email is required"
                binding.email.requestFocus()
            }
            if(password.isEmpty()){
                binding.password.error = "Password is required"
                binding.password.requestFocus()
            }
            if(role.isEmpty()){
                binding.role.error = "Role is required"
                binding.role.requestFocus()
            }
            RetrofitClient.instance.registerUser(firstName, lastName, password, email, role)
                .enqueue(object: Callback<DefaultResponseRegister>{
                    override fun onFailure(call: Call<DefaultResponseRegister>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error in onFailure" + t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<DefaultResponseRegister>,
                        response: Response<DefaultResponseRegister>
                    ) {
                        Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }


                })
        }


    }


}