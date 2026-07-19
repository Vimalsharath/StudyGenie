package com.studygenie.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class TimerState {
    IDLE, RUNNING, PAUSED, FINISHED
}

class PomodoroViewModel : ViewModel() {

    private val defaultSessionTime = 25 * 60L // 25 minutes in seconds

    private val _timeLeft = MutableStateFlow(defaultSessionTime)
    val timeLeft: StateFlow<Long> = _timeLeft.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _totalFocusTime = MutableStateFlow(0L)
    val totalFocusTime: StateFlow<Long> = _totalFocusTime.asStateFlow()

    private val _initialTime = MutableStateFlow(defaultSessionTime)
    val initialTime: StateFlow<Long> = _initialTime.asStateFlow()

    private var timerJob: Job? = null

    fun startTimer() {
        if (_timerState.value == TimerState.RUNNING) return
        
        if (_timerState.value == TimerState.FINISHED) {
            resetTimer()
        }

        _timerState.value = TimerState.RUNNING
        timerJob = viewModelScope.launch(Dispatchers.Default) {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.update { it - 1 }
                _totalFocusTime.update { it + 1 }
            }
            _timerState.update { TimerState.FINISHED }
            timerJob = null
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
        _timerState.update { TimerState.PAUSED }
    }

    fun resetTimer() {
        timerJob?.cancel()
        timerJob = null
        _timeLeft.update { _initialTime.value }
        _timerState.update { TimerState.IDLE }
    }

    fun setSessionTime(minutes: Int) {
        val seconds = minutes * 60L
        _initialTime.update { seconds }
        _timeLeft.update { seconds }
        _timerState.update { TimerState.IDLE }
        timerJob?.cancel()
        timerJob = null
    }

    fun formatTime(seconds: Long): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "%02d:%02d".format(minutes, remainingSeconds)
    }
}
