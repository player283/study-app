package com.example.samapp.model

data class Sell(

    var sid: String? = null,//글 번호
    var sName: String? = null,//상품이름
    var sPrice: String? = null,//상품가격
    var sDes: String? = null,//책상태 내용
    var User: String? = null,//유저
    var regDate: Any = Any(),//작성날짜
)
