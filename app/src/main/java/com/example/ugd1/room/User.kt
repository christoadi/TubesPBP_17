package com.example.ugd1.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name : String,
    val number : Int,
    val email : String,
    val gender : String,
    val password : String

)