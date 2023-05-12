package com.example.samapp.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.samapp.MyApplication
import com.example.samapp.adapter.DiaryAdapter
import com.example.samapp.databinding.ActivityStudyDiaryBinding
import com.example.samapp.model.DiaryItemData



class StudyDiaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityStudyDiaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnMyDiary.setOnClickListener {
            val intent = Intent(this, StudyDiaryDetailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        makeRecyclerView()
    }

    private fun makeRecyclerView() {
        MyApplication.db.collection("diaries")
            .get()
            .addOnSuccessListener {result ->
                val itemList = mutableListOf<DiaryItemData>()
                for (document in result){
                    val item=document.toObject(DiaryItemData::class.java)
                    item.docId=document.id
                    itemList.add(item)
                    Log.d("myLog","${item.date}")
                }
                binding.recyclerDiaryView.layoutManager =LinearLayoutManager(this)
                binding.recyclerDiaryView.adapter = DiaryAdapter(this, itemList)


            }

            .addOnFailureListener { exception->
                Log.d("myLog","리사이클뷰 에러",exception)
                Toast.makeText(this,"서버 데이터 획득 실패",Toast.LENGTH_SHORT).show()
            }
    }

}