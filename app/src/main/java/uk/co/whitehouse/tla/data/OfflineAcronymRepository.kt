package uk.co.whitehouse.tla.data

import kotlinx.coroutines.flow.Flow

class OfflineAcronymRepository(private val acronymDao: AcronymDao) : AcronymRepository {

    override fun getAllItemsStream(): Flow<List<Acronym>> = acronymDao.getAllAcronyms()

    override fun getItemStream(id: Int): Flow<Acronym?> = acronymDao.getItem(id)

    override fun getItemStream(acronym: String): Flow<Acronym?> = acronymDao.getItem(acronym)

    override suspend fun insertItem(acronym: Acronym) = acronymDao.insert(acronym)

    override suspend fun deleteItem(acronym: Acronym) = acronymDao.delete(acronym)

    override suspend fun updateItem(acronym: Acronym) = acronymDao.update(acronym)

}