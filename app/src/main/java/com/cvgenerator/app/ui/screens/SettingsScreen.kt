package com.cvgenerator.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cvgenerator.app.ui.components.ModernCard
import com.cvgenerator.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    var darkMode by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }
    var autoSave by remember { mutableStateOf(true) }
    var aiEnhancement by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Settings",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Appearance Section
            SettingsSection(
                title = "Appearance",
                icon = Icons.Default.Palette
            ) {
                SettingsToggleItem(
                    title = "Dark Mode",
                    description = "Switch between light and dark themes",
                    checked = darkMode,
                    onCheckedChange = { darkMode = it },
                    icon = Icons.Default.DarkMode
                )
            }
            
            // Preferences Section
            SettingsSection(
                title = "Preferences",
                icon = Icons.Default.Settings
            ) {
                SettingsToggleItem(
                    title = "Notifications",
                    description = "Receive updates and tips",
                    checked = notifications,
                    onCheckedChange = { notifications = it },
                    icon = Icons.Default.Notifications
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                SettingsToggleItem(
                    title = "Auto Save",
                    description = "Automatically save your progress",
                    checked = autoSave,
                    onCheckedChange = { autoSave = it },
                    icon = Icons.Default.Save
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                SettingsToggleItem(
                    title = "AI Enhancement",
                    description = "Enable AI-powered content suggestions",
                    checked = aiEnhancement,
                    onCheckedChange = { aiEnhancement = it },
                    icon = Icons.Default.Psychology
                )
            }
            
            // Account Section
            SettingsSection(
                title = "Account",
                icon = Icons.Default.Person
            ) {
                SettingsClickableItem(
                    title = "Profile",
                    description = "Manage your profile information",
                    icon = Icons.Default.AccountCircle,
                    onClick = { }
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                SettingsClickableItem(
                    title = "Export Data",
                    description = "Download your CV data",
                    icon = Icons.Default.Download,
                    onClick = { }
                )
            }
            
            // Support Section
            SettingsSection(
                title = "Support",
                icon = Icons.Default.Help
            ) {
                SettingsClickableItem(
                    title = "Help & FAQ",
                    description = "Get help and find answers",
                    icon = Icons.Default.QuestionMark,
                    onClick = { }
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                SettingsClickableItem(
                    title = "Contact Support",
                    description = "Get in touch with our team",
                    icon = Icons.Default.Email,
                    onClick = { }
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                SettingsClickableItem(
                    title = "Rate App",
                    description = "Share your feedback",
                    icon = Icons.Default.Star,
                    onClick = { }
                )
            }
            
            // About Section
            SettingsSection(
                title = "About",
                icon = Icons.Default.Info
            ) {
                SettingsClickableItem(
                    title = "Version",
                    description = "1.0.0 (Latest)",
                    icon = Icons.Default.Update,
                    onClick = { }
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                SettingsClickableItem(
                    title = "Privacy Policy",
                    description = "Read our privacy policy",
                    icon = Icons.Default.Security,
                    onClick = { }
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                SettingsClickableItem(
                    title = "Terms of Service",
                    description = "Read our terms of service",
                    icon = Icons.Default.Description,
                    onClick = { }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    ModernCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2
    ) {
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(24.dp)
            )
            
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

@Composable
private fun SettingsToggleItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryBlue
            )
        )
    }
}

@Composable
private fun SettingsClickableItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithoutRipple { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier {
    return this.then(
        Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick() }
    )
}
