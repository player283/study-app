package com.example.samapp.market

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.samapp.MyApplication
import com.example.samapp.databinding.ActivitySellDetailBinding


class SellDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivitySellDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySellDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val User = intent.getStringExtra("User")
        binding.tvProductUserName.text = User

        val sName = intent.getStringExtra("sName")
        binding.tvProductName.text = sName

        val sDes = intent.getStringExtra("sDes")
        binding.tvProductDes.text = sDes

        val sPrice = intent.getStringExtra("sPrice")
        binding.tvProductPrice.text = sPrice

        val sid = intent.getStringExtra("sid")
        val imgRef = MyApplication.storage.reference.child("sellImages/${sid}.jpg")
        imgRef.downloadUrl.addOnCompleteListener { tast ->
            if (tast.isSuccessful) {
                Glide.with(this)
                    .load(tast.result)
                    .into(binding.tvPic)
            }
        }

        // 1:1 채팅 연결
        binding.btnDetailMarket.setOnClickListener {
            //채팅창 선택 시 이동
            var destinationUsers = intent.getStringExtra("User")
            val intent = Intent(this, SellMessageActivity::class.java)
            intent.putExtra("destinationUid", destinationUsers)
            Log.d("myLogtest","${destinationUsers}")
            startActivity(intent)
        }

    }
}