package com.cvgenerator.app.service

import android.content.Context
import android.net.Uri
import com.cvgenerator.app.data.CVData
import com.cvgenerator.app.data.PersonalInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.extractor.WordExtractor
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor
import java.io.InputStream

class FileParsingService(private val context: Context) {
    
    suspend fun parseFile(uri: Uri): Result<CVData> = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return@withContext Result.failure(Exception("Cannot open file"))
            
            val mimeType = context.contentResolver.getType(uri)
            val fileName = getFileName(uri)
            
            val extractedText = when {
                mimeType?.contains("text") == true -> parseText(inputStream)
                mimeType?.contains("pdf") == true || fileName?.endsWith(".pdf", true) == true -> parsePDF(inputStream)
                mimeType?.contains("wordprocessingml") == true || fileName?.endsWith(".docx", true) == true -> parseDOCX(inputStream)
                mimeType?.contains("msword") == true || fileName?.endsWith(".doc", true) == true -> parseDOC(inputStream)
                else -> throw Exception("Unsupported file format. Supported formats: PDF, DOC, DOCX, TXT")
            }
            
            val cvData = extractCVDataFromText(extractedText)
            Result.success(cvData)
        } catch (e: Exception) {
            Result.failure(Exception("Error parsing file: ${e.message}"))
        }
    }
    
    private fun getFileName(uri: Uri): String? {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        }
    }
    
    private fun parsePDF(inputStream: InputStream): String {
        return try {
            val pdfReader = PdfReader(inputStream)
            val pdfDocument = PdfDocument(pdfReader)
            val text = StringBuilder()
            
            for (i in 1..pdfDocument.numberOfPages) {
                text.append(PdfTextExtractor.extractText(pdfDocument.getPage(i)))
                text.append("
")
            }
            
            pdfDocument.close()
            text.toString()
        } catch (e: Exception) {
            throw Exception("Error reading PDF: ${e.message}")
        }
    }
    
    private fun parseDOCX(inputStream: InputStream): String {
        return try {
            val document = XWPFDocument(inputStream)
            val text = StringBuilder()
            
            document.paragraphs.forEach { paragraph ->
                text.append(paragraph.text)
                text.append("
")
            }
            
            document.close()
            text.toString()
        } catch (e: Exception) {
            throw Exception("Error reading DOCX: ${e.message}")
        }
    }
    
    private fun parseDOC(inputStream: InputStream): String {
        return try {
            val document = HWPFDocument(inputStream)
            val extractor = WordExtractor(document)
            val text = extractor.text
            extractor.close()
            document.close()
            text
        } catch (e: Exception) {
            throw Exception("Error reading DOC: ${e.message}")
        }
    }
    

    private fun parseText(inputStream: InputStream): String {
        return inputStream.bufferedReader().use { it.readText() }
    }
    
    private fun extractCVDataFromText(text: String): CVData {
        val lines = text.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        
        // Basic extraction logic - this can be enhanced with more sophisticated parsing
        val personalInfo = extractPersonalInfo(text)
        val summary = extractSection(text, listOf("summary", "profile", "objective"))
        val experience = extractExperienceSection(text)
        val education = extractEducationSection(text)
        val skills = extractSkillsSection(text)
        val projects = extractProjectsSection(text)
        
        return CVData(
            personalInfo = personalInfo,
            summary = summary,
            experience = experience,
            education = education,
            skills = skills,
            projects = projects
        )
    }
    
    private fun extractPersonalInfo(text: String): PersonalInfo {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.][a-zA-Z]{2,}")
        val phoneRegex = Regex("[+]?[0-9]{1,3}[-. ]?[(]?[0-9]{3}[)]?[-. ]?[0-9]{3}[-. ]?[0-9]{4}")
        
        val email = emailRegex.find(text)?.value ?: ""
        val phone = phoneRegex.find(text)?.value ?: ""
        
        // Extract name (usually the first line or largest text)
        val lines = text.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        val name = lines.firstOrNull { line ->
            line.length > 5 && 
            !line.contains("@") && 
            !phoneRegex.containsMatchIn(line) &&
            line.split(" ").size <= 4
        } ?: ""
        
        return PersonalInfo(
            fullName = name,
            firstName = name.split(" ").firstOrNull() ?: "",
            lastName = name.split(" ").lastOrNull() ?: "",
            email = email,
            phone = phone
        )
    }
    
    private fun extractSection(text: String, keywords: List<String>): String {
        val lines = text.split("\n")
        var startIndex = -1
        var endIndex = lines.size
        
        // Find section start
        for (i in lines.indices) {
            val line = lines[i].lowercase()
            if (keywords.any { keyword -> line.contains(keyword) }) {
                startIndex = i + 1
                break
            }
        }
        
        if (startIndex == -1) return ""
        
        // Find section end (next section or end of document)
        val sectionKeywords = listOf("experience", "education", "skills", "projects", "work", "employment")
        for (i in startIndex until lines.size) {
            val line = lines[i].lowercase()
            if (sectionKeywords.any { keyword -> line.contains(keyword) && !keywords.contains(keyword) }) {
                endIndex = i
                break
            }
        }
        
        return lines.subList(startIndex, endIndex)
            .joinToString(" ")
            .trim()
    }
    
    private fun extractExperienceSection(text: String): List<com.cvgenerator.app.data.Experience> {
        // This is a simplified extraction - can be enhanced with more sophisticated parsing
        val experienceText = extractSection(text, listOf("experience", "work", "employment"))
        if (experienceText.isEmpty()) return emptyList()
        
        // Basic parsing - split by common patterns
        val experiences = mutableListOf<com.cvgenerator.app.data.Experience>()
        val sections = experienceText.split("\n\n".toRegex())
        
        sections.forEach { section ->
            if (section.trim().isNotEmpty()) {
                val lines = section.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
                if (lines.isNotEmpty()) {
                    experiences.add(
                        com.cvgenerator.app.data.Experience(
                            jobTitle = lines.getOrNull(0) ?: "",
                            company = lines.getOrNull(1) ?: "",
                            description = lines.drop(2).joinToString(" ")
                        )
                    )
                }
            }
        }
        
        return experiences
    }
    
    private fun extractEducationSection(text: String): List<com.cvgenerator.app.data.Education> {
        val educationText = extractSection(text, listOf("education", "academic", "qualification"))
        if (educationText.isEmpty()) return emptyList()
        
        val educations = mutableListOf<com.cvgenerator.app.data.Education>()
        val sections = educationText.split("\n\n".toRegex())
        
        sections.forEach { section ->
            if (section.trim().isNotEmpty()) {
                val lines = section.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
                if (lines.isNotEmpty()) {
                    educations.add(
                        com.cvgenerator.app.data.Education(
                            degree = lines.getOrNull(0) ?: "",
                            institution = lines.getOrNull(1) ?: "",
                            description = lines.drop(2).joinToString(" ")
                        )
                    )
                }
            }
        }
        
        return educations
    }
    
    private fun extractSkillsSection(text: String): com.cvgenerator.app.data.Skills {
        val skillsText = extractSection(text, listOf("skills", "technical", "competencies"))
        if (skillsText.isEmpty()) return com.cvgenerator.app.data.Skills()
        
        // Basic skill extraction - can be enhanced
        val skills = skillsText.split("[,;\n]".toRegex())
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        
        return com.cvgenerator.app.data.Skills(
            technicalSkills = skills
        )
    }
    
    private fun extractProjectsSection(text: String): List<com.cvgenerator.app.data.Project> {
        val projectsText = extractSection(text, listOf("projects", "portfolio"))
        if (projectsText.isEmpty()) return emptyList()
        
        val projects = mutableListOf<com.cvgenerator.app.data.Project>()
        val sections = projectsText.split("\n\n".toRegex())
        
        sections.forEach { section ->
            if (section.trim().isNotEmpty()) {
                val lines = section.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
                if (lines.isNotEmpty()) {
                    projects.add(
                        com.cvgenerator.app.data.Project(
                            name = lines.getOrNull(0) ?: "",
                            description = lines.drop(1).joinToString(" ")
                        )
                    )
                }
            }
        }
        
        return projects
    }
}
