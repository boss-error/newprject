#!/usr/bin/env python3
"""
Validation script for Android CV Generator App
Tests core functionality and data structures
"""

import os
import re
import json

def validate_project_structure():
    """Validate that all required files are present"""
    print("🔍 Validating project structure...")
    
    required_files = [
        "app/build.gradle",
        "app/src/main/AndroidManifest.xml",
        "app/src/main/java/com/cvgenerator/app/MainActivity.kt",
        "app/src/main/java/com/cvgenerator/app/data/CVData.kt",
        "app/src/main/java/com/cvgenerator/app/service/AIService.kt",
        "app/src/main/java/com/cvgenerator/app/service/FileParsingService.kt",
        "app/src/main/java/com/cvgenerator/app/service/PDFCompilationService.kt",
        "app/src/main/java/com/cvgenerator/app/viewmodel/CVGeneratorViewModel.kt",
        "app/src/main/java/com/cvgenerator/app/ui/CVGeneratorApp.kt",
        "app/src/main/assets/templates/modern/template.tex",
        "app/src/main/assets/templates/minimalist/template.tex",
        "app/src/main/assets/templates/elegant/template.tex",
        "app/src/main/res/values/strings.xml",
        "app/src/main/res/values/colors.xml",
        "app/src/main/res/values/themes.xml"
    ]
    
    missing_files = []
    for file_path in required_files:
        if not os.path.exists(file_path):
            missing_files.append(file_path)
    
    if missing_files:
        print(f"❌ Missing files: {missing_files}")
        return False
    else:
        print("✅ All required files present")
        return True

def validate_latex_templates():
    """Validate LaTeX templates have required placeholders"""
    print("🔍 Validating LaTeX templates...")
    
    template_dirs = [
        "app/src/main/assets/templates/modern",
        "app/src/main/assets/templates/minimalist", 
        "app/src/main/assets/templates/elegant"
    ]
    
    required_placeholders = [
        "{{FULL_NAME}}", "{{EMAIL}}", "{{PHONE}}", "{{SUMMARY}}"
    ]
    
    all_valid = True
    
    for template_dir in template_dirs:
        template_file = os.path.join(template_dir, "template.tex")
        if os.path.exists(template_file):
            with open(template_file, 'r') as f:
                content = f.read()
                
            missing_placeholders = []
            for placeholder in required_placeholders:
                if placeholder not in content:
                    missing_placeholders.append(placeholder)
            
            if missing_placeholders:
                print(f"❌ {template_dir}: Missing placeholders {missing_placeholders}")
                all_valid = False
            else:
                print(f"✅ {template_dir}: All placeholders present")
        else:
            print(f"❌ {template_file}: File not found")
            all_valid = False
    
    return all_valid

def validate_kotlin_syntax():
    """Basic validation of Kotlin files for syntax issues"""
    print("🔍 Validating Kotlin syntax...")
    
    kotlin_files = []
    for root, dirs, files in os.walk("app/src/main/java"):
        for file in files:
            if file.endswith(".kt"):
                kotlin_files.append(os.path.join(root, file))
    
    syntax_issues = []
    
    for kt_file in kotlin_files:
        with open(kt_file, 'r') as f:
            content = f.read()
            
        # Basic syntax checks
        issues = []
        
        # Check for package declaration
        if not content.strip().startswith("package "):
            issues.append("Missing package declaration")
        
        # Check for balanced braces
        open_braces = content.count('{')
        close_braces = content.count('}')
        if open_braces != close_braces:
            issues.append(f"Unbalanced braces: {open_braces} open, {close_braces} close")
        
        # Check for balanced parentheses
        open_parens = content.count('(')
        close_parens = content.count(')')
        if open_parens != close_parens:
            issues.append(f"Unbalanced parentheses: {open_parens} open, {close_parens} close")
        
        if issues:
            syntax_issues.append(f"{kt_file}: {issues}")
    
    if syntax_issues:
        print(f"❌ Syntax issues found:")
        for issue in syntax_issues:
            print(f"   {issue}")
        return False
    else:
        print("✅ No syntax issues found")
        return True

def validate_data_models():
    """Validate data model structure"""
    print("🔍 Validating data models...")
    
    data_file = "app/src/main/java/com/cvgenerator/app/data/CVData.kt"
    if not os.path.exists(data_file):
        print(f"❌ Data file not found: {data_file}")
        return False
    
    with open(data_file, 'r') as f:
        content = f.read()
    
    required_classes = [
        "data class CVData",
        "data class PersonalInfo", 
        "data class Experience",
        "data class Education",
        "data class Skills",
        "data class Project",
        "data class CVTemplate"
    ]
    
    missing_classes = []
    for class_def in required_classes:
        if class_def not in content:
            missing_classes.append(class_def)
    
    if missing_classes:
        print(f"❌ Missing data classes: {missing_classes}")
        return False
    else:
        print("✅ All data classes present")
        return True

