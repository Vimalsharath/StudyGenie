package com.studygenie.app.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.studygenie.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository responsible for interacting with the Google AI Studio (Gemini) API.
 * Uses the official Google AI SDK for Kotlin.
 */
class AiRepository {

    private val systemInstruction = content {
        text("""
            You are StudyGenie AI, a premium, high-intelligence academic mentor and personal tutor.
            Your goal is to help students with ANY question they have, whether academic, career-oriented, or general knowledge.
            
            Guidelines:
            1. Formatting: Always use Markdown for your responses. Use # for headings, ## for subheadings, bullet points for lists, and **bold** for emphasis.
            2. Code: When providing code, always use Markdown code blocks (```language ... ```).
            3. Persona: Be encouraging, professional, and clear. If a topic is complex, explain it with simple analogies.
            4. Scope: You can answer any question including programming (Java, Kotlin, Python, etc.), Science, Math, History, Placement guidance, and Productivity tips.
            5. Math: Use clear formatting for mathematical equations.
            
            Always prioritize accuracy and student success.
        """.trimIndent())
    }

    private val config = generationConfig {
        temperature = 0.7f
        topK = 40
        topP = 0.95f
    }

    private val safetySettings = listOf(
        SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
        SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
        SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
        SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
    )

    /**
     * Using Google AI Studio Developer API.
     * Model: gemini-2.0-flash (Experimental/Bleeding Edge)
     */
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = config,
        safetySettings = safetySettings,
        systemInstruction = systemInstruction
    )

    private var chat = generativeModel.startChat()

    /**
     * Sends a message to the Gemini model and returns the response.
     * Handles conversation history automatically via [chat].
     */
    suspend fun getAiResponse(prompt: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = chat.sendMessage(prompt)
            response.text?.let {
                Result.success(it)
            } ?: Result.failure(Exception("AI returned empty response"))
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("401") == true || e.message?.contains("invalid") == true -> 
                    "Invalid API Key. Please check your local.properties configuration."
                e.message?.contains("404") == true -> 
                    "Model not found. gemini-2.0-flash might not be available in your region."
                e.message?.contains("429") == true || e.message?.contains("quota") == true -> 
                    "Quota Exceeded: You've sent too many requests. Please wait a minute."
                e.message?.contains("403") == true -> 
                    "Access Denied: Check your API key permissions in Google AI Studio."
                else -> e.message ?: "Failed to connect to AI service"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    /**
     * Resets the current chat session, clearing history.
     */
    fun resetChat() {
        chat = generativeModel.startChat()
    }
}
