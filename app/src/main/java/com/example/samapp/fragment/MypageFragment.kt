package com.example.samapp.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.samapp.MyApplication.Companion.currentUserName
import com.example.samapp.MyApplication.Companion.db
import com.example.samapp.MyApplication.Companion.email
import com.example.samapp.SplashActivity
import com.example.samapp.UserUpdateActivity
import com.example.samapp.UserUpdateActivity.Companion.test
import com.example.samapp.databinding.FragmentMypageBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase




private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MypageFragment : Fragment() {

  val user = Firebase.auth.currentUser!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    val binding = FragmentMypageBinding.inflate(layoutInflater)


   binding.profileTextviewName.text = currentUserName


    binding.btnMyinfo.setOnClickListener {
      val intentTwo = Intent(context, UserUpdateActivity::class.java)
      startActivity(intentTwo)
    }

    Log.d("myLog","${test}")

    //회원 탈퇴
    binding.btnBan.setOnClickListener {

      //스토어 데이터 삭제
      db.collection("userInfo").document("$test")
        .delete()
        .addOnSuccessListener { Log.d("myLog", "DocumentSnapshot successfully deleted!") }
        .addOnFailureListener { e -> Log.w("myLOg", "Error deleting document", e) }
     //회원 삭제
      user!!.delete()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                Log.d("myLog", "User account deleted.")

                Toast.makeText(context,"회원탈퇴가 완료 되었습니다.",Toast.LENGTH_SHORT).show()

                Handler().postDelayed({
                    val intent = Intent(context, SplashActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }, 3000)
            }
        }
        .addOnFailureListener {
          Log.d("myLog", "error")
        }
    }


//비밀번호 변경
binding.btnModify.setOnClickListener {
  email?.let { it1 ->
    Firebase.auth.sendPasswordResetEmail(it1)
    .addOnCompleteListener { task ->
      if (task.isSuccessful) {
        Log.d("TAG", "Email sent.")
      }
    }
  }
}
    return binding.root
  }
}
