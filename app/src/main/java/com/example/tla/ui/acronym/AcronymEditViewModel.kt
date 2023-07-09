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

package com.example.tla.ui.acronym

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tla.data.AcronymRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update an item from the [ItemsRepository]'s data source.
 */
class AcronymEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val acronymRepository: AcronymRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var acronymUiState by mutableStateOf(AcronymUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[AcronymEditDestination.acronymIdArg])

    init {
        viewModelScope.launch {
            acronymUiState = acronymRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toAcronymUiState(true)
        }
    }

    /**
     * Update the item in the [ItemsRepository]'s data source
     */
    suspend fun updateItem() {
        if (validateInput(acronymUiState.acronymDetails)) {
            acronymRepository.updateItem(acronymUiState.acronymDetails.toItem())
        }
    }

    /**
     * Updates the [acronymUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(acronymDetails: AcronymDetails) {
        acronymUiState =
            AcronymUiState(acronymDetails = acronymDetails, isEntryValid = validateInput(acronymDetails))
    }

    private fun validateInput(uiState: AcronymDetails = acronymUiState.acronymDetails): Boolean {
        return with(uiState) {
            ack.isNotBlank() && ack.length == 3
        }
    }
}
