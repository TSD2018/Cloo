package com.example.cloo2019

object CurrentUser {
    private var currentUser: String = ""
    private var currentUserID: String = ""

    fun setCurrentUser(user: String, userID: String)
    {
        currentUser = user
        currentUserID = userID
    }

    fun getCurrentUser(): String {return currentUser}

    fun getCurrentUserID(): String {return currentUserID}
}