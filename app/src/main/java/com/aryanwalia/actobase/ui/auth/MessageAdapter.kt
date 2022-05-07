package com.aryanwalia.actobase.ui.auth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aryanwalia.actobase.R
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(
    val context: Context,
    val messageList: ArrayList<Message>,

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==1){
            // inflate receive
            val view : View = LayoutInflater.from(context).inflate(R.layout.received,parent,false)
            return ReceiveViewHolder(view)
        }else{
            // inflate sent
            val view : View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]


        if(holder.javaClass == SentViewHolder::class.java){
            //work here for sent view holder
                val viewHolder =  holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        }else{
            //do stuff for receive view holder

            val viewHolder =  holder as ReceiveViewHolder
            holder.receivedMessage.text = currentMessage.message
            holder.statusTV.text = currentMessage.status
            holder.genderTV.text = currentMessage.gender


        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else
            return ITEM_RECEIVE
    }

    override fun getItemCount(): Int {
       return messageList.size
    }


    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.sent_message)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val receivedMessage = itemView.findViewById<TextView>(R.id.received_message)
        val statusTV = itemView.findViewById<TextView>(R.id.status_val)
        val genderTV = itemView.findViewById<TextView>(R.id.gender_val)

    }

//    class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val statusTV = itemView.findViewById<TextView>(R.id.status_val)
//    }
//
//    class GenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val genderTV = itemView.findViewById<TextView>(R.id.gender_val)
//    }
}