def validate_services():
    """Validate service implementations"""
    print("🔍 Validating services...")
    
    services = [
        ("app/src/main/java/com/cvgenerator/app/service/AIService.kt", ["class AIService", "suspend fun enhanceCV", "suspend fun generateLatexFromTemplate"]),
        ("app/src/main/java/com/cvgenerator/app/service/FileParsingService.kt", ["class FileParsingService", "suspend fun parseFile"]),
        ("app/src/main/java/com/cvgenerator/app/service/PDFCompilationService.kt", ["class PDFCompilationService", "suspend fun compileLatexToPDF"])
    ]
    
    all_valid = True
    
    for service_file, required_methods in services:
        if not os.path.exists(service_file):
            print(f"❌ Service file not found: {service_file}")
            all_valid = False
            continue
            
        with open(service_file, 'r') as f:
            content = f.read()
        
        missing_methods = []
        for method in required_methods:
            if method not in content:
                missing_methods.append(method)
        
        if missing_methods:
            print(f"❌ {service_file}: Missing methods {missing_methods}")
            all_valid = False
        else:
            print(f"✅ {service_file}: All methods present")
    
    return all_valid

def validate_ui_screens():
    """Validate UI screen implementations"""
    print("🔍 Validating UI screens...")
    
    screens = [
        "WelcomeScreen.kt",
        "DataInputScreen.kt", 
        "TemplateSelectionScreen.kt",
        "ProcessingScreen.kt",
        "PDFPreviewScreen.kt"
    ]
    
    all_valid = True
    
    for screen in screens:
        screen_file = f"app/src/main/java/com/cvgenerator/app/ui/screens/{screen}"
        if not os.path.exists(screen_file):
            print(f"❌ Screen file not found: {screen_file}")
            all_valid = False
            continue
            
        with open(screen_file, 'r') as f:
            content = f.read()
        
        # Check for Composable function
        if "@Composable" not in content:
            print(f"❌ {screen}: Missing @Composable annotation")
            all_valid = False
        else:
            print(f"✅ {screen}: Valid Composable screen")
    
    return all_valid

def validate_gradle_configuration():
    """Validate Gradle build configuration"""
    print("🔍 Validating Gradle configuration...")
    
    build_gradle = "app/build.gradle"
    if not os.path.exists(build_gradle):
        print(f"❌ Build file not found: {build_gradle}")
        return False
    
    with open(build_gradle, 'r') as f:
        content = f.read()
    
    required_dependencies = [
        "androidx.compose.ui:ui",
        "androidx.compose.material3:material3",
        "androidx.navigation:navigation-compose",
        "org.apache.poi:poi",
        "com.squareup.retrofit2:retrofit"
    ]
    
    missing_deps = []
    for dep in required_dependencies:
        if dep not in content:
            missing_deps.append(dep)
    
    if missing_deps:
        print(f"❌ Missing dependencies: {missing_deps}")
        return False
    else:
        print("✅ All required dependencies present")
        return True

def generate_test_report():
    """Generate comprehensive test report"""
    print("\n" + "="*60)
    print("📋 ANDROID CV GENERATOR APP - VALIDATION REPORT")
    print("="*60)
    
    tests = [
        ("Project Structure", validate_project_structure),
        ("LaTeX Templates", validate_latex_templates),
        ("Kotlin Syntax", validate_kotlin_syntax),
        ("Data Models", validate_data_models),
        ("Services", validate_services),
        ("UI Screens", validate_ui_screens),
        ("Gradle Configuration", validate_gradle_configuration)
    ]
    
    results = []
    for test_name, test_func in tests:
        print(f"\n🧪 Running {test_name} validation...")
        result = test_func()
        results.append((test_name, result))
    
    print("\n" + "="*60)
    print("📊 VALIDATION SUMMARY")
    print("="*60)
    
    passed = 0
    total = len(results)
    
    for test_name, result in results:
        status = "✅ PASS" if result else "❌ FAIL"
        print(f"{test_name:<25} {status}")
        if result:
            passed += 1
    
    print(f"\nOverall Result: {passed}/{total} tests passed")
    
    if passed == total:
        print("🎉 ALL VALIDATIONS PASSED! The Android CV Generator App is ready for development.")
        return True
    else:
        print(f"⚠️  {total - passed} validation(s) failed. Please review and fix the issues above.")
        return False

def main():
    """Main validation function"""
    print("🚀 Starting Android CV Generator App Validation...")
    
    # Change to workspace directory
    if os.path.exists("/home/user/workspace"):
        os.chdir("/home/user/workspace")
    
    success = generate_test_report()
    
    if success:
        print("\n✅ VALIDATION COMPLETE - App is ready for testing!")
        
        print("\n📱 Next Steps:")
        print("1. Open project in Android Studio")
        print("2. Add your OpenAI/Gemini API keys in AIService.kt")
        print("3. Sync Gradle and build the project")
        print("4. Run on Android device/emulator")
        print("5. Test the complete CV generation workflow")
        
        return 0
    else:
        print("\n❌ VALIDATION FAILED - Please fix the issues above")
        return 1

if __name__ == "__main__":
    exit(main())
