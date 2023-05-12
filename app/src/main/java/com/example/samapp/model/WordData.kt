package com.example.samapp.model

data class WordData(
  var wordId: String? = null,//단어 아이디
  var wordEn: String? = null,//영단어
  var wordKr: String? = null,//한글단어
  var wordDate: String? = null,//날짜
  var wordUId: String? = null,//유저 아이디
)
