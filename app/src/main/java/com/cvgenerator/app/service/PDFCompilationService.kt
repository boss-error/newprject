package com.cvgenerator.app.service

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class PDFCompilationService(private val context: Context) {
    
    private val client = OkHttpClient()
    private val mediaType = "application/json; charset=utf-8".toMediaType()
    
    // You can use a cloud LaTeX compilation service or set up your own backend
    private val latexCompilationUrl = "https://your-latex-service.com/compile"
    
    suspend fun compileLatexToPDF(latexCode: String): Result<File> = withContext(Dispatchers.IO) {
        try {
            // Option 1: Use cloud service
            val pdfBytes = compileWithCloudService(latexCode)
            
            // Save PDF to local file
            val outputFile = File(context.cacheDir, "generated_cv_\${System.currentTimeMillis()}.pdf")
            outputFile.writeBytes(pdfBytes)
            
            Result.success(outputFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun compileWithCloudService(latexCode: String): ByteArray = withContext(Dispatchers.IO) {
        val json = JSONObject().apply {
            put("latex", latexCode)
            put("format", "pdf")
            put("timeout", 30)
        }
        
        val requestBody = json.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url(latexCompilationUrl)
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()
        
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("LaTeX compilation failed: \${response.code}")
        }
        
        return@withContext response.body?.bytes() ?: throw IOException("Empty response")
    }
    
    // Alternative: Use Overleaf API (if available)
    suspend fun compileWithOverleaf(latexCode: String): Result<File> = withContext(Dispatchers.IO) {
        try {
            // This would require Overleaf API integration
            // For now, we'll use a mock implementation
            val mockPdfContent = createMockPDF(latexCode)
            val outputFile = File(context.cacheDir, "generated_cv_\${System.currentTimeMillis()}.pdf")
            outputFile.writeBytes(mockPdfContent)
            
            Result.success(outputFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Mock PDF generation for testing purposes
    private fun createMockPDF(latexCode: String): ByteArray {
        // This is a mock implementation
        // In a real app, you would integrate with a proper LaTeX compilation service
        
        // For demonstration, we'll create a simple text-based "PDF" content
        val mockContent = """
            %PDF-1.4
            1 0 obj
            <<
            /Type /Catalog
            /Pages 2 0 R
            >>
            endobj
            
            2 0 obj
            <<
            /Type /Pages
            /Kids [3 0 R]
            /Count 1
            >>
            endobj
            
            3 0 obj
            <<
            /Type /Page
            /Parent 2 0 R
            /MediaBox [0 0 612 792]
            /Contents 4 0 R
            >>
            endobj
            
            4 0 obj
            <<
            /Length 44
            >>
            stream
            BT
            /F1 12 Tf
            72 720 Td
            (CV Generated from LaTeX) Tj
            ET
            endstream
            endobj
            
            xref
            0 5
            0000000000 65535 f 
            trailer
            <<
            /Size 5
            /Root 1 0 R
            >>
            startxref
            299
            %%EOF
        """.trimIndent()
        
        return mockContent.toByteArray()
    }
    
    // Alternative backend service implementation
    suspend fun compileWithCustomBackend(latexCode: String): Result<File> = withContext(Dispatchers.IO) {
        try {
            // This would call your custom Node.js or Python backend
            val json = JSONObject().apply {
                put("latex_code", latexCode)
                put("engine", "pdflatex")
                put("output_format", "pdf")
            }
            
            val requestBody = json.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url("https://your-backend.com/api/compile-latex")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer YOUR_API_KEY")
                .post(requestBody)
                .build()
            
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw IOException("Backend compilation failed: \${response.code}")
            }
            
            val responseJson = JSONObject(response.body?.string() ?: "")
            val pdfBase64 = responseJson.getString("pdf_content")
            val pdfBytes = android.util.Base64.decode(pdfBase64, android.util.Base64.DEFAULT)
            
            val outputFile = File(context.cacheDir, "generated_cv_\${System.currentTimeMillis()}.pdf")
            outputFile.writeBytes(pdfBytes)
            
            Result.success(outputFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Utility function to clean up temporary files
    fun cleanupTempFiles() {
        val cacheDir = context.cacheDir
        cacheDir.listFiles()?.forEach { file ->
            if (file.name.startsWith("generated_cv_") && file.name.endsWith(".pdf")) {
                val ageInMillis = System.currentTimeMillis() - file.lastModified()
                val ageInHours = ageInMillis / (1000 * 60 * 60)
                
                // Delete files older than 24 hours
                if (ageInHours > 24) {
                    file.delete()
                }
            }
        }
    }
}
