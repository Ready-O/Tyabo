package com.tyabo.tyabo.persistence

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.tyabo.tyabo.service.UserDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.sessionPreferences by preferencesDataStore("session")

class SessionPreferences @Inject constructor(
    @ApplicationContext context: Context
) : SessionDataStore {

    private val dataStore = context.sessionPreferences

    override suspend fun getToken(): String {
        val preferences = dataStore.data.first()
        return preferences[KEY_ID] ?: ""
    }

    override suspend fun setToken(name: String) {
        dataStore.edit {
            it[KEY_ID] = name
        }
    }

    companion object {
        val KEY_ID = stringPreferencesKey("id")
    }
}