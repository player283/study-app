package com.example.samapp.diary

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
import com.example.samapp.databinding.ActivityStudyDiaryDetailBinding
import com.example.samapp.util.dateToString
import com.example.samapp.util.myCheckPermission
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class StudyDiaryDetailActivity : AppCompatActivity() {

    //파일 페스 선언
    lateinit var filePath: String

    //바인딩 선언
    lateinit var binding: ActivityStudyDiaryDetailBinding
    val diaryCollectionRef = Firebase.firestore.collection("diaries")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyDiaryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(Date())
        binding.currentDate.text = dateFormat



        //인증 해서 사진 업로드하기 위치 어디로???
        myCheckPermission(this)

        //입력 버튼
        binding.btnUpdate.setOnClickListener {
            if(binding.picture1.drawable !== null && binding.diaryTitle.text.isNotEmpty() && binding.diaryContent.text.isNotEmpty()){
                //store 에 먼저 데이터를 저장후 document id 값으로 업로드 파일 이름 지정
                saveDiary()


            }else {
                Toast.makeText(this, "데이터가 모두 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }


        }
        //사진첨부
        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            requestLauncher.launch(intent)
        }


    }


    //다이어리 데이터 저장
    private fun saveDiary() {
        var data = mapOf(

            "title" to binding.diaryTitle.text.toString(),
            "content" to binding.diaryContent.text.toString(),
            "date" to dateToString(Date()),
        )
        MyApplication.db.collection("diaries")
            .add(data)
            .addOnSuccessListener {
                uploadImage(it.id)

            }
            .addOnFailureListener {
                Log.d("myLog", "data save error",it)
            }
    }

    // 글라이더 이미지 보내기?
    val requestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult())
    {
        if (it.resultCode === android.app.Activity.RESULT_OK) {
            Glide
                .with(applicationContext)
                .load(it.data?.data)
                .apply(RequestOptions().override(250, 200))
                .centerCrop()
                .into(binding.picture1)

            val cursor = contentResolver.query(it.data?.data as Uri,
                arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null);
            cursor?.moveToFirst().let {
                filePath = cursor?.getString(0) as String
            }

        }
    }

    //이미지 업로드
    private fun uploadImage(docId: String) {
        //add............................
        val storage = MyApplication.storage
        val storageRef = storage.reference
        val imgRef = storageRef.child("images/${docId}.jpg")
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

