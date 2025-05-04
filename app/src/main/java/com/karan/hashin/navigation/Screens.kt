package com.karan.hashin.navigation

sealed class Screens(val name : String) {
    object Splash : Screens(name = "splash")
    object Auth : Screens(name = "auth")
    object Home : Screens(name = "home")

}