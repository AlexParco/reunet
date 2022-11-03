package com.reunet.app.ui.user.account

import android.os.Bundle
import com.reunet.app.databinding.ActivityProfleBinding
import com.reunet.app.models.User
import com.reunet.app.ui.BaseActivity

class ProfileActivity : BaseActivity<ActivityProfleBinding>(ActivityProfleBinding::inflate) {
    private var USER: String = "USER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user: User = intent.getSerializableExtra(USER) as User
        binding.username.text = user.firstname
    }
}