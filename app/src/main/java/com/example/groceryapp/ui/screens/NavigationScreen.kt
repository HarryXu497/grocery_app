package com.example.groceryapp.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.groceryapp.routes.Routes
import com.example.groceryapp.routes.TopLevelRoute
import com.example.groceryapp.state.auth.AuthViewModel
import com.example.groceryapp.ui.screens.auth.RegisterScreenScaffold
import com.example.groceryapp.ui.screens.auth.SignInScreenScaffold
import com.example.groceryapp.ui.screens.content.BrowseScreenScaffold
import com.example.groceryapp.ui.screens.content.BrowseScreenTopBar
import com.example.groceryapp.ui.screens.content.CartScreenScaffold
import com.example.groceryapp.ui.screens.content.CartScreenTopBar
import com.example.groceryapp.ui.screens.content.ItemScreenScaffold
import com.example.groceryapp.ui.screens.content.ProfileScreenScaffold
import com.example.groceryapp.ui.screens.content.SearchScreenScaffold
import com.example.groceryapp.ui.screens.content.SearchScreenTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
val topLevelRoutes = listOf(
    TopLevelRoute(
        "Browse",
        Routes.Content.Browse,
        Icons.Outlined.Home,
        topAppBar = { onViewProfile, scrollBehavior ->
            BrowseScreenTopBar(
                onViewProfile = onViewProfile,
                scrollBehavior = scrollBehavior
            )
        },
    ),
    TopLevelRoute(
        "Search",
        Routes.Content.Search,
        Icons.Outlined.Search,
        topAppBar = { onViewProfile, scrollBehavior ->
            SearchScreenTopBar(
                onViewProfile = onViewProfile,
                scrollBehavior = scrollBehavior
            )
        },
    ),
    TopLevelRoute(
        "Cart",
        Routes.Content.Cart,
        Icons.Outlined.ShoppingCart,
        topAppBar = { onViewProfile, scrollBehavior ->
            CartScreenTopBar(
                onViewProfile = onViewProfile,
                scrollBehavior = scrollBehavior
            )
        },
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RestrictedApi")
@Composable
fun NavigationScreen(
    authViewModel: AuthViewModel = viewModel(),
) {
    val currentUser by authViewModel.authState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val bottomBar: @Composable () -> Unit = { BottomBar(navController) }
    val onTopBarViewProfile: () -> Unit = { navController.navigate(Routes.Content.Profile) }

    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) Routes.Auth else Routes.Content,
        enterTransition = {
            fadeIn(animationSpec = tween(300),)
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        }
    ) {
        navigation<Routes.Auth>(startDestination = Routes.Auth.SignIn) {
            composable<Routes.Auth.SignIn> {
                SignInScreenScaffold(
                    onSubmit = { email, password ->
                        coroutineScope.launch {
                            authViewModel.signIn(email, password)
                            navController.navigate(Routes.Content)
                        }
                    },
                    onAlternate = { navController.navigate(Routes.Auth.Register) }
                )
            }
            composable<Routes.Auth.Register> {
                RegisterScreenScaffold(
                    onSubmit = { name, email, password ->
                        coroutineScope.launch {
                            authViewModel.createUser(name, email, password)
                            navController.navigate(Routes.Content)
                        }
                    },
                    onAlternate = { navController.navigate(Routes.Auth.SignIn) }
                )
            }
        }
        navigation<Routes.Content>(startDestination = Routes.Content.Browse) {
            composable<Routes.Content.Browse> {
                BrowseScreenScaffold(
                    onItemClick = { model -> navController.navigate(Routes.Content.Item(model.id)) },
                    onTopBarViewProfile = onTopBarViewProfile,
                    bottomBar = bottomBar,
                )
            }
            composable<Routes.Content.Search> {
                SearchScreenScaffold(
                    onItemClick = { model -> navController.navigate(Routes.Content.Item(model.id)) },
                    onTopBarViewProfile = onTopBarViewProfile,
                    bottomBar = bottomBar
                )
            }
            composable<Routes.Content.Item> { navBackStackEntry ->
                val itemRoute = navBackStackEntry.toRoute<Routes.Content.Item>()

                ItemScreenScaffold(
                    itemId = itemRoute.id,
                    onNavigateBack = { navController.popBackStack() },
                    bottomBar = bottomBar
                )
            }
            composable<Routes.Content.Cart> {
                CartScreenScaffold(
                    onTopBarViewProfile = onTopBarViewProfile,
                    bottomBar = bottomBar,
                )
            }
            composable<Routes.Content.Profile> {
                ProfileScreenScaffold(
                    onNavigateBack = { navController.popBackStack() },
                    onSignOut = { navController.navigate(Routes.Auth) },
                    bottomBar = bottomBar,
                )
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (currentDestination?.route?.startsWith(Routes.Content::class.qualifiedName.toString()) == true) {
        Log.i("CHEESE", currentDestination.hierarchy.map { it.route }.toList().toString())
        NavigationBar {
            topLevelRoutes.forEach { topLevelRoute ->
            Log.i("CHEESE", (currentDestination.hierarchy.any {
                Log.i("CHEESE BALL", "${it.route} == ${topLevelRoute.route::class.qualifiedName.toString()}")
                it.route == topLevelRoute.route::class.qualifiedName.toString()
            }).toString())

                NavigationBarItem(
                    icon = {
                        Icon(
                            topLevelRoute.icon,
                            contentDescription = topLevelRoute.name
                        )
                    },
                    label = {
                        Text(
                            topLevelRoute.name,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    selected = currentDestination.hierarchy.any {
                        it.route == topLevelRoute.route::class.qualifiedName.toString()
                    },
                    onClick = {
                        navController.navigate(topLevelRoute.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(topLevelRoute.route)
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
