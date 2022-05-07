package com.aryanwalia.actobase.ui.auth


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.aryanwalia.actobase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.Map as Map

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView : RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var send_button:ImageButton

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList : ArrayList<Message>
    private lateinit var statusList : ArrayList<String>
    private lateinit var genderList : ArrayList<String>

    private lateinit var mDbRef : DatabaseReference

    var receiverRoom : String? = null
    var senderRoom : String? = null

    var spam_api_url : String =  "https://spam-sms-api-aryan.herokuapp.com/predict"
    var author_gender_api_url : String = "https://author-gender-api-aryan.herokuapp.com/predict"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        mDbRef = FirebaseDatabase.getInstance().reference
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.edit_message)
        send_button = findViewById(R.id.send_button)
        
        messageList = ArrayList()
        statusList = ArrayList()
        genderList = ArrayList()

        messageAdapter = MessageAdapter(this,messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //adding the chats data to the recycler view
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        //adding the message to the database
        send_button.setOnClickListener {
            val message = messageBox.text.toString().trim()
            messageBox.setText("")
            
            // now we'll hit the APIs to fetch the message's status and the gender of the author
            // we'll send those values to the receiver's side
            // which will then be displayed above the message

            getMessageStatus(message,senderUid)

        }
    }

    private fun getMessageStatus(message: String, senderUid: String?) {
        var status = ""
        val stringRequest: StringRequest
        stringRequest = object : StringRequest( Method.POST, spam_api_url,
            Response.Listener { response ->
                    val jsonObject = JSONObject(response)
                    var status_s = jsonObject.getString("status")
                    try{
                        if(status_s=="1"){
                            status = "SPAM"
                            getMessageGender(message,senderUid,status)
                        }else{
                            status = "NOT SPAM"
                            getMessageGender(message,senderUid,status)
                        }
                    }catch(e:JSONException){
                        e.printStackTrace()
                    }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["msg"] = message
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)

    }



    private fun getMessageGender(message: String, senderUid: String?,status:String){
        var gender = ""
        val stringRequest_author: StringRequest
        stringRequest_author = object : StringRequest( Method.POST, author_gender_api_url,
            Response.Listener { response ->
                    val jsonObject = JSONObject(response)
                    var gender_g = jsonObject.getString("status")
                    try{
                        if(gender_g=="1"){
                            gender = "MALE"
                            takeGenderVal(gender,message,senderUid,status)
                        }else{
                            gender = "FEMALE"
                            takeGenderVal(gender,message,senderUid,status)
                        }
                    }catch (e:JSONException){
                        e.printStackTrace()
                    }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["msg"] = message
                return params
            }
        }
        val requestQueueAuthor = Volley.newRequestQueue(this)
        requestQueueAuthor.add(stringRequest_author)

    }


    private fun takeGenderVal(gen:String,message:String,senderUid: String?,status: String) {
       val messageObject = Message()
        messageObject.message = message
        messageObject.senderId = senderUid
        messageObject.gender = gen
        messageObject.status = status

        mDbRef.child("chats").child(senderRoom!!).child("messages").push()
            .setValue(messageObject).addOnSuccessListener {
                mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                    .setValue(messageObject)
            }
    }

}