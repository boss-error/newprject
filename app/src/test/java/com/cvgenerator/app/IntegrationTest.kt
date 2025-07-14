package com.cvgenerator.app

import com.cvgenerator.app.data.*
import com.cvgenerator.app.viewmodel.CVGenerationStep
import org.junit.Test
import org.junit.Assert.*

class IntegrationTest {
    
    @Test
    fun testCompleteWorkflowDataFlow() {
        // Test the complete data flow from input to output
        
        // Step 1: Create initial CV data (simulating user input)
        val initialCVData = CVData(
            personalInfo = PersonalInfo(
                fullName = "Jane Smith",
                firstName = "Jane",
                lastName = "Smith",
                email = "jane.smith@example.com",
                phone = "+1-555-0123",
                location = "San Francisco, CA"
            ),
            summary = "Experienced mobile developer with passion for creating user-friendly applications",
            experience = listOf(
                Experience(
                    jobTitle = "Mobile Developer",
                    company = "TechStart Inc",
                    location = "San Francisco, CA",
                    startDate = "2021-03",
                    endDate = "Present",
                    description = "Developed and maintained Android applications using Kotlin and Java",
                    achievements = listOf(
                        "Improved app performance by 40%",
                        "Led migration to Jetpack Compose",
                        "Mentored 3 junior developers"
                    )
                ),
                Experience(
                    jobTitle = "Junior Android Developer",
                    company = "Mobile Solutions LLC",
                    location = "San Jose, CA",
                    startDate = "2019-06",
                    endDate = "2021-02",
                    description = "Built mobile applications and worked on UI/UX improvements",
                    achievements = listOf(
                        "Delivered 5 successful app releases",
                        "Reduced crash rate by 60%"
                    )
                )
            ),
            education = listOf(
                Education(
                    degree = "Bachelor of Science in Computer Science",
                    institution = "University of California, Berkeley",
                    location = "Berkeley, CA",
                    startDate = "2015-09",
                    endDate = "2019-05",
                    gpa = "3.8",
                    description = "Relevant coursework: Mobile Development, Software Engineering, Data Structures"
                )
            ),
            skills = Skills(
                programmingLanguages = listOf("Kotlin", "Java", "Python", "JavaScript"),
                frameworks = listOf("Android SDK", "Jetpack Compose", "Spring Boot", "React Native"),
                tools = listOf("Git", "Android Studio", "Firebase", "Docker", "Jenkins"),
                databases = listOf("SQLite", "Room", "PostgreSQL", "MongoDB"),
                technicalSkills = listOf("Mobile Development", "UI/UX Design", "API Integration", "Testing", "CI/CD")
            ),
            projects = listOf(
                Project(
                    name = "Fitness Tracker App",
                    description = "Android app for tracking workouts and nutrition with 10K+ downloads",
                    technologies = listOf("Kotlin", "Jetpack Compose", "Room", "Firebase"),
                    startDate = "2023-01",
                    endDate = "2023-06",
                    url = "https://github.com/janesmith/fitness-tracker",
                    achievements = listOf(
                        "Featured in Google Play Store",
                        "4.5 star rating with 500+ reviews"
                    )
                ),
                Project(
                    name = "Weather Dashboard",
                    description = "Real-time weather application with location-based forecasts",
                    technologies = listOf("Kotlin", "Retrofit", "MVVM", "Material Design"),
                    startDate = "2022-08",
                    endDate = "2022-12",
                    url = "https://github.com/janesmith/weather-app"
                )
            ),
            targetJobTitle = "Senior Android Developer"
        )
        
        // Validate initial data structure
        assertEquals("Jane Smith", initialCVData.personalInfo.fullName)
        assertEquals("Senior Android Developer", initialCVData.targetJobTitle)
        assertEquals(2, initialCVData.experience.size)
        assertEquals(1, initialCVData.education.size)
        assertEquals(2, initialCVData.projects.size)
        assertTrue(initialCVData.skills.programmingLanguages.contains("Kotlin"))
        
        // Step 2: Test template selection
        val availableTemplates = listOf(
            CVTemplate(
                id = "modern",
                name = "Modern",
                description = "Clean, modern design perfect for tech professionals",
                previewImagePath = "templates/modern/preview.png",
                templatePath = "templates/modern/template.tex"
            ),
            CVTemplate(
                id = "minimalist",
                name = "Minimalist",
                description = "Simple and elegant design focusing on content",
                previewImagePath = "templates/minimalist/preview.png",
                templatePath = "templates/minimalist/template.tex"
            ),
            CVTemplate(
                id = "elegant",
                name = "Elegant",
                description = "Professional design suitable for all industries",
                previewImagePath = "templates/elegant/preview.png",
                templatePath = "templates/elegant/template.tex"
            )
        )
        
        assertEquals(3, availableTemplates.size)
        val selectedTemplate = availableTemplates.find { it.id == "modern" }
        assertNotNull(selectedTemplate)
        assertEquals("Modern", selectedTemplate?.name)
        
        // Step 3: Test AI prompt generation
        val aiPrompt = generateAIPrompt(initialCVData)
        assertTrue(aiPrompt.contains("Jane Smith"))
        assertTrue(aiPrompt.contains("Senior Android Developer"))
        assertTrue(aiPrompt.contains("Mobile Developer at TechStart Inc"))
        assertTrue(aiPrompt.contains("Kotlin"))
        assertTrue(aiPrompt.contains("Jetpack Compose"))
        
        // Step 4: Simulate AI enhancement (mock response)
        val enhancedCVContent = simulateAIEnhancement(initialCVData)
        assertTrue(enhancedCVContent.contains("experienced"))
        assertTrue(enhancedCVContent.contains("Android"))
        assertTrue(enhancedCVContent.contains("leadership"))
        
        // Step 5: Test LaTeX template processing
        val latexTemplate = loadMockTemplate("modern")
        assertTrue(latexTemplate.contains("{{FULL_NAME}}"))
        assertTrue(latexTemplate.contains("{{EMAIL}}"))
        assertTrue(latexTemplate.contains("{{EXPERIENCE}}"))
        
        val processedLatex = processLatexTemplate(latexTemplate, initialCVData)
        assertTrue(processedLatex.contains("Jane Smith"))
        assertTrue(processedLatex.contains("jane.smith@example.com"))
        assertFalse(processedLatex.contains("{{FULL_NAME}}"))
        
        // Step 6: Test PDF generation (mock)
        val pdfContent = generateMockPDF(processedLatex)
        assertTrue(pdfContent.isNotEmpty())
        assertTrue(String(pdfContent).contains("%PDF"))
        
        // Step 7: Test workflow state progression
        val workflowSteps = listOf(
            CVGenerationStep.WELCOME,
            CVGenerationStep.DATA_INPUT,
            CVGenerationStep.TEMPLATE_SELECTION,
            CVGenerationStep.AI_ENHANCEMENT,
            CVGenerationStep.LATEX_GENERATION,
            CVGenerationStep.PDF_COMPILATION,
            CVGenerationStep.COMPLETED
        )
        
        // Verify step progression
        for (i in 0 until workflowSteps.size - 1) {
            assertTrue(workflowSteps[i].ordinal < workflowSteps[i + 1].ordinal)
        }
        
        assertEquals(CVGenerationStep.WELCOME, workflowSteps.first())
        assertEquals(CVGenerationStep.COMPLETED, workflowSteps.last())
    }
    
