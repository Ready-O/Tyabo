package com.tyabo.persistence.implementation

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tyabo.data.Token
import com.tyabo.persistence.interfaces.SessionDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.sessionPreferences by preferencesDataStore("session")

class SessionPreferences @Inject constructor(
    @ApplicationContext context: Context
) : SessionDataStore {

    private val dataStore = context.sessionPreferences

    override suspend fun getToken(): Result<Token> {
        val preferences = dataStore.data.first()
        val id = preferences[KEY_ID] ?: return Result.failure(Exception())
        val isChef = preferences[KEY_ACCOUNT_CHEF] ?: return Result.failure(Exception())
        return Result.success(Token(id = id, isChef = isChef))
    }

    override suspend fun setToken(token: Token) {
        dataStore.edit {
            it[KEY_ID] = token.id
            it[KEY_ACCOUNT_CHEF] = token.isChef
        }
    }

    override suspend fun clearToken() {
        dataStore.edit { it.clear() }
    }

    companion object {
        val KEY_ID = stringPreferencesKey("id")
        val KEY_ACCOUNT_CHEF = booleanPreferencesKey("is_chef")
    }
}