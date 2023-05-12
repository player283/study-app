package com.example.samapp.model


data class User(
    var uId: String? = null,
    var email: String? = null,
    var password: String? = null,
    var name: String? = null,
    val profileImageUrl: String? = null
) {

}
