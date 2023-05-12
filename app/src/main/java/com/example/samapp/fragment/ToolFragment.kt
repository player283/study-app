package com.example.samapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.samapp.databinding.FragmentToolBinding
import com.example.samapp.tool.*


class ToolFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //툴프레그 xml 먼트 연결
        val binding = FragmentToolBinding.inflate(layoutInflater)

        //toolfragment id값 불러와서 인텐트로 출력
        binding.timecheck.setOnClickListener {
            val intent = Intent(context, TimeCheckActivity::class.java)
            startActivity(intent)
        }

        binding.krEntran.setOnClickListener {
            val intent = Intent(context, Kr_EnActivity::class.java)
            startActivity(intent)
        }
        binding.enKrtran.setOnClickListener {
            val intent = Intent(context, En_KrActivity::class.java)
            startActivity(intent)
        }

        binding.dictation.setOnClickListener {
            val intent = Intent(context, DictationActivity::class.java)
            startActivity(intent)
        }
        binding.wordpass.setOnClickListener {
            val intent = Intent(context, WordActivity::class.java)
            startActivity(intent)
        }

        return binding.root

    }
}