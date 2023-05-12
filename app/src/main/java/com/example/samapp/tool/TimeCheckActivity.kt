package com.example.samapp.tool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.example.samapp.databinding.ActivityTimeCheckBinding

class TimeCheckActivity : AppCompatActivity() {
    //스탑워치 멈춘 시간 계산 하기위한 기초시간
    var pauseTime = 0L
    // 뒤로가기버튼의 시간 계산 하기위한 기초시간
    var initTime = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTimeCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //스탑워치 기능 구현
        //시작버튼
        binding.start.setOnClickListener {
            //스탑워치 시간 = 부팅부터 현재까지 시간 + 멈춘시간
            //SystemClock.elapsedRealtime() : 부팅된 시점부터 현재까지의 시간을 millisecond로 리턴
            binding.chronometer.base = SystemClock.elapsedRealtime() + pauseTime
            //스탑워치 시작
            binding.chronometer.start()

            //버튼의 표시 조정
            binding.start.isEnabled = false
            binding.stop.isEnabled = true
            binding.reset.isEnabled = true
        }
        //스탑버튼
        binding.stop.setOnClickListener {
            // 멈춘시간 = 스탑워치 시간 - SystemClock.elapsedRealtime()
            pauseTime = binding.chronometer.base - SystemClock.elapsedRealtime()
            // 스탑워치 멈춤
            binding.chronometer.stop()
            //버튼의 표시 조정
            binding.start.isEnabled = true
            binding.stop.isEnabled = false
            binding.reset.isEnabled = true
        }
        //리셋버튼
        binding.reset.setOnClickListener {
            //멈춘시간 초기화
            pauseTime = 0L
            binding.chronometer.base = SystemClock.elapsedRealtime()
            //스탑워치 멈춤
            binding.chronometer.stop()
            //버튼의 표시 조정
            binding.start.isEnabled = true
            binding.stop.isEnabled = false
            binding.reset.isEnabled = false
        }
    }
}