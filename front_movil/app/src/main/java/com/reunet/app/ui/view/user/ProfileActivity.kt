package com.reunet.app.ui.view.user

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.reunet.app.databinding.ActivityProfileBinding
import com.reunet.app.models.User
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import com.reunet.app.ui.view.BaseActivity
import com.reunet.app.ui.view.MainActivity
import java.io.File

class ProfileActivity : BaseActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }

        val user: User? = sharedPreferencesUtil.getUser()

        val imageUriSaved = getImageUri(this@ProfileActivity, user!!.id.toLong())

        binding.fullName.text = "${user!!.firstname} ${user.lastname}"
        binding.email.text = user.email
        binding.circleImageView2.setImageURI(imageUriSaved)
        binding.signOff.setOnClickListener{
            val userNull = User("", "", "", "", "", "")
            sharedPreferencesUtil.saveToken("")
            sharedPreferencesUtil.saveUser(userNull)
            intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        binding.toolbar2.setNavigationOnClickListener{
            finish()
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getImageUri(context: Context, id:Long): Uri {
        val file = File(context.filesDir, id.toString())
        return if (file.exists()){
            Uri.fromFile(file)
        }else{
            Uri.parse("android.resource://com.reunet.app/drawable/ic_avatar_account")
        }
    }
}