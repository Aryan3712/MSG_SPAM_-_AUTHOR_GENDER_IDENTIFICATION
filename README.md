# Message SPAM & Author Gender Identification in an Android chat application

This repository contains the android project, which is a chatting application in which the messages are labelled as SPAM/NOT SPAM and MALE/FEMALE if a message is a spam or not spam
and from which gender it is.

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
* This Project is able to label a message as SPAM/NOT SPAM & the author's gender
* the android app is able to create new users, which are all present on one server (firebase)
* a user can text anyone
* messages sent by a user are labelled at the receiver's side
* messages from the app are taken to the model and the prediction is fetched through an API (using volley)
* uses naive bayes algorithm
	
## Technologies
Project is created with:
* kotlin
* APIS created using FLASK
	
