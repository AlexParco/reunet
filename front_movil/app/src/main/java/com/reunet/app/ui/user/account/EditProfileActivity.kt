package com.reunet.app.ui.user.account

import android.os.Bundle
import com.reunet.app.R
import com.reunet.app.ui.BaseActivity
import com.reunet.app.databinding.ActivityEditProfileBinding
import com.reunet.app.models.User

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>(ActivityEditProfileBinding::inflate) {
    private var USER: String = "USER"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}