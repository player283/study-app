package com.example.samapp.user

import com.google.firebase.auth.FirebaseAuth

class UserAuthUtils {


    companion object {

        lateinit var auth : FirebaseAuth
        fun getUid() : String{

            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }

    }
}
