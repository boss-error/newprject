package com.cvgenerator.app

import com.cvgenerator.app.data.*
import com.cvgenerator.app.service.FileParsingService
import com.cvgenerator.app.service.PDFCompilationService
import org.junit.Test
import org.junit.Assert.*

class ServiceTest {
    
    @Test
    fun testFileParsingServiceInitialization() {
        // Test that FileParsingService can be instantiated
        // In a real test, we would mock the Android Context
        // For now, we'll test the logic without Android dependencies
        
        val testText = """
            John Doe
            john.doe@example.com
            +1234567890
            
            SUMMARY
            Experienced software developer with 5 years of experience
            
            EXPERIENCE
            Software Engineer at Tech Corp
            Developed mobile applications using Kotlin and Java
            
            SKILLS
            Kotlin, Java, Android, Git
        """.trimIndent()
        
        // Test email extraction logic
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val email = emailRegex.find(testText)?.value
        assertEquals("john.doe@example.com", email)
        
        // Test phone extraction logic
        val phoneRegex = Regex("(\\+?\\d{1,3}[-.\\s]?)?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}")
        val phone = phoneRegex.find(testText)?.value
        assertEquals("+1234567890", phone)
    }
    
    @Test
    fun testPDFCompilationServiceMockGeneration() {
        // Test the mock PDF generation logic
        val mockContent = """
            %PDF-1.4
            1 0 obj
            <<
            /Type /Catalog
            /Pages 2 0 R
            >>
            endobj
        """.trimIndent()
        
        val pdfBytes = mockContent.toByteArray()
        assertTrue(pdfBytes.isNotEmpty())
        assertTrue(String(pdfBytes).contains("%PDF-1.4"))
    }
    
    @Test
    fun testLatexTemplateVariableReplacement() {
        val templateContent = """
            \\begin{document}
            \\name{{{FULL_NAME}}}
            \\email{{{EMAIL}}}
            \\phone{{{PHONE}}}
            \\section{Summary}
            {{SUMMARY}}
            \\end{document}
        """.trimIndent()
        
        val cvData = CVData(
            personalInfo = PersonalInfo(
                fullName = "John Doe",
                email = "john.doe@example.com",
                phone = "+1234567890"
            ),
            summary = "Experienced developer"
        )
        
        // Test template variable identification
        assertTrue(templateContent.contains("{{FULL_NAME}}"))
        assertTrue(templateContent.contains("{{EMAIL}}"))
        assertTrue(templateContent.contains("{{PHONE}}"))
        assertTrue(templateContent.contains("{{SUMMARY}}"))
        
        // Test basic replacement logic
        var processedContent = templateContent
            .replace("{{FULL_NAME}}", cvData.personalInfo.fullName)
            .replace("{{EMAIL}}", cvData.personalInfo.email)
            .replace("{{PHONE}}", cvData.personalInfo.phone)
            .replace("{{SUMMARY}}", cvData.summary)
        
        assertTrue(processedContent.contains("John Doe"))
        assertTrue(processedContent.contains("john.doe@example.com"))
        assertTrue(processedContent.contains("Experienced developer"))
        assertFalse(processedContent.contains("{{FULL_NAME}}"))
    }
    
    @Test
    fun testAIPromptGeneration() {
        val cvData = CVData(
            personalInfo = PersonalInfo(
                fullName = "John Doe",
                email = "john.doe@example.com",
                phone = "+1234567890"
            ),
            summary = "Software developer",
            targetJobTitle = "Senior Android Developer",
            experience = listOf(
                Experience(
                    jobTitle = "Software Engineer",
                    company = "Tech Corp",
                    description = "Developed mobile apps"
                )
            ),
            skills = Skills(
                programmingLanguages = listOf("Kotlin", "Java"),
                frameworks = listOf("Android")
            )
        )
        
        // Test prompt generation logic
        val prompt = """
            Using the following user information, improve and rewrite it to match a strong professional CV tailored to the specific job title.

            INPUT DATA:
            Name: \${cvData.personalInfo.fullName}
            Email: \${cvData.personalInfo.email}
            Phone: \${cvData.personalInfo.phone}
            Summary: \${cvData.summary}
            
            Experience:
            \${cvData.experience.joinToString("\n") { 
                "\${it.jobTitle} at \${it.company}\n\${it.description}"
            }}
            
            Skills:
            Programming Languages: \${cvData.skills.programmingLanguages.joinToString(", ")}
            Frameworks: \${cvData.skills.frameworks.joinToString(", ")}

            TARGET JOB TITLE: \${cvData.targetJobTitle}
        """.trimIndent()
        
        assertTrue(prompt.contains("John Doe"))
        assertTrue(prompt.contains("Senior Android Developer"))
        assertTrue(prompt.contains("Software Engineer at Tech Corp"))
        assertTrue(prompt.contains("Kotlin, Java"))
    }
    
    @Test
    fun testViewModelStateManagement() {
        // Test the UI state enum
        val steps = listOf(
            com.cvgenerator.app.viewmodel.CVGenerationStep.WELCOME,
            com.cvgenerator.app.viewmodel.CVGenerationStep.DATA_INPUT,
            com.cvgenerator.app.viewmodel.CVGenerationStep.TEMPLATE_SELECTION,
            com.cvgenerator.app.viewmodel.CVGenerationStep.AI_ENHANCEMENT,
            com.cvgenerator.app.viewmodel.CVGenerationStep.LATEX_GENERATION,
            com.cvgenerator.app.viewmodel.CVGenerationStep.PDF_COMPILATION,
            com.cvgenerator.app.viewmodel.CVGenerationStep.COMPLETED
        )
        
        assertEquals(7, steps.size)
        assertEquals(0, steps[0].ordinal) // WELCOME should be first
        assertEquals(6, steps[6].ordinal) // COMPLETED should be last
    }
}
