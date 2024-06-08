package com.example.watchoid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchoid.MainActivity
import com.example.watchoid.dao.SettingsDAO
import com.example.watchoid.entity.Settings
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SettingsViewModel() : ViewModel() {

    private val _settings = MutableLiveData<Settings?>()
    val settings: MutableLiveData<Settings?> get() = _settings

    private var originalSettings: Settings? = null

    fun loadSettings(protocol : String) {
        viewModelScope.launch(IO) {
            val loadedSettings = MainActivity.database.settingsTable().getSettingByProtocol(protocol)
            originalSettings = loadedSettings
            _settings.value = loadedSettings
        }
    }

    fun updateSettings(newSettings: Settings) {
        _settings.value = newSettings
    }

    fun hasUnsavedChanges(): Boolean {
        return _settings.value != originalSettings
    }

    fun revertChanges() {
        _settings.value = originalSettings
    }

    fun saveSettings() {
        viewModelScope.launch(IO) {
            _settings.value?.let {
                MainActivity.database.settingsTable().update(it)
                originalSettings = it
            }
        }
    }
}
