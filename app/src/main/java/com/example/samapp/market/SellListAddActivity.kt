package com.example.samapp.market

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.samapp.MyApplication
import com.example.samapp.MyApplication.Companion.auth
import com.example.samapp.databinding.ActivitySellListAddBinding
import com.example.samapp.util.myCheckPermission
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.File


class SellListAddActivity : AppCompatActivity() {
    lateinit var filePath: String
    lateinit var binding: ActivitySellListAddBinding


    val SellCollectionRef = Firebase.firestore.collection("SellList")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySellListAddBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myCheckPermission(this)

        binding.sellUpdatebtn.setOnClickListener {
            if (binding.btnRegPImg.drawable !== null ){
                saveSellist()
            }else{
                Toast.makeText(this, "데이터가 모두 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        //에뮬에서 선택한 사진보이기
        binding.btnPutImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            requestLauncher.launch(intent)
        }

    }
    //모델에 저장할 데이터(모델과 "이름"이 같아야함)
    private fun saveSellist() {
        val user = auth.currentUser
        var data = mapOf(
            "sName" to binding.edPName.text.toString(),
            "sPrice" to binding.edPrice.text.toString(),
            "sDes" to binding.edDes.text.toString(),
            "User" to  user?.uid.toString()
        )
        MyApplication.db.collection("SellList")
            .add(data)
            .addOnSuccessListener {
                uploadSellImage(it.id)


            }
            .addOnFailureListener {
                Log.d("myLog", "data save error",it)
            }
    }

    // 이미지 규격
    val requestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult())
    {
        if (it.resultCode === android.app.Activity.RESULT_OK) {
            Glide
                .with(applicationContext)
                .load(it.data?.data)
                .apply(RequestOptions().override(250, 200))
                .centerCrop()
                .into(binding.btnRegPImg)

            val cursor = contentResolver.query(it.data?.data as Uri,
                arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null);
            cursor?.moveToFirst().let {
                filePath = cursor?.getString(0) as String
            }

        }
    }

    //이미지 업로드
    private fun uploadSellImage(sid: String) {
        val storage = MyApplication.storage
        val storageRef = storage.reference
        val imgRef = storageRef.child("sellImages/${sid}.jpg")
        val file = Uri.fromFile(File(filePath))
        imgRef.putFile(file) //파일 업로드
            .addOnSuccessListener {
                Toast.makeText(this, "save Img", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Log.d("myLog", "file save error", it)
            }
    }
}