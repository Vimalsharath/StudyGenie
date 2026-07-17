package com.studygenie.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class TimerState {
    IDLE, RUNNING, PAUSED, FINISHED
}

class PomodoroViewModel : ViewModel() {

    private val _timeLeft = MutableStateFlow(25 * 60L) // 25 minutes in seconds
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState

    private val _totalFocusTime = MutableStateFlow(0L)
    val totalFocusTime: StateFlow<Long> = _totalFocusTime

    private var timerJob: Job? = null

    fun startTimer() {
        if (_timerState.value == TimerState.RUNNING) return

        _timerState.value = TimerState.RUNNING
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1
                _totalFocusTime.value += 1
            }
            _timerState.value = TimerState.FINISHED
            // Logic for XP rewards can be added here
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.PAUSED
    }

    fun resetTimer() {
        timerJob?.cancel()
        _timeLeft.value = 25 * 60L
        _timerState.value = TimerState.IDLE
    }

    fun formatTime(seconds: Long): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "%02d:%02d".format(minutes, remainingSeconds)
    }
}
