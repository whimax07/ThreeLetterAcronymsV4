package uk.co.whitehouse.tla.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
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

    fun createdAsDateString(): String {
        return SimpleDateFormat("dd-MM-yyyy").format(Date(dataCreated))
    }

    fun createdAsTimeString(): String {
        return SimpleDateFormat("HH:mm:ss").format(dataCreated)
    }

}
