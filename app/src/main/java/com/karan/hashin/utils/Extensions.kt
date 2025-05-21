package com.karan.hashin.utils


private val REG_EMAIL : Regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

fun String?.isValidInput(): Boolean = !this.isNullOrBlank()
fun String.isValidEmail(): Boolean = matches(REG_EMAIL)
