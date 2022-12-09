package com.reunet.app.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.reunet.app.databinding.FragmentAddGroupDialogBinding
import com.reunet.app.models.Group
import com.reunet.app.models.payload.request.GroupRequest
import com.reunet.app.models.payload.request.ParticipantRequest
import com.reunet.app.services.GroupService
import com.reunet.app.services.ParticipantService
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import com.reunet.app.ui.view.group.GroupActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddGroupDialogFragment(
    private val onSaveClickListener: (String) -> Unit,
    sharedPreferencesUtil: SharedPreferenceUtil
): DialogFragment() {

    private val idUser = sharedPreferencesUtil.getUser()?.id

    private lateinit var binding: FragmentAddGroupDialogBinding

    private lateinit var participant: ParticipantRequest

    private val groupService by lazy{
        sharedPreferencesUtil.getToken()?.let { GroupService.build(it) }
    }

    private val participantService by lazy {
        sharedPreferencesUtil.getToken()?.let { ParticipantService.build(it) }
    }

    //Objects request
    private lateinit var group: GroupRequest

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = FragmentAddGroupDialogBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.createGroup.setOnClickListener {
            val name = binding.groupName.text.toString()
            val description = binding.groupDescription.text.toString()
            if (idUser != null) {
                group = GroupRequest(
                    name,
                    idUser.toInt(),
                    description
                )
            }
            try{
                if(validateData(name)){
                    addGroup(group)
                    val intent = Intent(activity, GroupActivity::class.java)
                    startActivity(intent)
                    dismiss()
                }
            }catch(e: Exception){
                Log.i("Error adding group", e.message.toString())
            }
        }

        binding.cancelCreation.setOnClickListener{
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)
        return dialog
    }

    private fun addGroup(group: GroupRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val callCreate = groupService?.createGroup(group)
            val callGet = groupService?.getGroups()
            try{
                    if(callCreate!!.isSuccessful){
                        val groupCreated: Group? = callCreate.body()?.data
                        //To be continued
                        Handler(Looper.getMainLooper()).post(Runnable {
                            fun run() {
                                onSaveClickListener.invoke("Group created successfully")
                            }
                            run()
                        })
                        participant = ParticipantRequest(
                            groupCreated?.id ?: "",
                            idUser.toString())
                        addParticipant(participant)
                    }else {
                        Handler(Looper.getMainLooper()).post(Runnable {
                            fun run() {
                                onSaveClickListener.invoke("Could not create group")
                            }
                            run()
                        })
                    }
            }catch (e: Exception){
                Log.i("ERROR IN FUNCTION", e.message.toString())
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

    private fun addParticipant(participant: ParticipantRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val call = participantService?.addParticipant(participant)
            try {
                if (call?.isSuccessful == true) {
                    Log.i("ADD FUNCTION", "User added successfully")
                }else{
                    throw Exception("credentials are invalid, CODE: ${call?.code()}")
                }
            } catch (e: Exception) {
                Log.i("Error adding participant", e.toString())
            }
        }
    }
}