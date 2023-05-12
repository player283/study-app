package com.example.samapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.samapp.MyApplication
import com.example.samapp.databinding.ItemSellBinding
import com.example.samapp.fragment.SellFragment
import com.example.samapp.market.SellDetailActivity
import com.example.samapp.model.Sell

//리사이클러뷰 어댑터
class ListSellViewHolder(val binding:ItemSellBinding ): RecyclerView.ViewHolder(binding.root)

class ListSell(context1: SellFragment, val datas:MutableList<Sell>): RecyclerView.Adapter<ListSellViewHolder>(){

    lateinit var context:SellFragment
    init{
        context=context1
    }
    private val SellDetail= ArrayList<Sell>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSellViewHolder {
        return ListSellViewHolder(
            ItemSellBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )

    }

    override fun onBindViewHolder(holder: ListSellViewHolder, position: Int) {
        val data =datas.get(position)

//모델과 뷰 연결
        holder.binding.run{
            itProductName.text=data.sName
            itPrice.text=data.sPrice

        }
        //스토리지 이미지 다운로드
        val imgRef= MyApplication.storage.reference.child("sellImages/${data.sid}.jpg")
        imgRef.downloadUrl.addOnCompleteListener {tast->
            if(tast.isSuccessful){
                Glide.with(context)
                    .load(tast.result)
                    .into(holder.binding.itproductImg)
            }
        }

// 상세페이지 이동
        holder.itemView.setOnClickListener {
            val intent=Intent(context.activity,SellDetailActivity::class.java)
            intent.putExtra("sName",data.sName)
            intent.putExtra("sDes",data.sDes)
            intent.putExtra("sPrice",data.sPrice)
            intent.putExtra("sid",data.sid)
            intent.putExtra("User",data.User)
            context?.startActivity(intent)

        }

    }
    override fun getItemCount(): Int {
        return datas.size
    }
}