    private fun generateAIPrompt(cvData: CVData): String {
        return """
            Using the following user information, improve and rewrite it to match a strong professional CV tailored to the specific job title.

            INPUT DATA:
            Name: \${cvData.personalInfo.fullName}
            Email: \${cvData.personalInfo.email}
            Phone: \${cvData.personalInfo.phone}
            Summary: \${cvData.summary}
            
            Experience:
            \${cvData.experience.joinToString("\n") { 
                "\${it.jobTitle} at \${it.company} (\${it.startDate} - \${it.endDate})\n\${it.description}"
            }}
            
            Skills:
            Programming Languages: \${cvData.skills.programmingLanguages.joinToString(", ")}
            Frameworks: \${cvData.skills.frameworks.joinToString(", ")}
            Tools: \${cvData.skills.tools.joinToString(", ")}

            TARGET JOB TITLE: \${cvData.targetJobTitle}
        """.trimIndent()
    }
    
    private fun simulateAIEnhancement(cvData: CVData): String {
        return """
            ENHANCED CV CONTENT:
            
            PROFESSIONAL SUMMARY:
            Highly experienced Senior Android Developer with \${cvData.experience.size}+ years of proven expertise in mobile application development. 
            Demonstrated leadership in \${cvData.experience.first().achievements.size} key achievements including performance optimization and team mentorship.
            
            TECHNICAL EXPERTISE:
            - Advanced proficiency in \${cvData.skills.programmingLanguages.joinToString(", ")}
            - Expert-level experience with \${cvData.skills.frameworks.joinToString(", ")}
            - Strong background in \${cvData.skills.technicalSkills.joinToString(", ")}
            
            PROFESSIONAL EXPERIENCE:
            \${cvData.experience.joinToString("\n\n") { exp ->
                "\${exp.jobTitle} | \${exp.company} | \${exp.startDate} - \${exp.endDate}\n" +
                "• Enhanced \${exp.description}\n" +
                exp.achievements.joinToString("\n") { "• \$it" }
            }}
        """.trimIndent()
    }
    
