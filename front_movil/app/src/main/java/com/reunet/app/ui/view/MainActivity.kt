package com.reunet.app.ui.view

import android.app.Activity
import com.reunet.app.models.Activity as ActivityModel
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.reunet.app.databinding.ActivityMainBinding
import com.reunet.app.models.User
import com.reunet.app.services.GroupService
import com.reunet.app.services.ParticipantService
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import com.reunet.app.ui.view.chat.SocialActivity
import com.reunet.app.ui.view.event.PendingEventActivity
import com.reunet.app.ui.view.group.GroupActivity
import com.reunet.app.ui.view.user.ProfileActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : BaseActivity<ActivityMainBinding>
    (ActivityMainBinding::inflate) {
    //For avatar image
    private val SELECT_ACTIVITY_IMG =  50
    private val ACTIVITIES = "ACTIVITIES"
    private var imageUri: Uri? = null

    private var pendingActivity = mutableListOf<ActivityModel>()

    private var auxListActivity = ArrayList<ActivityModel>()

    private val groupService by lazy{
        sharedPreferencesUtil.getToken()?.let { GroupService.build(it) }
    }

    private val participantService by lazy{
        sharedPreferencesUtil.getToken()?.let { ParticipantService.build(it) }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }

        val user: User? = sharedPreferencesUtil.getUser()

        val imageUriSaved = getImageUri(this@MainActivity, user!!.id.toLong())

        binding.username.text = "${user.firstname} ${user.lastname}"

        getPendingActivities()

        //principal events
        binding.btnGroups.setOnClickListener{
            intent = Intent(this@MainActivity, GroupActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnChat.setOnClickListener{
            intent = Intent(this@MainActivity, SocialActivity:: class.java)
            startActivity(intent)
            finish()
        }

        binding.btnProfile.setOnClickListener{
            intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnActivities.setOnClickListener{
            intent = Intent(this@MainActivity, PendingEventActivity::class.java)
            intent.putExtra(ACTIVITIES, auxListActivity)
            startActivity(intent)
            finish()
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
    private fun uploadImage(activity: Activity, code: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }

    private fun saveImage(context: Context, id: Long, uri: Uri){
        val file = File(context.filesDir, id.toString())
        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()!!
        file.writeBytes(bytes)
        Log.i(TAG, "image saved")
    }

    private fun getImageUri(context: Context, id:Long):Uri{
        val file = File(context.filesDir, id.toString())
        return if (file.exists()){
             Uri.fromFile(file)
        }else{
            Uri.parse("android.resource://com.reunet.app/drawable/ic_avatar_account")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
            requestCode == SELECT_ACTIVITY_IMG && resultCode == Activity.RESULT_OK ->{
                imageUri = data!!.data
                binding.imgAvatar.setImageURI(imageUri)
            }
        }
    }


    private fun getPendingActivities(){
        val user_id = sharedPreferencesUtil.getUser()?.id?.toInt()
        CoroutineScope(Dispatchers.IO).launch {
            val callGroup = groupService?.getGroups()
            try {
                if (callGroup!!.isSuccessful){
                    val groupResponse = callGroup.body()?.data ?: emptyList()
                    for(group in groupResponse){
                        val idGroup = group.id
                        val callParticipant = participantService?.getParticipantsByGroup(idGroup)
                        val participantResponse = callParticipant!!.body()?.data ?: emptyList()
                        for(participant in participantResponse){
                            if(participant.userId == user_id){
                                val callActivities = groupService?.getActivityByGroupId(idGroup)
                                val activityResponse = callActivities!!.body()?.data ?: emptyList()
                                for (activity in activityResponse){
                                    val actualDate = Calendar.getInstance().time
                                    val dateSubString = activity.closedAt.substring(0, 10)
                                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                                    val dateCompare: Date = sdf.parse(dateSubString) as Date
                                    val cmp = dateCompare.compareTo(actualDate)
                                    when{
                                        cmp > 0 -> {
                                            auxListActivity.add(activity)
                                        }
                                    }
                                }
                                runOnUiThread{
                                    pendingActivity.clear()
                                    pendingActivity.addAll(auxListActivity.toList())
                                    binding.ibvEvents.badgeValue = pendingActivity.size
                                }
                            }
                        }
                    }
                }else{
                    callGroup.errorBody()?.close()
                }
            }catch (e: Exception){
                Log.i("Error with coroutines", e.toString())
            }
        }
    }
}


