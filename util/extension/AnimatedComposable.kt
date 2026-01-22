package com.itirafapp.android.util.extension

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(400))
    },
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(400))
    },
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(400))
    },
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(400))
    },
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        content = content
    )
}