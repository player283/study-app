package com.example.samapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.samapp.databinding.ActivityIntroBinding
import com.example.samapp.user.UserLoginActivity

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@IntroActivity, UserLoginActivity::class.java)
            startActivity(intent)
        }
    }
}



