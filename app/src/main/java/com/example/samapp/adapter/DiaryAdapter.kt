package com.example.samapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.samapp.MyApplication
import com.example.samapp.databinding.ItemStudyDiaryBinding
import com.example.samapp.model.DiaryItemData
import com.example.samapp.util.dateToString
import java.util.*

class DiaryViewHolder(val binding: ItemStudyDiaryBinding) : RecyclerView.ViewHolder(binding.root)

class DiaryAdapter(val context: Context, val datas: MutableList<DiaryItemData>) :
    RecyclerView.Adapter<DiaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        return DiaryViewHolder(
            ItemStudyDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {

        val data = datas.get(position)

        holder.binding.run {
            listTitleId.text = data.title
            listDateId.text = data.date
            listLoginId.text
        }

        //스토리지 이미지 다운로드........................
        val imgRef = MyApplication.storage.reference.child("images/${data.docId}.jpg")
        imgRef.downloadUrl.addOnCompleteListener { tast ->
            if (tast.isSuccessful) {
                Glide.with(context)
                    .load(tast.result)
                    .into(holder.binding.itemImageView)
            }
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}