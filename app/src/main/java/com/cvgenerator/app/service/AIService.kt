package com.cvgenerator.app.service

import com.cvgenerator.app.data.CVData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class AIService {
    private val client = OkHttpClient()
    private val mediaType = "application/json; charset=utf-8".toMediaType()
    
    // Replace with your actual API key
    private val openAiApiKey = "YOUR_OPENAI_API_KEY"
    private val geminiApiKey = "YOUR_GEMINI_API_KEY"
    
    suspend fun enhanceCV(cvData: CVData): Result<String> = withContext(Dispatchers.IO) {
        try {
            val prompt = createCVEnhancementPrompt(cvData)
            val response = callOpenAI(prompt)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun generateLatexFromTemplate(
        enhancedCVData: String,
        templateContent: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val prompt = createLatexGenerationPrompt(enhancedCVData, templateContent)
            val response = callOpenAI(prompt)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun createCVEnhancementPrompt(cvData: CVData): String {
        return """
        Using the following user information, improve and rewrite it to match a strong professional CV tailored to the specific job title.

        INPUT DATA:
        Name: ${cvData.personalInfo.fullName}
        Email: ${cvData.personalInfo.email}
        Phone: ${cvData.personalInfo.phone}
        Summary: ${cvData.summary}
        
        Experience:
        ${cvData.experience.joinToString("\n") { 
            "${it.jobTitle} at ${it.company} (${it.startDate} - ${it.endDate})\n${it.description}"
        }}
        
        Education:
        ${cvData.education.joinToString("\n") { 
            "${it.degree} from ${it.institution} (${it.startDate} - ${it.endDate})"
        }}
        
        Skills:
        Programming Languages: ${cvData.skills.programmingLanguages.joinToString(", ")}
        Frameworks: ${cvData.skills.frameworks.joinToString(", ")}
        Tools: ${cvData.skills.tools.joinToString(", ")}
        
        Projects:
        ${cvData.projects.joinToString("\n") { 
            "${it.name}: ${it.description}"
        }}

        TARGET JOB TITLE: ${cvData.targetJobTitle}

        Please output:
        1. A refined, structured CV content with sections: Summary, Experience, Education, Skills, Projects, etc.
        2. Make the content more professional and impactful
        3. Tailor the content to match the target job title
        4. Use action verbs and quantifiable achievements where possible
        5. Ensure ATS-friendly formatting
        
        Format the response as structured text that can be easily parsed.
        """.trimIndent()
    }
    
    private fun createLatexGenerationPrompt(enhancedCVData: String, templateContent: String): String {
        return """
        OVERLEAF CODE FORMAT:
        $templateContent

        ANALYSE THESE OVERLEAF CODE FORMAT AND GIVE CV DATA IN ABOVE OVERLEAF CODE.
        JUST ANALYSE THE FORMAT – DO NOT USE ANY DATA INSIDE THE FORMAT.

        ENHANCED CV DATA:
        $enhancedCVData

        OUTPUT:
        Generate valid LaTeX code for the CV above using the OVERLEAF template format.
        Replace all placeholder variables (like {{FULL_NAME}}, {{EMAIL}}, etc.) with the actual data from the enhanced CV.
        Ensure proper LaTeX formatting and escaping of special characters.
        Return only the complete LaTeX code, ready for compilation.
        """.trimIndent()
    }
    
    private suspend fun callOpenAI(prompt: String): String = withContext(Dispatchers.IO) {
        val json = JSONObject().apply {
            put("model", "gpt-4")
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            })
            put("max_tokens", 2000)
            put("temperature", 0.7)
        }
        
        val requestBody = json.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $openAiApiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()
        
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }
        
        val responseBody = response.body?.string() ?: throw IOException("Empty response")
        val jsonResponse = JSONObject(responseBody)
        val choices = jsonResponse.getJSONArray("choices")
        val message = choices.getJSONObject(0).getJSONObject("message")
        
        return@withContext message.getString("content")
    }
    
    // Alternative method using Gemini API
    suspend fun enhanceCVWithGemini(cvData: CVData): Result<String> = withContext(Dispatchers.IO) {
        try {
            val prompt = createCVEnhancementPrompt(cvData)
            val response = callGemini(prompt)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun callGemini(prompt: String): String = withContext(Dispatchers.IO) {
        val json = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
        }
        
        val requestBody = json.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=$geminiApiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()
        
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }
        
        val responseBody = response.body?.string() ?: throw IOException("Empty response")
        val jsonResponse = JSONObject(responseBody)
        val candidates = jsonResponse.getJSONArray("candidates")
        val content = candidates.getJSONObject(0).getJSONObject("content")
        val parts = content.getJSONArray("parts")
        
        return@withContext parts.getJSONObject(0).getString("text")
    }
}
