package com.reunet.app.ui.holder

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ItemRightMessageBinding
import com.reunet.app.models.MessageStatus
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import java.io.File

class RightMessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemRightMessageBinding.bind(view)
    lateinit var  sharedPreferencesUtil: SharedPreferenceUtil

    fun bind(message: MessageStatus, context: Context){
        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(context)
        }
        val imageUriSaved = getImageUri(context,  sharedPreferencesUtil.getUser()?.id!!.toLong())

        with(binding){
            if (message.isRemoved) bodyMessage.text = "This message was deleted"
            else if(message.isReport) bodyMessage.text = "This message was reported"
            else {
                bodyMessage.text = message.message.body
                circleImageView.setImageURI(imageUriSaved)

                bodyMessage.setOnLongClickListener{
                    actionsForMsg.visibility = View.VISIBLE
                    return@setOnLongClickListener true
                }

                bodyMessage.setOnClickListener{
                    actionsForMsg.visibility = View.GONE
                }

                reportMsg.setOnClickListener{
                    message.isReport = true
                    bodyMessage.text = "This message was reported"

                    Toast.makeText(context,
                        "Message reported successfully",
                        Toast.LENGTH_SHORT).show()

                    actionsForMsg.visibility = View.GONE
                }
                deleteMsg.setOnClickListener{
                    message.isRemoved = true
                    bodyMessage.text = "This message was deleted"

                    Toast.makeText(context,
                        "Message deleted successfully",
                        Toast.LENGTH_SHORT).show()

                    actionsForMsg.visibility = View.GONE
                }
            }
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