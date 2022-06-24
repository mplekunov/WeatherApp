package com.application.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Duration.Companion.seconds

class DateTimeViewModel : ViewModel() {
    private val _liveDate = MutableLiveData<String>()
    private val _liveTime = MutableLiveData<String>()

    val date: LiveData<String> = _liveDate
    val time: LiveData<String> = _liveTime

    init {
        updateCurrentTime()
    }

    private fun updateCurrentTime() {
        viewModelScope.launch {
            while (true) {
                Log.d("ViewModels", "DateTimeViewModel")
                val localDateTime = LocalDateTime.now()

                _liveDate.value = localDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
                _liveTime.value = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

                delay(5.seconds)
            }
        }
    }
}