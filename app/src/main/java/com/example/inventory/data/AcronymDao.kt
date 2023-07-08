package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.inventory.data.Acronym
import kotlinx.coroutines.flow.Flow

@Dao
interface AcronymDao {

    @Query("SELECT * from acronyms ORDER BY acronym ASC")
    fun getAllAcronyms(): Flow<List<Acronym>>

    @Query("SELECT * from acronyms WHERE id = :id")
    fun getItem(id: Int): Flow<Acronym>

    @Query("SELECT * from acronyms WHERE acronym = :acronym")
    fun getItem(acronym: String): Flow<Acronym>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(acronym: Acronym)

    @Update
    suspend fun update(acronym: Acronym)

    @Delete
    suspend fun delete(acronym: Acronym)

}