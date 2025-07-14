package com.cvgenerator.app.data

data class CVData(
    val personalInfo: PersonalInfo = PersonalInfo(),
    val summary: String = "",
    val experience: List<Experience> = emptyList(),
    val education: List<Education> = emptyList(),
    val skills: Skills = Skills(),
    val projects: List<Project> = emptyList(),
    val targetJobTitle: String = ""
)

data class PersonalInfo(
    val fullName: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val address: String = "",
    val city: String = "",
    val country: String = "",
    val website: String = "",
    val linkedin: String = "",
    val github: String = ""
)

data class Experience(
    val jobTitle: String = "",
    val company: String = "",
    val location: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val description: String = "",
    val achievements: List<String> = emptyList()
)

data class Education(
    val degree: String = "",
    val institution: String = "",
    val location: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val gpa: String = "",
    val description: String = ""
)

data class Skills(
    val programmingLanguages: List<String> = emptyList(),
    val frameworks: List<String> = emptyList(),
    val tools: List<String> = emptyList(),
    val databases: List<String> = emptyList(),
    val spokenLanguages: List<String> = emptyList(),
    val certifications: List<String> = emptyList(),
    val technicalSkills: List<String> = emptyList()
)

data class Project(
    val name: String = "",
    val description: String = "",
    val technologies: List<String> = emptyList(),
    val startDate: String = "",
    val endDate: String = "",
    val url: String = "",
    val achievements: List<String> = emptyList()
)

data class CVTemplate(
    val id: String,
    val name: String,
    val description: String,
    val previewImagePath: String,
    val templatePath: String
)

enum class CVTemplateType {
    MODERN,
    MINIMALIST,
    ELEGANT
}
