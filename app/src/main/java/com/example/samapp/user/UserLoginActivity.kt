package com.example.samapp.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.samapp.MainActivity
import com.example.samapp.MyApplication
import com.example.samapp.databinding.ActivityUserLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class UserLoginActivity : AppCompatActivity() {


    lateinit var binding: ActivityUserLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (MyApplication.checkAuth()) {
            changeVisibility("login")
        } else {
            changeVisibility("logout")
        }

        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )
        {
            //구글 로그인 결과 처리...........................
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                MyApplication.auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            MyApplication.email = account.email
                            changeVisibility("login")
                        } else {
                            changeVisibility("logout")
                        }
                    }
            } catch (e: ApiException) {
                changeVisibility("logout")
            }
        }

        binding.logoutBtn.setOnClickListener {
            //로그아웃...........
            MyApplication.auth.signOut()
            MyApplication.email = null
            changeVisibility("logout")
        }

        binding.goSignInBtn.setOnClickListener {
            changeVisibility("signin")
        }

        binding.signBtn.setOnClickListener {
            //이메일,비밀번호 회원가입........................
            val email = binding.authEmailEditView.text.toString()
            val name = binding.authNameEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()

            if( password.length < 6) {
                Toast.makeText(this,"비밀번호를 6자리 이상으로 입력해주세요",Toast.LENGTH_SHORT).show()
            }

            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    binding.authNameEditView.text.clear()

                    if (task.isSuccessful) {
                        MyApplication.auth.currentUser?.sendEmailVerification() // 유효한 이메일이 보내졌는지 검증
                            ?.addOnCompleteListener { sendTask ->
                                if (sendTask.isSuccessful) { // 유효하면
                                    Toast.makeText(
                                        baseContext,
                                        "회원가입에서 성공, 전송 메일을 확인해주세요",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    fireStoreSave(email,name,password, uId = String())

                                    changeVisibility("logout")
                                } else { //유효하지 않으면
                                    Toast.makeText(baseContext, "메일 발송 실패", Toast.LENGTH_SHORT)
                                        .show()
                                    changeVisibility("logout")
                                }
                            }
                    } else {
                        Toast.makeText(baseContext, "회원가입실패", Toast.LENGTH_SHORT).show()
                        changeVisibility("logout")
                    }
                }
        }


        binding.loginBtn.setOnClickListener {
            //이메일, 비밀번호 로그인.......................
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()


            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()

                    if (task.isSuccessful) {
                        if (MyApplication.checkAuth()) {
                            MyApplication.email = email
                            changeVisibility("login")

                            Handler().postDelayed({
                                val intent = Intent(this, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                startActivity(intent)
                                finish()
                            }, 2000)

                        } else {
                            Toast.makeText(
                                baseContext,
                                "전송된 메일로 이메일 인증이 되지 않았습니다",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    private fun fireStoreSave(email: String, name: String, password: String, uId:String) {
        var data = mapOf(
            "email" to email,
            "name" to name,
            "password" to password,
            "uId" to uId

        )
        MyApplication.db.collection("userInfo")
            .add(data)
            .addOnSuccessListener {
                Log.d("root", "data save")
            }
            .addOnFailureListener {
                Log.d("root", "data save error")
            }
    }


    fun changeVisibility(mode: String) {
        if (mode === "login") {

            val user = Firebase.auth.currentUser!!

            MyApplication.findUSerName()



            binding.run {
                authMainTextView.text = "${MyApplication.email} 님 반갑습니다."
                logoutBtn.visibility = View.VISIBLE
                goSignInBtn.visibility = View.GONE
                authPasswordEditView.visibility = View.GONE
                authNameEditView.visibility = View.GONE
                authEmailEditView.visibility = View.GONE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.GONE
                Log.d("root","이름")
            }

        } else if (mode === "logout") {
            binding.run {
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                logoutBtn.visibility = View.GONE
//                toMainBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE
//                googleLoginBtn.visibility = View.VISIBLE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                authNameEditView.visibility = View.GONE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.VISIBLE
            }
        } else if (mode === "signin") {
            binding.run {
                authMainTextView.text = "회원가입을 위해 상세 내역을 입력해주세요."
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
//                googleLoginBtn.visibility = View.GONE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                authNameEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE
                loginBtn.visibility = View.GONE
            }
        }
    }
}