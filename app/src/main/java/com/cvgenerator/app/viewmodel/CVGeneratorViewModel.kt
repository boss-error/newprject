package com.cvgenerator.app.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cvgenerator.app.data.CVData
import com.cvgenerator.app.data.CVTemplate
import com.cvgenerator.app.data.CVTemplateType
import com.cvgenerator.app.service.AIService
import com.cvgenerator.app.service.FileParsingService
import com.cvgenerator.app.service.PDFCompilationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class CVGeneratorViewModel(private val context: Context) : ViewModel() {
    
    private val aiService = AIService()
    private val fileParsingService = FileParsingService(context)
    private val pdfCompilationService = PDFCompilationService(context)
    
    // UI State
    private val _uiState = MutableStateFlow(CVGeneratorUiState())
    val uiState: StateFlow<CVGeneratorUiState> = _uiState.asStateFlow()
    
    // CV Data
    private val _cvData = MutableStateFlow(CVData())
    val cvData: StateFlow<CVData> = _cvData.asStateFlow()
    
    // Available templates
    private val _templates = MutableStateFlow(getAvailableTemplates())
    val templates: StateFlow<List<CVTemplate>> = _templates.asStateFlow()
    
    // Selected template
    private val _selectedTemplate = MutableStateFlow<CVTemplate?>(null)
    val selectedTemplate: StateFlow<CVTemplate?> = _selectedTemplate.asStateFlow()
    
    // Generated PDF
    private val _generatedPDF = MutableStateFlow<File?>(null)
    val generatedPDF: StateFlow<File?> = _generatedPDF.asStateFlow()
    
    fun updateCVData(newData: CVData) {
        _cvData.value = newData
    }
    
    fun uploadFile(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            fileParsingService.parseFile(uri)
                .onSuccess { parsedData ->
                    _cvData.value = parsedData
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        currentStep = CVGenerationStep.DATA_INPUT
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to parse file"
                    )
                }
        }
    }
    
    fun selectTemplate(template: CVTemplate) {
        _selectedTemplate.value = template
        _uiState.value = _uiState.value.copy(currentStep = CVGenerationStep.TEMPLATE_SELECTED)
    }
    
    fun generateCV() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                currentStep = CVGenerationStep.AI_ENHANCEMENT,
                error = null
            )
            
            // Step 1: Enhance CV with AI
            aiService.enhanceCV(_cvData.value)
                .onSuccess { enhancedData ->
                    _uiState.value = _uiState.value.copy(
                        currentStep = CVGenerationStep.LATEX_GENERATION
                    )
                    
                    // Step 2: Generate LaTeX from template
                    val template = _selectedTemplate.value
                    if (template != null) {
                        val templateContent = loadTemplateContent(template)
                        
                        aiService.generateLatexFromTemplate(enhancedData, templateContent)
                            .onSuccess { latexCode ->
                                _uiState.value = _uiState.value.copy(
                                    currentStep = CVGenerationStep.PDF_COMPILATION
                                )
                                
                                // Step 3: Compile LaTeX to PDF
                                pdfCompilationService.compileLatexToPDF(latexCode)
                                    .onSuccess { pdfFile ->
                                        _generatedPDF.value = pdfFile
                                        _uiState.value = _uiState.value.copy(
                                            isLoading = false,
                                            currentStep = CVGenerationStep.COMPLETED
                                        )
                                    }
                                    .onFailure { error ->
                                        _uiState.value = _uiState.value.copy(
                                            isLoading = false,
                                            error = "PDF compilation failed: \${error.message}"
                                        )
                                    }
                            }
                            .onFailure { error ->
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    error = "LaTeX generation failed: \${error.message}"
                                )
                            }
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "No template selected"
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "AI enhancement failed: \${error.message}"
                    )
                }
        }
    }
    
    private fun loadTemplateContent(template: CVTemplate): String {
        return try {
            context.assets.open(template.templatePath).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            ""
        }
    }
    
    private fun getAvailableTemplates(): List<CVTemplate> {
        return listOf(
            CVTemplate(
                id = "modern",
                name = "Modern",
                description = "A clean, modern design perfect for tech professionals",
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
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun resetWorkflow() {
        _uiState.value = CVGeneratorUiState()
        _cvData.value = CVData()
        _selectedTemplate.value = null
        _generatedPDF.value = null
    }
    
    fun goToStep(step: CVGenerationStep) {
        _uiState.value = _uiState.value.copy(currentStep = step)
    }
    
    override fun onCleared() {
        super.onCleared()
        // Clean up temporary files
        pdfCompilationService.cleanupTempFiles()
    }
}

data class CVGeneratorUiState(
    val isLoading: Boolean = false,
    val currentStep: CVGenerationStep = CVGenerationStep.WELCOME,
    val error: String? = null
)

enum class CVGenerationStep {
    WELCOME,
    DATA_INPUT,
    TEMPLATE_SELECTION,
    TEMPLATE_SELECTED,
    AI_ENHANCEMENT,
    LATEX_GENERATION,
    PDF_COMPILATION,
    COMPLETED
}
