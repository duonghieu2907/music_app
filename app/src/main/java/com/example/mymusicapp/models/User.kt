package com.example.mymusicapp.models

data class User(
    val userId: String,
    val name: String,
    val email: String,
    val password: String,
    val dateOfBirth: String,
    val profileImage: String // Image stored as URL
)
