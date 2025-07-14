package com.cvgenerator.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cvgenerator.app.R
import com.cvgenerator.app.viewmodel.CVGeneratorViewModel
import com.cvgenerator.app.viewmodel.CVGenerationStep

@Composable
fun ProcessingScreen(
    viewModel: CVGeneratorViewModel,
    onCompleted: () -> Unit,
    onError: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Navigate when completed
    LaunchedEffect(uiState.currentStep) {
        if (uiState.currentStep == CVGenerationStep.COMPLETED) {
            onCompleted()
        }
    }
    
    // Handle errors
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            onError()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Progress Indicator
        CircularProgressIndicator(
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 32.dp),
            strokeWidth = 6.dp
        )
        
        // Current Step Title
        Text(
            text = getStepTitle(uiState.currentStep),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Step Description
        Text(
            text = getStepDescription(uiState.currentStep),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Progress Steps
        ProcessingSteps(currentStep = uiState.currentStep)
        
        // Error Display
        uiState.error?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Error",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun ProcessingSteps(currentStep: CVGenerationStep) {
    val steps = listOf(
        CVGenerationStep.AI_ENHANCEMENT to "Enhancing Content",
        CVGenerationStep.LATEX_GENERATION to "Generating LaTeX",
        CVGenerationStep.PDF_COMPILATION to "Compiling PDF"
    )
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        steps.forEachIndexed { index, (step, title) ->
            ProcessingStepItem(
                title = title,
                isActive = currentStep == step,
                isCompleted = currentStep.ordinal > step.ordinal,
                isLast = index == steps.size - 1
            )
        }
    }
}

@Composable
private fun ProcessingStepItem(
    title: String,
    isActive: Boolean,
    isCompleted: Boolean,
    isLast: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Step Indicator
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                isCompleted -> {
                    Card(
                        modifier = Modifier.size(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✓",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                isActive -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                }
                else -> {
                    Card(
                        modifier = Modifier.size(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ) {}
                }
            }
        }
        
        // Step Title
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            color = when {
                isCompleted -> MaterialTheme.colorScheme.primary
                isActive -> MaterialTheme.colorScheme.onSurface
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.padding(start = 16.dp)
        )
    }
    
    // Connector Line
    if (!isLast) {
        Box(
            modifier = Modifier
                .padding(start = 11.dp)
                .width(2.dp)
                .height(16.dp)
        ) {
            Divider(
                modifier = Modifier.fillMaxHeight(),
                color = if (isCompleted) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
private fun getStepTitle(step: CVGenerationStep): String {
    return when (step) {
        CVGenerationStep.AI_ENHANCEMENT -> stringResource(R.string.enhancing_cv)
        CVGenerationStep.LATEX_GENERATION -> stringResource(R.string.generating_latex)
        CVGenerationStep.PDF_COMPILATION -> stringResource(R.string.compiling_pdf)
        else -> "Processing..."
    }
}

@Composable
private fun getStepDescription(step: CVGenerationStep): String {
    return when (step) {
        CVGenerationStep.AI_ENHANCEMENT -> "Our AI is analyzing and improving your CV content to match your target job requirements."
        CVGenerationStep.LATEX_GENERATION -> "Converting your enhanced CV data into professional LaTeX format using your selected template."
        CVGenerationStep.PDF_COMPILATION -> "Compiling the LaTeX code into a beautiful PDF document ready for download."
        else -> "Please wait while we process your CV..."
    }
}
