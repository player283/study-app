package com.example.samapp.tool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.samapp.MyApplication
import com.example.samapp.databinding.ActivityWordRegBinding
import com.example.samapp.util.dateToString
import java.util.*

class WordRegActivity : AppCompatActivity() {

    lateinit var binding: ActivityWordRegBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordRegBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnWordUpdate.setOnClickListener {
            if (binding.editKr.text.isNotEmpty() && binding.editEn.text.isNotEmpty()) {
                saveWord()
            } else {
                Toast.makeText(this, "데이터가 모두 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveWord() {
        var data = mapOf(

            "wordKr" to binding.editKr.text.toString(),
            "wordEn" to binding.editEn.text.toString(),
            "wordDate" to dateToString(Date())
        )
        MyApplication.db.collection("wordpass")
            .add(data)
            .addOnSuccessListener {
                Log.d("myLog", "data save ok")
            }

            .addOnFailureListener {
                Log.d("myLog", "data save error")
            }
      finish()
    }
}