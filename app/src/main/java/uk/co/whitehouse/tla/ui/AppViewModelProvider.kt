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

package uk.co.whitehouse.tla.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import uk.co.whitehouse.tla.AcronymApplication
import uk.co.whitehouse.tla.ui.acronym.AcronymDetailsViewModel
import uk.co.whitehouse.tla.ui.acronym.AcronymEditViewModel
import uk.co.whitehouse.tla.ui.acronym.AcronymEntryViewModel
import uk.co.whitehouse.tla.ui.home.HomeViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for AcronymEditViewModel
        initializer {
            AcronymEditViewModel(
                this.createSavedStateHandle(),
                acronymApplication().container.acronymRepository
            )
        }
        // Initializer for AcronymEntryViewModel
        initializer {
            AcronymEntryViewModel(acronymApplication().container.acronymRepository)
        }

        // Initializer for AcronymDetailsViewModel
        initializer {
            AcronymDetailsViewModel(
                this.createSavedStateHandle(),
                acronymApplication().container.acronymRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(acronymApplication().container.acronymRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.acronymApplication(): AcronymApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as AcronymApplication)
