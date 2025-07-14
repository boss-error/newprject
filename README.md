# 📱 Android CV Generator App

A comprehensive Android application that enables users to create professional CVs using AI enhancement and LaTeX templates.

## 🚀 Features

### Core Functionality
- **📥 Multiple Input Methods**: Upload CV files (PDF, DOCX, TXT) or manual input
- **🤖 AI Enhancement**: Improve CV content using OpenAI GPT-4+ or Gemini API
- **🎨 Professional Templates**: Choose from Modern, Minimalist, and Elegant LaTeX templates
- **📄 PDF Generation**: Compile LaTeX to professional PDF documents
- **📱 Modern UI**: Built with Jetpack Compose and Material 3 Design

### Technical Features
- **File Parsing**: Extract data from DOCX (Apache POI) and PDF (PDFBox) files
- **AI Integration**: OpenAI and Gemini API support for content enhancement
- **LaTeX Compilation**: Cloud-based or custom backend LaTeX compilation
- **PDF Viewing**: In-app PDF preview and sharing capabilities
- **Responsive Design**: Optimized for all Android device sizes

## 🛠 Tech Stack

| Component | Technology |
|-----------|------------|
| **Frontend** | Kotlin + Jetpack Compose |
| **Architecture** | MVVM with StateFlow |
| **AI Backend** | OpenAI API (GPT-4) / Gemini API |
| **File Parsing** | Apache POI, PDFBox |
| **Networking** | Retrofit, OkHttp |
| **PDF Generation** | LaTeX compilation service |
| **UI Framework** | Material 3 Design |
| **Storage** | Android Scoped Storage |

## 📁 Project Structure

```
app/
├── src/main/
│   ├── java/com/cvgenerator/app/
│   │   ├── data/                 # Data models and classes
│   │   ├── service/              # AI, File parsing, PDF services
│   │   ├── viewmodel/            # ViewModels for UI state management
│   │   ├── ui/                   # Compose UI components
│   │   │   ├── screens/          # Individual screen composables
│   │   │   └── theme/            # App theming
│   │   └── MainActivity.kt       # Main activity
│   ├── assets/templates/         # LaTeX templates
│   │   ├── modern/
│   │   ├── minimalist/
│   │   └── elegant/
│   └── res/                      # Android resources
└── build.gradle                  # App-level build configuration
```

## 🎯 App Workflow

1. **Welcome Screen**: Introduction and feature overview
2. **Data Input**: Upload file or manual CV data entry
3. **Template Selection**: Choose from available LaTeX templates
4. **AI Processing**: 
   - Content enhancement with AI
   - LaTeX code generation
   - PDF compilation
5. **Preview & Share**: Download, share, or regenerate CV

## 🔧 Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+ (Android 7.0)
- Kotlin 1.8.20+

### API Keys Setup
1. Get API keys from:
   - [OpenAI API](https://platform.openai.com/api-keys)
   - [Google Gemini API](https://makersuite.google.com/app/apikey)

2. Update API keys in `AIService.kt`:
```kotlin
private val openAiApiKey = "YOUR_OPENAI_API_KEY"
private val geminiApiKey = "YOUR_GEMINI_API_KEY"
```

### Build and Run
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Update API keys
5. Build and run on device/emulator

## 📋 Dependencies

### Core Android
- `androidx.core:core-ktx:1.10.1`
- `androidx.activity:activity-compose:1.7.2`
- `androidx.compose.material3:material3`
- `androidx.navigation:navigation-compose:2.6.0`

### File Processing
- `org.apache.poi:poi:5.2.3`
- `org.apache.poi:poi-ooxml:5.2.3`
- `org.apache.pdfbox:pdfbox-android:2.0.27.0`

### Networking
- `com.squareup.retrofit2:retrofit:2.9.0`
- `com.squareup.okhttp3:logging-interceptor:4.11.0`

### PDF Viewing
- `com.github.barteksc:android-pdf-viewer:3.2.0-beta.1`

## 🎨 LaTeX Templates

### Modern Template
- Clean, professional design
- Perfect for tech professionals
- ATS-friendly formatting

### Minimalist Template
- Simple and elegant
- Focus on content over design
- Suitable for all industries

### Elegant Template
- Professional design with modern styling
- Comprehensive sections support
- Academic and corporate friendly

## 🔒 Permissions

- `INTERNET`: For AI API calls and LaTeX compilation
- `READ_EXTERNAL_STORAGE`: For file uploads
- `WRITE_EXTERNAL_STORAGE`: For PDF downloads

## 🚀 Future Enhancements

- **Multi-language Support**: Internationalization
- **LinkedIn Integration**: Import profile data
- **Offline Mode**: Local CV drafts
- **More Templates**: Additional LaTeX designs
- **Advanced AI**: Job-specific optimizations
- **Cloud Sync**: Save CVs to cloud storage

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue on GitHub
- Check the documentation
- Review the code comments

## 🙏 Acknowledgments

- OpenAI for GPT-4 API
- Google for Gemini API
- Apache POI team for document processing
- PDFBox team for PDF handling
- Android Jetpack Compose team

---

**Note**: This app requires active internet connection for AI processing and LaTeX compilation. Ensure you have valid API keys configured before running the application.
