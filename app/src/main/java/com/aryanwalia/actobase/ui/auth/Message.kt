package com.aryanwalia.actobase.ui.auth

class Message {

    var message : String? = null
    var senderId : String? = null
    var status : String = ""
    var gender : String = ""

    constructor(){}

    constructor(message: String?, senderId:String?, status:String, gender:String){
        this.message = message
        this.senderId = senderId
        this.status = status
        this.gender = gender
    }
}