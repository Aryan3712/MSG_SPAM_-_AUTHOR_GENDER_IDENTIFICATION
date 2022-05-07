package com.aryanwalia.actobase.ui.auth

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


//this same viewmodel will be used for both login and sign up activity
class AuthViewModel : ViewModel(){
    var user_name : String? = null
    var email : String? = null
    var password : String? = null
    var final_password : String? = null
    var authListener : AuthListener? = null
    private var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var mDbRef : DatabaseReference = FirebaseDatabase.getInstance().reference



    fun onLoginButtonClick(view: View){
        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            //failure
            authListener?.onFailure("Invalid email or password")
            return
        }
        //success
        login(email!!, password!!,view)
    }

    private fun login(email: String, password: String,view:View) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authListener?.onSuccess()
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(view.context,HomeActivity::class.java)
                    startActivity(view.context,intent,null)
                    authListener?.onBackPressed()
                } else {
                    // If sign in fails, display a message to the user.
                    authListener?.onFailure("No User Found")

                }
            }
    }

    fun onSignUpOptionSelected(view : View){
        val intent = Intent(view.context,SignUpActivity::class.java)
        startActivity(view.context,intent,null)
        authListener?.onBackPressed()
    }

    fun onSignUpButtonClicked(view:View){
        if(user_name.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || final_password.isNullOrEmpty()){
            //failure
            authListener?.onFailure("Enter username & password")
            return
        }
        //success
        Log.d("check_details","user_name = $user_name")
        signup(user_name!!,email!!,password!!,final_password!!,view)
        authListener?.onSuccess()
    }

    private fun signup(user_name:String,email: String, password: String, final_password:String, view: View) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //from here we'll jump to the home activity
                        addUserToDatabase(user_name,email,password,final_password,
                            mAuth.currentUser?.uid
                        )
                        val intent = Intent(view.context,HomeActivity::class.java)
                        startActivity(view.context,intent,null)
                        authListener?.onBackPressed()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(view.context,"An error occurred",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(userName: String, email: String, password: String, finalPassword: String, uid: String?) {

        if (uid != null) {
            mDbRef.child("user").child(uid).setValue(User(userName,email,uid))
        }
    }


}