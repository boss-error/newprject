package com.cvgenerator.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cvgenerator.app.ui.screens.*
import com.cvgenerator.app.viewmodel.CVGeneratorViewModel
import com.cvgenerator.app.viewmodel.CVGenerationStep

@Composable
fun CVGeneratorApp() {
    val context = LocalContext.current
    val viewModel: CVGeneratorViewModel = viewModel { CVGeneratorViewModel(context) }
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("splash") {
                SplashScreen(
                    onSplashComplete = {
                        navController.navigate("onboarding") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }
            
            composable("onboarding") {
                OnboardingScreen(
                    onComplete = {
                        navController.navigate("welcome") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                )
            }
            
            composable("welcome") {
                EnhancedWelcomeScreen(
                    onGetStarted = {
                        navController.navigate("data_input")
                    }
                )
            }
            
            composable("data_input") {
                DataInputScreen(
                    viewModel = viewModel,
                    onNext = {
                        navController.navigate("template_selection")
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable("template_selection") {
                TemplateSelectionScreen(
                    viewModel = viewModel,
                    onTemplateSelected = {
                        navController.navigate("processing")
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable("processing") {
                ProcessingScreen(
                    viewModel = viewModel,
                    onCompleted = {
                        navController.navigate("preview") {
                            popUpTo("welcome") { inclusive = false }
                        }
                    },
                    onError = {
                        // Handle error - could navigate back or show error screen
                    }
                )
            }
            
            composable("preview") {
                PDFPreviewScreen(
                    viewModel = viewModel,
                    onStartOver = {
                        viewModel.resetWorkflow()
                        navController.navigate("welcome") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            
            composable("settings") {
                SettingsScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
