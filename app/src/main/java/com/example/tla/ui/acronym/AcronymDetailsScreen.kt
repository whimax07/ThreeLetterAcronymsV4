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

import androidx.annotation.StringRes
import androidx.compose.foundation.checkScrollableContainerConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tla.R
import com.example.tla.AcronymTopAppBar
import com.example.tla.data.Acronym
import com.example.tla.ui.AppViewModelProvider
import com.example.tla.ui.navigation.NavigationDestination
import com.example.tla.ui.theme.InventoryTheme
import kotlinx.coroutines.launch

object AcronymDetailsDestination : NavigationDestination {
    override val route = "acronym_details"
    override val titleRes = R.string.acronym_detail_title
    const val acronymIdArg = "acronymId"
    val routeWithArgs = "$route/{$acronymIdArg}"
}

@Composable
fun AcronymDetailsScreen(
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AcronymDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(topBar = {
        AcronymTopAppBar(
            title = stringResource(AcronymDetailsDestination.titleRes),
            canNavigateBack = true,
            navigateUp = navigateBack
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = { navigateToEditItem(uiState.value.acronymDetails.id) },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))

        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit_item_title),
            )
        }
    }, modifier = modifier
    ) { innerPadding ->
        AcronymDetailsBody(
            acronymDetailsUiState = uiState.value,
            onDelete = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be deleted from the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.deleteItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun AcronymDetailsBody(
    acronymDetailsUiState: AcronymDetailsUiState,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        AcronymDetails(
            acronym = acronymDetailsUiState.acronymDetails.toItem(), modifier = Modifier.fillMaxWidth()
        )
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(onDeleteConfirm = {
                deleteConfirmationRequired = false
                onDelete()
            },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}


@Composable
fun AcronymDetails(
    acronym: Acronym, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            ItemDetailsRow(
                labelResID = R.string.acronym,
                itemDetail = acronym.acronym,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen
                    .padding_medium))
            )
            ItemDetailsRow(
                labelResID = R.string.time_created,
                itemDetail = acronym.createdAsTimeString() + ", " + acronym.createdAsDateString(),
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen
                    .padding_medium))
            )
            ItemDetailsRow(
                labelResID = R.string.last_edited,
                itemDetail = acronym.lastEditedAsTimeString() + ", " + acronym.lastEditedAsDateString(),
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            )
        }

    }
    Card {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        ) {
            Text(stringResource(id = R.string.comment))
            Text(text = acronym.comment, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ItemDetailsRow(
    @StringRes labelResID: Int, itemDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = itemDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        })
}

@Preview(showBackground = true)
@Composable
fun AcronymDetailsScreenPreview() {
    InventoryTheme {
        AcronymDetailsBody(
            AcronymDetailsUiState(
            acronymDetails = AcronymDetails(1, "Pen", "$100", 1688905705000, 1688905715000)
        ), onDelete = {})
    }
}

@Preview(showBackground = true)
@Composable
fun AcronymDetailsScreenLongCommentPreview() {
    InventoryTheme {
        AcronymDetailsBody(
            AcronymDetailsUiState(
                acronymDetails = AcronymDetails(1, "Pen", "The Unix epoch (or Unix time or POSIX time or Unix timestamp) is the number of seconds that have elapsed since January 1, 1970 (midnight UTC/GMT), not counting leap seconds (in ISO 8601: 1970-01-01T00:00:00Z). Literally speaking the epoch is Unix time 0 (midnight 1/1/1970), but 'epoch' is often used as a synonym for Unix time.\n Some systems store epoch dates as a signed 32-bit integer, which might cause problems on January 19, 2038 (known as the Year 2038 problem or Y2038). The converter on this page converts timestamps in seconds (10-digit), milliseconds (13-digit) and microseconds (16-digit) to readable dates.\n\n Please note: All tools on this page are based on the date & time settings of your computer and use JavaScript to convert times. Some browsers use the current DST (Daylight Saving Time) rules for all dates in history. JavaScript does not support leap seconds.",
                    1688905705000, 1688905715000)
            ), onDelete = {})
    }
}