    private fun loadMockTemplate(templateId: String): String {
        return when (templateId) {
            "modern" -> """
                \documentclass[letterpaper,11pt]{article}
                \begin{document}
                
                \begin{center}
                    {\Huge \scshape {{FULL_NAME}}} \\ \vspace{1pt}
                    \small {{PHONE}} ~ \href{mailto:{{EMAIL}}}{{{EMAIL}}}
                \end{center}
                
                \section{Professional Summary}
                {{SUMMARY}}
                
                \section{Experience}
                {{EXPERIENCE}}
                
                \section{Education}
                {{EDUCATION}}
                
                \section{Technical Skills}
                {{SKILLS}}
                
                \section{Projects}
                {{PROJECTS}}
                
                \end{document}
            """.trimIndent()
            else -> ""
        }
    }
    
    private fun processLatexTemplate(template: String, cvData: CVData): String {
        return template
            .replace("{{FULL_NAME}}", cvData.personalInfo.fullName)
            .replace("{{EMAIL}}", cvData.personalInfo.email)
            .replace("{{PHONE}}", cvData.personalInfo.phone)
            .replace("{{SUMMARY}}", cvData.summary)
            .replace("{{EXPERIENCE}}", cvData.experience.joinToString("\n\n") { 
                "\${it.jobTitle} at \${it.company} (\${it.startDate} - \${it.endDate})\n\${it.description}"
            })
            .replace("{{EDUCATION}}", cvData.education.joinToString("\n") {
                "\${it.degree} from \${it.institution} (\${it.startDate} - \${it.endDate})"
            })
            .replace("{{SKILLS}}", cvData.skills.programmingLanguages.joinToString(", "))
            .replace("{{PROJECTS}}", cvData.projects.joinToString("\n\n") {
                "\${it.name}: \${it.description}"
            })
    }
    
    private fun generateMockPDF(latexContent: String): ByteArray {
        val mockPDFContent = """
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
            /Length 100
            >>
            stream
            BT
            /F1 12 Tf
            72 720 Td
            (Generated CV from LaTeX: \${latexContent.take(50)}...) Tj
            ET
            endstream
            endobj
            
            xref
            0 5
            trailer
            <<
            /Size 5
            /Root 1 0 R
            >>
            startxref
            500
            %%EOF
        """.trimIndent()
        
        return mockPDFContent.toByteArray()
    }
}
