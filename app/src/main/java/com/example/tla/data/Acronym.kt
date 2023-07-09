package com.example.tla.data

import android.content.Context
import androidx.compose.material3.Text
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tla.R
import java.sql.Date
import java.text.DateFormat
import java.text.SimpleDateFormat

@Entity(tableName = "acronyms")
data class Acronym(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val acronym: String,
    val comment: String,
    val dataCreated: Long,
    val lastEdited: Long,
) {

    fun lastEditedAsDateString(): String {
        return SimpleDateFormat("dd-MM-yyyy").format(Date(lastEdited))
    }

    fun lastEditedAsTimeString(): String {
        return SimpleDateFormat("HH:mm:ss").format(lastEdited)
    }

}
