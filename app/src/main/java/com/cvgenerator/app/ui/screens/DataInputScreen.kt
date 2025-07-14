package com.cvgenerator.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cvgenerator.app.R
import com.cvgenerator.app.data.CVData
import com.cvgenerator.app.data.PersonalInfo
import com.cvgenerator.app.viewmodel.CVGeneratorViewModel

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
        
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Processing file...")
                }
            }
        } else if (!showManualInput && cvData.personalInfo.fullName.isEmpty()) {
            // File Upload Options
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose how to input your CV data:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                // Upload File Button
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    onClick = {
                        filePickerLauncher.launch("*/*")
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.upload_file),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.supported_formats),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Text(
                    text = "OR",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                
                // Manual Input Button
                OutlinedButton(
                    onClick = { showManualInput = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.manual_input))
                }
            }
        } else {
            // Manual Input Form or Edit Parsed Data
            ManualInputForm(
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
private fun ManualInputForm(
    cvData: CVData,
    onDataChange: (CVData) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Personal Information
        Text(
            text = "Personal Information",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = cvData.personalInfo.fullName,
            onValueChange = { 
                onDataChange(cvData.copy(
                    personalInfo = cvData.personalInfo.copy(fullName = it)
                ))
            },
            label = { Text(stringResource(R.string.name_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        
        OutlinedTextField(
            value = cvData.personalInfo.email,
            onValueChange = { 
                onDataChange(cvData.copy(
                    personalInfo = cvData.personalInfo.copy(email = it)
                ))
            },
            label = { Text(stringResource(R.string.email_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        
        OutlinedTextField(
            value = cvData.personalInfo.phone,
            onValueChange = { 
                onDataChange(cvData.copy(
                    personalInfo = cvData.personalInfo.copy(phone = it)
                ))
            },
            label = { Text(stringResource(R.string.phone_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // Target Job Title
        OutlinedTextField(
            value = cvData.targetJobTitle,
            onValueChange = { 
                onDataChange(cvData.copy(targetJobTitle = it))
            },
            label = { Text(stringResource(R.string.target_job_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // Professional Summary
        OutlinedTextField(
            value = cvData.summary,
            onValueChange = { 
                onDataChange(cvData.copy(summary = it))
            },
            label = { Text(stringResource(R.string.summary_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            minLines = 3,
            maxLines = 5
        )
        
        // Experience
        OutlinedTextField(
            value = cvData.experience.joinToString("\n\n") { 
                "\${it.jobTitle} at \${it.company}\n\${it.description}"
            },
            onValueChange = { 
                // Simple parsing - in a real app, you'd want more sophisticated handling
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
            label = { Text(stringResource(R.string.experience_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            minLines = 4,
            maxLines = 8
        )
        
        // Skills
        OutlinedTextField(
            value = cvData.skills.technicalSkills.joinToString(", "),
            onValueChange = { 
                onDataChange(cvData.copy(
                    skills = cvData.skills.copy(
                        technicalSkills = it.split(",").map { skill -> skill.trim() }
                    )
                ))
            },
            label = { Text(stringResource(R.string.skills_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            minLines = 2
        )
        
        // Next Button
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = cvData.personalInfo.fullName.isNotEmpty() && 
                     cvData.personalInfo.email.isNotEmpty() &&
                     cvData.targetJobTitle.isNotEmpty()
        ) {
            Text(
                text = stringResource(R.string.next),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
