package com.aryanwalia.actobase.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.aryanwalia.actobase.R
import com.aryanwalia.actobase.databinding.ActivityLoginBinding
import com.aryanwalia.actobase.databinding.ActivitySignUpBinding
import com.aryanwalia.actobase.util.toast

class SignUpActivity : AppCompatActivity(), AuthListener{

    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataBinding : ActivitySignUpBinding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        val viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        dataBinding.viewmodel = viewModel
        viewModel.authListener = this

        progressBar = dataBinding.progressBar

    }

    override fun onStarted() {
        val visible = if(progressBar.visibility == View.GONE)
            View.VISIBLE
        else
            View.GONE
        progressBar.visibility = visible
        toast("Sign Up Started")
    }

    override fun onSuccess() {
        val visible = if(progressBar.visibility == View.GONE)
            View.VISIBLE
        else
            View.GONE
        progressBar.visibility = visible
        toast("Sign Up Success")
    }

    override fun onFailure(message: String) {
        val visible = if(progressBar.visibility == View.VISIBLE)
            View.GONE
        else
            View.GONE
        progressBar.visibility = visible
        toast(message)
    }
}