package com.studygenie.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studygenie.app.repository.AiRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val isError: Boolean = false,
    val isQuotaError: Boolean = false // New flag
)

class AiViewModel : ViewModel() {
    private val repository = AiRepository()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun sendMessage(text: String, isRetry: Boolean = false) {
        if (text.isBlank()) return

        if (!isRetry) {
            val userMessage = ChatMessage(text = text, isUser = true)
            _messages.value = _messages.value + userMessage
        }

        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getAiResponse(text)
            
            result.onSuccess { responseText ->
                val aiMessage = ChatMessage(text = responseText, isUser = false)
                _messages.value = _messages.value + aiMessage
            }.onFailure { error ->
                val isQuota = error.message?.contains("Quota") == true
                
                if (isQuota && !isRetry) {
                    // Auto-retry once after a delay
                    delay(2000)
                    sendMessage(text, isRetry = true)
                    return@launch
                }

                val errorMessage = ChatMessage(
                    text = error.message ?: "Failed to get response",
                    isUser = false,
                    isError = true,
                    isQuotaError = isQuota
                )
                _messages.value = _messages.value + errorMessage
            }
            _isLoading.value = false
        }
    }

    fun clearChat() {
        _messages.value = emptyList()
        repository.resetChat()
    }

    fun regenerateLastResponse() {
        val lastUserMessage = _messages.value.lastOrNull { it.isUser }
        if (lastUserMessage != null) {
            // Remove last AI message if it exists and was an error or the very last one
            val currentMessages = _messages.value.toMutableList()
            if (currentMessages.isNotEmpty() && !currentMessages.last().isUser) {
                currentMessages.removeAt(currentMessages.size - 1)
                _messages.value = currentMessages
            }
            sendMessage(lastUserMessage.text)
        }
    }
}
