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
import androidx.lifecycle.ViewModel
import com.example.tla.data.Acronym
import com.example.tla.data.AcronymRepository

/**
 * View Model to validate and insert items in the Room database.
 */
class AcronymEntryViewModel(private val acronymRepository: AcronymRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var acronymUiState by mutableStateOf(AcronymUiState())
        private set

    /**
     * Updates the [acronymUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(acronymDetails: AcronymDetails) {
        acronymUiState =
            AcronymUiState(acronymDetails = acronymDetails, isEntryValid = validateInput(acronymDetails))
    }

    /**
     * Inserts an [Acronym] in the Room database
     */
    suspend fun saveItem() {
        if (validateInput()) {
            acronymRepository.insertItem(acronymUiState.acronymDetails.save())
        }
    }

    private fun validateInput(uiState: AcronymDetails = acronymUiState.acronymDetails): Boolean {
        return with(uiState) {
            ack.isNotBlank() && ack.length == 3
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class AcronymUiState(
    val acronymDetails: AcronymDetails = AcronymDetails(),
    val isEntryValid: Boolean = false
)

data class AcronymDetails(
    val id: Int = 0,
    val ack: String = "",
    val comment: String = "",
    val dataCreated: Long = 0,
    val lastEdited: Long = 0,
)

/**
 * Extension function to convert [AcronymUiState] to [Acronym].
 */
fun AcronymDetails.toItem(): Acronym = Acronym(
    id = id,
    acronym = ack,
    comment = comment,
    dataCreated = dataCreated,
    lastEdited = lastEdited
)

fun AcronymDetails.save(): Acronym = Acronym(
    id = id,
    acronym = ack,
    comment = comment,
    dataCreated = System.currentTimeMillis(),
    lastEdited = System.currentTimeMillis()
)

fun AcronymDetails.update(): Acronym = Acronym(
    id = id,
    acronym = ack,
    comment = comment,
    dataCreated = dataCreated,
    lastEdited = System.currentTimeMillis()
)


/**
 * Extension function to convert [Acronym] to [AcronymUiState]
 */
fun Acronym.toAcronymUiState(isEntryValid: Boolean = false): AcronymUiState = AcronymUiState(
    acronymDetails = this.toAcronymDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Acronym] to [AcronymDetails]
 */
fun Acronym.toAcronymDetails(): AcronymDetails = AcronymDetails(
    id = id,
    ack = acronym,
    comment = comment,
    dataCreated = dataCreated,
    lastEdited = lastEdited,
)
