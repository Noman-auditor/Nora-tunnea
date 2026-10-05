package com.nora.tunnel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.nora.tunnel.ui.screens.home.HomeScreen
import com.nora.tunnel.ui.screens.profiles.ProfileScreen
import com.nora.tunnel.ui.screens.routing.RoutingStudio
import com.nora.tunnel.ui.screens.lab.NetworkLab
import com.nora.tunnel.ui.screens.logs.LogCenter
import com.nora.tunnel.ui.screens.security.SecurityCenter

@Composable
fun NoraNavGraph() {
    val nav = rememberNavController()
    NavHost(nav, startDestination = "home") {
        composable("home") { HomeScreen() }
        composable("profiles") { ProfileScreen() }
        composable("routing") { RoutingStudio() }
        composable("lab") { NetworkLab() }
        composable("logs") { LogCenter() }
        composable("security") { SecurityCenter() }
    }
}
