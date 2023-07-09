/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.whitehouse.tla.ui.acronym

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uk.co.whitehouse.tla.data.AcronymRepository

/**
 * ViewModel to retrieve, update and delete an item from the [ItemsRepository]'s data source.
 */
class AcronymDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val acronymsRepository: AcronymRepository,
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[AcronymDetailsDestination.acronymIdArg])

    /**
     * Holds the acronym details ui state. The data is retrieved from [AcronymRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<AcronymDetailsUiState> =
        acronymsRepository.getItemStream(itemId)
            .filterNotNull()
            .map {
                AcronymDetailsUiState(acronymDetails = it.toAcronymDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = AcronymDetailsUiState()
            )

    /**
     * Deletes the item from the [ItemsRepository]'s data source.
     */
    suspend fun deleteItem() {
        acronymsRepository.deleteItem(uiState.value.acronymDetails.toItem())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for AcronymDetailsScreen
 */
data class AcronymDetailsUiState(
    val acronymDetails: AcronymDetails = AcronymDetails()
)
