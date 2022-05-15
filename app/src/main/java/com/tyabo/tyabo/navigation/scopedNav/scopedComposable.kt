package com.tyabo.tyabo.navigation.scopedNav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.tyabo.tyabo.navigation.Destination
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent.getKoin


/* Inspired by https://github.com/lotdrops/Composing-Clocks */

inline fun <reified T : Destination> NavGraphBuilder.scopedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    crossinline content: @Composable (NavBackStackEntry, Scope) -> Unit,
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks= deepLinks,
    ) { navEntry ->
        val koinScope = getKoin().getOrCreateScope<T>(checkNotNull(navEntry.destination.route))
        koinScope.registerCallback(ClearableScopeCallback())

        RunOnce {
            ScopeLifecycleHandler().bind(koinScope, navEntry.lifecycle)
        }

        content(navEntry, koinScope)
    }
}

/**
 * Hack so that an effect can be run only once and not on each composition.
 */
@Composable
fun RunOnce(composable: () -> Unit) {
    rememberSaveable {
        composable()
        true
    }
}