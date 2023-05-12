package com.example.samapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.samapp.databinding.FragmentHomeBinding
import com.example.samapp.diary.StudyDiaryActivity


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.goDiary.setOnClickListener {
            val intent = Intent(context, StudyDiaryActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}