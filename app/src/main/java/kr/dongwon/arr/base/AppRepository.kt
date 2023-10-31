package kr.dongwon.arr.base

import android.content.SharedPreferences
import javax.inject.Inject

interface AppRepository {

    suspend fun loadBooleanData(key: String): Boolean
    suspend fun loadStringData(key: String): String
    suspend fun saveData(key: String, value: Boolean)
    suspend fun saveData(key: String, value: String)
}

class AppRepositoryImpl @Inject constructor(
    private val sharedPreference: SharedPreferences,
    private val api: AppApi,
) : AppRepository {

    override suspend fun loadBooleanData(key: String): Boolean {
        return try { sharedPreference.getBoolean(key, false) } catch (_: Exception) { false }
    }

    override suspend fun loadStringData(key: String): String {
        return try { sharedPreference.getString(key, "") ?: "" } catch (_: Exception) { "" }
    }

    override suspend fun saveData(key: String, value: Boolean) {
        with(sharedPreference.edit()) { putBoolean(key, value); apply(); }
    }

    override suspend fun saveData(key: String, value: String) {
        with(sharedPreference.edit()) { putString(key, value); apply(); }
    }
}