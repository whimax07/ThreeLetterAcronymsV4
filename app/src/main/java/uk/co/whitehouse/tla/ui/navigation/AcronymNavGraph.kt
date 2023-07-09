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

package uk.co.whitehouse.tla.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import uk.co.whitehouse.tla.ui.acronym.AcronymDetailsDestination
import uk.co.whitehouse.tla.ui.acronym.AcronymDetailsScreen
import uk.co.whitehouse.tla.ui.acronym.AcronymEditDestination
import uk.co.whitehouse.tla.ui.acronym.AcronymEditScreen
import uk.co.whitehouse.tla.ui.acronym.AcronymEntryDestination
import uk.co.whitehouse.tla.ui.acronym.AcronymEntryScreen
import uk.co.whitehouse.tla.ui.home.HomeDestination
import uk.co.whitehouse.tla.ui.home.HomeScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun AcronymNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToAcronymEntry = { navController.navigate(AcronymEntryDestination.route) },
                navigateToAcronymUpdate = {
                    navController.navigate("${AcronymDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = AcronymEntryDestination.route) {
            AcronymEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = AcronymDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(AcronymDetailsDestination.acronymIdArg) {
                type = NavType.IntType
            })
        ) {
            AcronymDetailsScreen(
                navigateToEditItem = { navController.navigate("${AcronymEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = AcronymEditDestination.routeWithArgs,
            arguments = listOf(navArgument(AcronymEditDestination.acronymIdArg) {
                type = NavType.IntType
            })
        ) {
            AcronymEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
