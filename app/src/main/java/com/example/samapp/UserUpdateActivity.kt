package com.example.samapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.samapp.MyApplication.Companion.db
import com.example.samapp.databinding.ActivityUserUpdateBinding
import com.example.samapp.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class UserUpdateActivity : AppCompatActivity() {

  var test1: String? = null

  companion object {
    var test: String? = null
    var userName: String? = null
    private val user = Firebase.auth.currentUser
    private val email = user?.email.toString()
  }

  lateinit var binding: ActivityUserUpdateBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityUserUpdateBinding.inflate(layoutInflater)
    setContentView(binding.root)

    testtest()

    binding.btn.text = userName

    binding.btnUserUpdate.setOnClickListener {
      updateUserInfo()
    }
  }

  //firebase에 저장된 유저 정보 불러오기
  private fun testtest() {
    db.collection("userInfo")
      .whereEqualTo("email", email)
      .get()
      .addOnSuccessListener { result ->
        for (document in result) {
          val item = document.toObject(User::class.java)
          item.uId = document.id
          test = item.uId
          test1 = item.password
          userName = item.name

          Log.d("myLog", "${item.uId},${test1}")
          }
      }
  }

  //업데이트
  private fun updateUserInfo() {

    var uId = test
    var passWord1 = test1

    var userInfoData = mapOf(
      "name" to binding.updateName.text.toString().trim(),
      "email" to email,
      "password" to passWord1,
      "uId" to ""
    )
    db.collection("userInfo").document("$uId")
      .set(userInfoData)
      .addOnSuccessListener {
        Log.d("myLog", "data good")
      }
      .addOnFailureListener {
        Log.d("myLog", "data save error")
      }

    if (MyApplication.checkAuth()) {
      MyApplication.email = email

     Handler().postDelayed({
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
      }, 3000)
    }
    Toast.makeText(baseContext, "수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show()
  }
}



