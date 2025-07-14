package com.cvgenerator.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cvgenerator.app.R
import com.cvgenerator.app.data.CVData
import com.cvgenerator.app.data.PersonalInfo
import com.cvgenerator.app.viewmodel.CVGeneratorViewModel
import com.cvgenerator.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataInputScreen(
    viewModel: CVGeneratorViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val cvData by viewModel.cvData.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    
    var showManualInput by remember { mutableStateOf(false) }
    
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadFile(it) }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = stringResource(R.string.input_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
        
        // Show error message if any
        uiState.errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person, // You can use a warning icon
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = PrimaryBlue,
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Processing file...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please wait while we extract your CV data",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else if (!showManualInput && cvData.personalInfo.fullName.isEmpty()) {
            // Modern File Upload Options with gradient background
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose how to input your CV data:",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                // Upload File Button with modern design
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    onClick = { filePickerLauncher.launch("*/*") }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(PrimaryBlue, SecondaryPurple)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Upload File",
                                modifier = Modifier.size(32.dp),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Upload CV File",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Supports PDF, DOC, DOCX, and TXT files",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "We'll automatically extract your information",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                // Divider with modern styling
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    Text(
                        text = "OR",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                }
                
                // Manual Input Button with modern design
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    onClick = { showManualInput = true }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(AccentTeal, AccentGreen)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Manual Input",
                                modifier = Modifier.size(32.dp),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Enter Manually",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Fill in your details step by step",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Perfect for creating a new CV from scratch",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            // Manual Input Form or Data Display
            ModernManualInputForm(
                cvData = cvData,
                onDataChange = { viewModel.updateCVData(it) },
                onNext = onNext
            )
        }
        
        // Error Display
        uiState.error?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernManualInputForm(
    cvData: CVData,
    onDataChange: (CVData) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Personal Information Section
        ModernSectionCard(
            title = "Personal Information",
            icon = Icons.Default.Person,
            gradient = Brush.horizontalGradient(
                colors = listOf(PrimaryBlue.copy(alpha = 0.1f), SecondaryPurple.copy(alpha = 0.1f))
            )
        ) {
            ModernTextField(
                value = cvData.personalInfo.fullName,
                onValueChange = { 
                    onDataChange(cvData.copy(
                        personalInfo = cvData.personalInfo.copy(fullName = it)
                    ))
                },
                label = "Full Name",
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            ModernTextField(
                value = cvData.personalInfo.email,
                onValueChange = { 
                    onDataChange(cvData.copy(
                        personalInfo = cvData.personalInfo.copy(email = it)
                    ))
                },
                label = "Email",
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            ModernTextField(
                value = cvData.personalInfo.phone,
                onValueChange = { 
                    onDataChange(cvData.copy(
                        personalInfo = cvData.personalInfo.copy(phone = it)
                    ))
                },
                label = "Phone"
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Target Job Section
        ModernSectionCard(
            title = "Target Position",
            icon = Icons.Default.Business,
            gradient = Brush.horizontalGradient(
                colors = listOf(AccentTeal.copy(alpha = 0.1f), AccentGreen.copy(alpha = 0.1f))
            )
        ) {
            ModernTextField(
                value = cvData.targetJobTitle,
                onValueChange = { 
                    onDataChange(cvData.copy(targetJobTitle = it))
                },
                label = "Target Job Title"
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Professional Summary Section
        ModernSectionCard(
            title = "Professional Summary",
            icon = Icons.Default.Star,
            gradient = Brush.horizontalGradient(
                colors = listOf(SecondaryPurple.copy(alpha = 0.1f), PrimaryBlue.copy(alpha = 0.1f))
            )
        ) {
            ModernTextField(
                value = cvData.summary,
                onValueChange = { 
                    onDataChange(cvData.copy(summary = it))
                },
                label = "Professional Summary",
                minLines = 3,
                maxLines = 5
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Experience Section
        ModernSectionCard(
            title = "Work Experience",
            icon = Icons.Default.Business,
            gradient = Brush.horizontalGradient(
                colors = listOf(AccentGreen.copy(alpha = 0.1f), AccentTeal.copy(alpha = 0.1f))
            )
        ) {
            ModernTextField(
                value = cvData.experience.firstOrNull()?.description ?: "",
                onValueChange = { 
                    onDataChange(cvData.copy(
                        experience = listOf(
                            com.cvgenerator.app.data.Experience(
                                jobTitle = "Job Title",
                                company = "Company",
                                description = it
                            )
                        )
                    ))
                },
                label = "Work Experience",
                minLines = 4,
                maxLines = 8
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Skills Section
        ModernSectionCard(
            title = "Skills",
            icon = Icons.Default.Star,
            gradient = Brush.horizontalGradient(
                colors = listOf(PrimaryBlue.copy(alpha = 0.1f), SecondaryPurple.copy(alpha = 0.1f))
            )
        ) {
            ModernTextField(
                value = cvData.skills.technicalSkills.joinToString(", "),
                onValueChange = { 
                    onDataChange(cvData.copy(
                        skills = cvData.skills.copy(
                            technicalSkills = it.split(",").map { skill -> skill.trim() }
                        )
                    ))
                },
                label = "Technical Skills",
                minLines = 2
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Next Button with gradient
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(20.dp),
            enabled = cvData.personalInfo.fullName.isNotEmpty() && 
                     cvData.personalInfo.email.isNotEmpty() &&
                     cvData.targetJobTitle.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                disabledContainerColor = NeutralGray.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = "Next",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ModernSectionCard(
    title: String,
    icon: ImageVector,
    gradient: Brush,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE
) {
    // Ensure maxLines is always >= minLines to prevent crash
    val safeMaxLines = if (maxLines < minLines) minLines else maxLines
    
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        minLines = minLines,
        maxLines = safeMaxLines,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryBlue,
            focusedLabelColor = PrimaryBlue,
            cursorColor = PrimaryBlue,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

// Keep the old function for compatibility
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManualInputForm(
    cvData: CVData,
    onDataChange: (CVData) -> Unit,
    onNext: () -> Unit
) {
    ModernManualInputForm(cvData, onDataChange, onNext)
}
