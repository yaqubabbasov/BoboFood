package com.yaqubabbasov.bobofood.util

import com.yaqubabbasov.bobofood.R

data class PasswordStrength (val level: Int, val label: String, val color: Int)
    fun getPasswordStrength(password: String): PasswordStrength {
        var score = 0
        if (password.length >= 8) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { it.isUpperCase() } && password.any { it.isLowerCase() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++  // special char

        return when (score) {
            0, 1 -> PasswordStrength(1, "Weak", R.color.red)
            2 -> PasswordStrength(2, "Medium", R.color.orange)
            3 -> PasswordStrength(3, "Strong", R.color.darkgreen)
            else -> PasswordStrength(3, "Very Strong", R.color.green)
        }

    }
