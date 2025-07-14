package com.cvgenerator.app

import com.cvgenerator.app.data.*
import com.cvgenerator.app.service.AIService
import org.junit.Test
import org.junit.Assert.*

class CVGeneratorTest {
    
    @Test
    fun testCVDataCreation() {
        val personalInfo = PersonalInfo(
            fullName = "John Doe",
            email = "john.doe@example.com",
            phone = "+1234567890"
        )
        
        val cvData = CVData(
            personalInfo = personalInfo,
            summary = "Experienced software developer",
            targetJobTitle = "Senior Android Developer"
        )
        
        assertEquals("John Doe", cvData.personalInfo.fullName)
        assertEquals("john.doe@example.com", cvData.personalInfo.email)
        assertEquals("Senior Android Developer", cvData.targetJobTitle)
        assertNotNull(cvData.skills)
        assertTrue(cvData.experience.isEmpty())
    }
    
    @Test
    fun testCVTemplateCreation() {
        val template = CVTemplate(
            id = "modern",
            name = "Modern",
            description = "A clean, modern design",
            previewImagePath = "templates/modern/preview.png",
            templatePath = "templates/modern/template.tex"
        )
        
        assertEquals("modern", template.id)
        assertEquals("Modern", template.name)
        assertTrue(template.templatePath.contains("template.tex"))
    }
    
    @Test
    fun testExperienceDataStructure() {
        val experience = Experience(
            jobTitle = "Software Engineer",
            company = "Tech Corp",
            location = "San Francisco, CA",
            startDate = "2020-01",
            endDate = "2023-12",
            description = "Developed mobile applications",
            achievements = listOf("Increased app performance by 30%", "Led team of 5 developers")
        )
        
        assertEquals("Software Engineer", experience.jobTitle)
        assertEquals("Tech Corp", experience.company)
        assertEquals(2, experience.achievements.size)
    }
    
    @Test
    fun testSkillsDataStructure() {
        val skills = Skills(
            programmingLanguages = listOf("Kotlin", "Java", "Python"),
            frameworks = listOf("Android", "Spring Boot", "React"),
            tools = listOf("Git", "Docker", "Jenkins"),
            technicalSkills = listOf("Mobile Development", "API Design", "Database Design")
        )
        
        assertEquals(3, skills.programmingLanguages.size)
        assertTrue(skills.programmingLanguages.contains("Kotlin"))
        assertTrue(skills.frameworks.contains("Android"))
        assertEquals(4, skills.technicalSkills.size)
    }
    
    @Test
    fun testProjectDataStructure() {
        val project = Project(
            name = "CV Generator App",
            description = "Android app for generating professional CVs",
            technologies = listOf("Kotlin", "Jetpack Compose", "Material 3"),
            startDate = "2024-01",
            endDate = "2024-03",
            url = "https://github.com/example/cv-generator"
        )
        
        assertEquals("CV Generator App", project.name)
        assertEquals(3, project.technologies.size)
        assertTrue(project.technologies.contains("Jetpack Compose"))
    }
}
