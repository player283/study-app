package com.example.samapp

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.example.samapp.model.User
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import com.google.firebase.storage.ktx.storage

class MyApplication : MultiDexApplication() {
    companion object {
        lateinit var auth: FirebaseAuth
        var email: String? = null
        lateinit var db: FirebaseFirestore
        lateinit var storage: FirebaseStorage
        var currentUserName: String? = null

        fun checkAuth(): Boolean {
            var currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let {
                false
            }
        }

        fun findUSerName() {
            var user = Firebase.auth.currentUser
            var currentEmail = user?.email.toString()
            MyApplication.db.collection("userInfo")
                .whereEqualTo("email", currentEmail)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val item = document.toObject(User::class.java)
                        MyApplication.currentUserName = item.name
                        Log.d("myLog","${item.name}")
                    }
                }
        }
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage
    }
}
