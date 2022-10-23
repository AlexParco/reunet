package ayoria.chagua.reunetapp.auth

import android.content.Intent
import android.content.SharedPreferences
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import ayoria.chagua.reunetapp.BaseActivity
import ayoria.chagua.reunetapp.MainActivity
import ayoria.chagua.reunetapp.R
import ayoria.chagua.reunetapp.api.RetrofitClient
import ayoria.chagua.reunetapp.databinding.ActivitySignUpBinding
import ayoria.chagua.reunetapp.models.RegisterResponse
import ayoria.chagua.reunetapp.models.User
import ayoria.chagua.reunetapp.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate), AdapterView.OnItemSelectedListener {
    private lateinit var myAdapterRole: ArrayAdapter<String>
    private lateinit var sharedPreferenceManager: SharedPrefManager
    var role = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //adapter for Spinner
        myAdapterRole = ArrayAdapter<String>(this, R.layout.item_spinner)
        myAdapterRole.addAll(Arrays.asList("Role", "Published", "Group Leader", "Group member", "Common"))
        binding.spinnerRole.onItemSelectedListener = this
        binding.spinnerRole.adapter = myAdapterRole

        //get the sharedPreference
        sharedPreferenceManager = SharedPrefManager().also {
            it.setSharedPreference(this)
        }


        //Go to login
        binding.goSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }



        binding.signUpBtn.setOnClickListener {
            //get values
            val firstName = binding.firstname.text.toString().trim()
            val lastName = binding.lastname.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val passwordConfirm = binding.passwordConfirm.text.toString().trim()

            val intent = Intent(this, MainActivity::class.java)
            if(validateUserData(firstName, lastName, email, password, passwordConfirm)){
                Toast.makeText(this, "Enviando", Toast.LENGTH_SHORT).show()
                //with sharedPreference
                val user = User(email, password, firstName, lastName, role)
                sharedPreferenceManager.saveUser(user)


                //With we api
                RetrofitClient.instance.registerUser(firstName, lastName, password, email, this.role)
                    .enqueue(object: Callback<RegisterResponse>{
                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, "Error with failure"  + t.message, Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(
                            call: Call<RegisterResponse>,
                            response: Response<RegisterResponse>
                        ) {
                            Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }
                    })
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val roleSelected = myAdapterRole.getItem(position)
        role = roleSelected.toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        role = myAdapterRole.getItem(4).toString()
    }



    fun validateUserData(firstName: String, lastName: String, email: String, password: String, passwordConfirm: String):Boolean {
        var validate = false

        if(firstName.isEmpty()){
            binding.firstname.error = "First name is required"
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
            binding.passwordConfirm.error = "Repeat the password confirmation please"
            binding.passwordConfirm.requestFocus()
        }
        if(!firstName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !passwordConfirm.isEmpty()){
            if(!password.equals(passwordConfirm)){
                binding.passwordConfirm.error = "Make sure the passwords match"
                binding.password.error = "Make sure the passwords match"
                binding.passwordConfirm.requestFocus()
            }else{
                validate = true
            }
        }
        return validate
    }
}