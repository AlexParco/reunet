package com.reunet.app.ui.group

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.reunet.app.databinding.ActivityFormAddGroupBinding
import com.reunet.app.models.payload.request.GroupRequest
import com.reunet.app.services.GroupService
import com.reunet.app.storage.SharedPreferenceUtil
import com.reunet.app.ui.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormAddGroup: BaseActivity<ActivityFormAddGroupBinding>(ActivityFormAddGroupBinding::inflate){

    //Objects request
    private lateinit var group: GroupRequest

    private val groupService by lazy{
        sharedPreferencesUtil.getToken()?.let { GroupService.build(it) }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }

        //Set view size
        setWindowSize(0.8, 0.5)


        val id = sharedPreferencesUtil.getUser()?.id


        binding.createGroup.setOnClickListener{
            val name = binding.groupName.text.toString()
            val description = binding.groupDescription.text.toString()
            if (id != null) {
                group = GroupRequest(
                    name,
                    id.toInt(),
                    description
                )
            }
            try{
                if(validateData(name)){
                    addGroup(group)
                    finish()
                }
            }catch(e: Exception){
                Log.i("Error adding group", e.message.toString())
            }
        }

    }

    private fun addGroup(group: GroupRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val call = groupService?.createGroup(group)
            try{
                if (call != null) {
                    if(call.isSuccessful){
                        runOnUiThread{
                            Toast.makeText(this@FormAddGroup,
                                "Group created successfully", Toast.LENGTH_SHORT).show()
                        }
                    }else {
                        throw Exception("credentials are invalid, CODE: ${call.code()}")
                    }
                }
            }catch (e: Exception){
                Log.i("Error creating a group", e.toString())
            }
        }
    }

    private fun validateData(gName: String):Boolean{
        var validate = false

        if(gName.isEmpty()){
            binding.groupName.error = "The group name is required"
            binding.groupName.requestFocus()
        }

        if(!gName.isEmpty()){
            validate = true
        }
        return validate
    }
}