package com.example.noteapp.models

data class User(
    var fullName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val securityQuestion: SecurityQuestion? = null
)

data class SecurityQuestion(
    var question: String? = null,
    val answer : String? = null
)
