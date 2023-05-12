package com.example.samapp.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.samapp.databinding.ActivityUserAdminPageBinding

class UserAdminPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityUserAdminPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}