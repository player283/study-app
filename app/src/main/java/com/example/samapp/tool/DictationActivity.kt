package com.example.samapp.tool

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.samapp.R
import com.example.samapp.databinding.ActivityDictationBinding

class DictationActivity : AppCompatActivity() {
    private var btn1: Button? = null
    private var tvTest: TextView? = null
    private val RESULT_SPEECH = 1
    lateinit var binding : ActivityDictationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDictationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        btn1 = findViewById(R.id.btn1)
        tvTest = findViewById(R.id.tvTest)

        binding.btn1.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            startActivityForResult(intent, RESULT_SPEECH)
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_SPEECH -> if (requestCode == RESULT_OK && data != null) {
                val datas = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                tvTest!!.text = datas!![0]
            }
        }
    }
}