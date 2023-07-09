package com.example.tla.data

import kotlinx.coroutines.flow.Flow

interface AcronymRepository {

    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<Acronym>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItemStream(id: Int): Flow<Acronym?>

    /**
     * Retrieve an item from the given data source that matches with the [acronym].
     */
    fun getItemStream(acronym: String): Flow<Acronym?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(acronym: Acronym)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(acronym: Acronym)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(acronym: Acronym)

}