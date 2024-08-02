package com.example.gptsolo
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferencesHelper {

    private const val PREFS_NAME = "daily_planner_prefs"
    private const val NOTES_KEY = "notes"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveNotes(context: Context, noteList: List<Note>) {
        val gson = Gson()
        val notesJson = gson.toJson(noteList)
        getPreferences(context).edit().putString(NOTES_KEY, notesJson).apply()
    }

    fun getNotes(context: Context): List<Note> {
        val notesJson = getPreferences(context).getString(NOTES_KEY, null)
        val type = object : TypeToken<List<Note>>() {}.type
        return if (notesJson.isNullOrEmpty()) {
            emptyList()
        } else {
            Gson().fromJson(notesJson, type)
        }
    }
}
