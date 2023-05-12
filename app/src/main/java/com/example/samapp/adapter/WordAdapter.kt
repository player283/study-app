package com.example.samapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.samapp.databinding.ItemWordBinding
import com.example.samapp.model.WordData

class WordViewHolder(val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root)

class WordAdapter(val context: Context, val worddatas: MutableList<WordData>) :
  RecyclerView.Adapter<WordViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
    return WordViewHolder(
      ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
  }

  override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
    val data =worddatas.get(position)

    holder.binding.run{
      showEn.text = data.wordEn
      showKr.text = data.wordKr
      worddate.text = data.wordDate
    }
  }

  override fun getItemCount(): Int {
    return worddatas.size
  }
}