package data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class DataStoreRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val TIMESTAMP_KEY = longPreferencesKey(name = "saved_timestamp")
    }

    suspend fun saveTimestamp(timestamp: Long): Boolean =
        try {
            dataStore.edit { preferences ->
                preferences.set(key = TIMESTAMP_KEY, value = timestamp)
            }
            true
        } catch (e: Exception) {
            println("saveTimestamp() Error: $e")
            false
        }

    fun readTimestamp(): Flow<Long> =
        dataStore.data
            .catch { emptyFlow<Long>() }
            .map { preferences ->
                preferences[TIMESTAMP_KEY] ?: 0L
            }
}