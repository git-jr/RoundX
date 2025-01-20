package com.kmp.hango.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun getDataStore(producePath: () -> String): DataStore<Preferences> {
    return PreferenceDataStoreFactory
        .createWithPath(produceFile = { producePath().toPath() })
}


internal const val dataStoreFileName = "datastore.preferences_pb"