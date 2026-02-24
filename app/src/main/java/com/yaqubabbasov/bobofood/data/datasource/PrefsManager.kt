package com.yaqubabbasov.bobofood.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("app_prefs")

class PrefsManager(val context: Context) {

    companion object {
        val ONBOARDING_STEP = intPreferencesKey("onboarding_step")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    suspend fun setOnboardingStep(step: Int) {
        context.dataStore.edit {
            it[ONBOARDING_STEP] = step
        }
    }

    suspend fun getOnboardingStep(): Int {
        val pref = context.dataStore.data.first()
        return pref[ONBOARDING_STEP] ?: 1
    }

    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = value
        }
    }

    suspend fun isLoggedIn(): Boolean {
        val pref = context.dataStore.data.first()
        return pref[IS_LOGGED_IN] ?: false
    }
}