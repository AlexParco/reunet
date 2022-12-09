package com.reunet.app.ui.view.chat

import android.app.ProgressDialog
import android.content.Intent
import com.reunet.app.databinding.ActivityChatBinding
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.reunet.app.models.Group
import com.reunet.app.models.Message
import com.reunet.app.models.MessageStatus
import com.reunet.app.models.payload.request.MessageRequest
import com.reunet.app.services.MessageService
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import com.reunet.app.ui.adapter.MessageAdapter
import com.reunet.app.ui.view.BaseActivity
import com.reunet.app.ui.view.MainActivity
import com.reunet.app.ui.view.group.InfoGroupActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatActivity : BaseActivity<ActivityChatBinding>(ActivityChatBinding::inflate) {
    private val GROUP: String = "GROUP"

    var adapter: MessageAdapter? = null
    var dialog: ProgressDialog? = null
    var senderUid: String? = null
    var receiveruid: String? = null
    val msgCannotSent = listOf("lol", "csm", "hdp", "crj", "hdpt", "hate you", "wtf")
    val messagesAux = ArrayList<MessageStatus>()
    val listMessage = mutableListOf<MessageStatus>()

    private val messageService by lazy{
        sharedPreferencesUtil.getToken()?.let { MessageService.build(it)}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val group: Group = intent.getSerializableExtra(GROUP) as Group
        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }


        binding.toolbar.title = group.name

        binding.toolbar.setNavigationOnClickListener{
            val intent = Intent(this, SocialActivity::class.java)
            startActivity(intent)
            finish()
        }

        dialog = ProgressDialog(this@ChatActivity)
        dialog!!.setMessage("Uploading...")
        dialog!!.setCancelable(false)

        senderUid = sharedPreferencesUtil.getUser()?.id
        receiveruid = group.id

        val sender = senderUid + receiveruid
        val receiver = receiveruid +  senderUid

        getMessagesByGroupId(group.id)

        adapter = MessageAdapter(this@ChatActivity, listMessage, sender, receiver)

        binding.messages.layoutManager = LinearLayoutManager(this)

        binding.messages.adapter = adapter

        binding.sendMessage.setOnClickListener {

            var validate = true

            val messageTxt = binding.bodyMessage.text.toString()

            for (msg in msgCannotSent){
                if(messageTxt == msg){
                    Toast.makeText(this@ChatActivity,
                        "you can't send that kind of message",
                        Toast.LENGTH_SHORT).show()
                    validate = false
                    break
                }
            }

            if(validate){
                val userId = sharedPreferencesUtil.getUser()?.id
                val message = MessageRequest(
                    userId!!,
                    group.id,
                    messageTxt)
                sendMessage(message)
            }
        }
    }

    private fun getMessagesByGroupId(groupId: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = messageService?.getMessages(groupId)
            if(call!!.isSuccessful){
                val messagesResponse = call.body()
                val messages: List<Message> = messagesResponse!!.data
                for(message in messages){
                    val messageStatus = MessageStatus(
                        message,
                        false,
                        false
                    )
                    messagesAux.add(messageStatus)
                }
                if(messages.size > 0){
                    runOnUiThread{
                        listMessage.clear()
                        listMessage.addAll(messagesAux.toList())
                        adapter?.notifyDataSetChanged()
                    }
                }else{
                    runOnUiThread{
                        binding.noMessages.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun sendMessage(message: MessageRequest){
        val group: Group = intent.getSerializableExtra(GROUP) as Group
        CoroutineScope(Dispatchers.IO).launch {
            val call = messageService?.sendMessage(message)
            val response: Message = call!!.body()?.data ?: Message("", "", "", "", "")
            val msgAddSt = MessageStatus(response, false, false)
            if(call!!.isSuccessful){
                runOnUiThread{
                    binding.bodyMessage.setText("")
                    listMessage.add(msgAddSt)
                }
            }
        }
    }
}
