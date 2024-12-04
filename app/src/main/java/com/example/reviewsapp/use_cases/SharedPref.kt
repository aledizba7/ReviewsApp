package com.example.reviewsapp.use_cases

import android.content.Context

class SharedPref(
    context : Context
) {
    private val sharedPref = context.getSharedPreferences("myPref",Context.MODE_PRIVATE)

    fun saveUserSharedPref(
        userId : Int,
        isLogged : Boolean
    ){
        val editor = sharedPref.edit()
        editor.putInt("userId",userId)
        editor.putBoolean("isLogged",isLogged)
        editor.apply()
    }

    fun getUserIdSharedPref() : Int {
        return sharedPref.getInt("userId",0)
    }

    fun getUserDetails(): Pair<Int, Boolean> {
        val userId = sharedPref.getInt("userId", 0)
        val isLogged = sharedPref.getBoolean("isLogged", false)
        return Pair(userId, isLogged)
    }

    fun getIsLoggedSharedPref() : Boolean{
        return sharedPref.getBoolean("isLogged",false)
    }

    fun removeUserSharedPref(){
        val editor = sharedPref.edit()
        editor.remove("userId")
        editor.remove("isLogged")
        editor.apply()
    }

}