package com.example.samapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.samapp.MyApplication
import com.example.samapp.adapter.ListSell
import com.example.samapp.databinding.FragmentSellBinding
import com.example.samapp.market.SellListAddActivity
import com.example.samapp.model.Sell
import com.google.firebase.database.FirebaseDatabase

private val fireDatabase = FirebaseDatabase.getInstance().reference

class SellFragment : Fragment() {

    lateinit var binding: FragmentSellBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellBinding.inflate(layoutInflater, container, false)

//        새글작성버튼
        binding.sellupdateActionButton.setOnClickListener {
            val intent=Intent(context,SellListAddActivity::class.java)
            context?.startActivity(intent)

        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        makeSellRecyclerView()
    }


    fun makeSellRecyclerView(){
        MyApplication.db.collection("SellList")

            .get()
            .addOnSuccessListener { result ->
                val itemList= mutableListOf<Sell>()
                for(document in result){
                    val item=document.toObject(Sell::class.java)
                    item.sid = document.id //이미지 업로드해줌
                    itemList.add(item)
                }
                binding.marketfragmentRecyclerview.layoutManager = LinearLayoutManager(context)
                binding.marketfragmentRecyclerview.adapter = ListSell(this,itemList)
            }
            .addOnFailureListener{ exception ->
                Log.d("root","error.. getting document...", exception)

            }

    }


}






