package com.example.tla.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "acronyms")
data class Acronym(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val acronym: String,
    val comment: String,
    val dataCreated: Long,
    val lastEdited: Long,
)
