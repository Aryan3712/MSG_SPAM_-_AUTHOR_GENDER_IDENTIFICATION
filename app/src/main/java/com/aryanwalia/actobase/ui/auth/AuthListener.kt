package com.aryanwalia.actobase.ui.auth

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message:String)
    fun onBackPressed()
}