package com.reunet.app.ui.holder

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ItemLeftMessageBinding
import com.reunet.app.models.Message
import com.reunet.app.models.MessageStatus
import com.reunet.app.models.User
import com.reunet.app.services.UserService
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LeftMessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemLeftMessageBinding.bind(view)
    lateinit var  sharedPreferencesUtil: SharedPreferenceUtil

    private val userService by lazy {
        sharedPreferencesUtil.getToken()?.let { UserService.build(it)}
    }

    fun bind(message: MessageStatus, context: Context){
        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(context)
        }
        with(binding){

            if (message.isRemoved) bodyMessage.text = "This mesagge was deleted"
            else if(message.isReport) bodyMessage.text = "This mesagge was reported"
            else {
                bodyMessage.text = message.message.body

                bodyMessage.setOnLongClickListener{
                    actionsForMsg.visibility = View.VISIBLE
                    return@setOnLongClickListener true
                }

                bodyMessage.setOnClickListener{
                    actionsForMsg.visibility = View.GONE
                }

                reportMsg.setOnClickListener{
                    message.isReport = true
                    bodyMessage.text = "This mesagge was reported"
                    Toast.makeText(context, "Message reported successfully", Toast.LENGTH_SHORT).show()
                    actionsForMsg.visibility = View.GONE
                }
                deleteMsg.setOnClickListener{
                    message.isRemoved = true
                    bodyMessage.text = "This mesagge was deleted"
                    Toast.makeText(context, "Message deleted successfully", Toast.LENGTH_SHORT).show()
                    actionsForMsg.visibility = View.GONE
                }
            }
        }
        checkParticipantInGroup(message.message.userId)
    }

    private fun checkParticipantInGroup(idUser: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = userService?.getUserById(idUser.toInt())
            if (call?.isSuccessful == true){
                val userResponse = call.body()
                val user: User = userResponse!!.data
                Handler(Looper.getMainLooper()).post(Runnable {
                    fun run(){
                       binding.nameSender.text = "${user.firstname} ${user.lastname}"
                    }
                    run()
                })
            }
        }
    }
}