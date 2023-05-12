package com.example.samapp.tool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.samapp.MyApplication
import com.example.samapp.adapter.WordAdapter
import com.example.samapp.databinding.ActivityWordBinding
import com.example.samapp.model.WordData

class WordActivity : AppCompatActivity() {
  lateinit var binding:ActivityWordBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityWordBinding.inflate(layoutInflater)
    setContentView(binding.root)

binding.btnRegWord.setOnClickListener {
  val intent =Intent(this,WordRegActivity::class.java)
  startActivity(intent)
}

  }

  override fun onStart() {
    super.onStart()
    makeWordRecyclerView()
  }

  private fun makeWordRecyclerView() {
    MyApplication.db.collection("wordpass")
      .get()
      .addOnSuccessListener {result ->
        val itemList2 = mutableListOf<WordData>()
        for (document in result){
          val item2 = document.toObject(WordData::class.java)
          item2.wordId=document.id
          itemList2.add(item2)


          Log.d("myLog","${document},${item2.wordKr},,${item2.wordId}")
        }

        binding.recyclerWordView.layoutManager = LinearLayoutManager(this)
        binding.recyclerWordView.adapter = WordAdapter(this, itemList2)

      }
      .addOnFailureListener { exception->
        Log.d("myLog","리사이클뷰 에러",exception)
        Toast.makeText(this,"서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
      }
  }
}