package com.karan.hashin.navigation

sealed class Screens(open val route: String) {
    object Splash : Screens(route = "splash")
    object Auth : Screens(route = "auth")
    object Home : Screens(route = "home")

    sealed class HomeGraph(override val route: String) : Screens(route) {
        object Vault : HomeGraph(route = "home/vault")
        object Passkey : HomeGraph(route = "home/add")
        object Setting : HomeGraph(route = "home/settings")
        object Search : HomeGraph(route = "home/search")
        object Detail : HomeGraph(route = "home/detail/{passKeyId}")
    }
}