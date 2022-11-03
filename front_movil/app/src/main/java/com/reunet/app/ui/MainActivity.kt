package com.reunet.app.ui

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.reunet.app.databinding.ActivityMainBinding
import com.reunet.app.models.User
import com.reunet.app.ui.events.EventsActivity
import com.reunet.app.ui.groups.GroupsActivity
import com.reunet.app.ui.user.account.EditProfileActivity
import java.io.File


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private var USER: String = "USER"
    private var TOKEN: String = "TOKEN"

    //For avatar image
    private val SELECT_ACTIVITY_IMG =  50
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user: User = intent.getSerializableExtra(USER) as User
        val token: String = intent.getStringExtra(TOKEN).toString()

        val imageUriSaved = getImageUri(this@MainActivity, user.id.toLong())

        binding.username.text = "${user.firstname} ${user.lastname}"

        binding.editProfileBtn.setOnClickListener{
            intent = Intent(this@MainActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnActivities.setOnClickListener{
            intent = Intent(this@MainActivity, EventsActivity::class.java)
            intent.putExtra(TOKEN, token)
            startActivity(intent)
        }

        binding.btnGroups.setOnClickListener{
            intent = Intent(this@MainActivity, GroupsActivity::class.java)
            intent.putExtra(TOKEN, token)
            startActivity(intent)
        }

        binding.imgAvatar.setImageURI(imageUriSaved)

        binding.imgAvatar.setOnClickListener{
            uploadImage(this, SELECT_ACTIVITY_IMG)
            imageUri?.let {
                saveImage(this@MainActivity, user.id.toLong(), it)
            }
            if(imageUri == null){
                Log.i(TAG, "Image don't save my king")
            }
        }
    }

    //For avatar image
    fun uploadImage(activity: Activity, code: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }

    fun saveImage(context: Context, id: Long, uri: Uri){
        val file = File(context.filesDir, id.toString())
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()!!
        file.writeBytes(bytes)
        Log.i(TAG, "image saved")
    }

    fun getImageUri(context: Context, id:Long):Uri{
        val file = File(context.filesDir, id.toString())
        return if (file.exists()){
             Uri.fromFile(file)
        }else{
            Uri.parse("android.resource://com.reunet.app/drawable/ic_avatar_account")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when{
            requestCode == SELECT_ACTIVITY_IMG && resultCode == Activity.RESULT_OK ->{
                imageUri = data!!.data
                binding.imgAvatar.setImageURI(imageUri)
            }
        }
    }
}


