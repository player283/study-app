package com.example.samapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.samapp.databinding.ActivitySplashBinding
import com.example.samapp.user.UserAuthUtils


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = UserAuthUtils.getUid() // FirebaseAuthUtils 에서 uid 가지고 온다.

        if (uid == "null") { // uid "null"값 (세션기록 삭제로 인한 로그인기록이 없거나, 회원가입이 필요한 경우, 로그인 및 회원가입 페이지로 이동)
            Handler().postDelayed({
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, 3000)

        } else { // 로그인 기록이 있을 경우에는 MainActivity 로 바로 이동
            Handler().postDelayed({
                MyApplication.findUSerName()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, 3000)
        }

    }

}