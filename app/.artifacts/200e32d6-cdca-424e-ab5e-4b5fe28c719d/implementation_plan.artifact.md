# Implementation Plan: Migrate AI Module to Google AI Studio SDK

Completely remove Firebase AI Logic and Vertex AI dependencies. Migrate to the official Google AI Studio SDK using the `gemini-2.0-flash` model and secure API key management.

## User Review Required

> [!IMPORTANT]
> - **API Key Security**: The API key will be moved to `local.properties`. You MUST ensure your `local.properties` file contains the key after this migration if you are running this on a new machine.
> - **Model Version**: I am setting the model to `gemini-2.0-flash`. If you encounter a "Model Not Found" error, it may be because the 2.0 model is still in experimental/early access for your region; in that case, we can fallback to `gemini-1.5-flash`.

## Proposed Changes

### Build Configuration & Dependencies

#### [MODIFY] [libs.versions.toml](file:///C:/Users/sharath%20V/AndroidStudioProjects/StudyGenie/gradle/libs.versions.toml)
- Remove `firebase-ai` library.
- Add `google-gemini = { group = "com.google.ai.client.generativeai", name = "generativeai", version.ref = "gemini" }`.
- Ensure `gemini = "0.9.0"` is present.

#### [MODIFY] [build.gradle.kts](file:///C:/Users/sharath%20V/AndroidStudioProjects/StudyGenie/app/build.gradle.kts)
- Enable `buildConfig = true`.
- Add logic to read `GEMINI_API_KEY` from `local.properties`.
- Add `buildConfigField` for `GEMINI_API_KEY`.
- Replace `libs.firebase.ai` with `libs.google.gemini`.

#### [MODIFY] [local.properties](file:///C:/Users/sharath%20V/AndroidStudioProjects/StudyGenie/local.properties)
- Add `GEMINI_API_KEY=AQ...` (using the provided key).

---

### AI Module Refactoring

#### [MODIFY] [AiRepository.kt](file:///C:/Users/sharath%20V/AndroidStudioProjects/StudyGenie/app/src/main/java/com/studygenie/app/repository/AiRepository.kt)
- Remove all `com.google.firebase.ai` and `com.google.firebase.Firebase` imports.
- Add `com.google.ai.client.generativeai.*` imports.
- Update `GenerativeModel` initialization to use `BuildConfig.GEMINI_API_KEY` and `modelName = "gemini-2.0-flash"`.
- Clean up the error handling logic to map HTTP 401, 403, 404, 429 to user-friendly messages.

#### [MODIFY] [AiConfig.kt](file:///C:/Users/sharath%20V/AndroidStudioProjects/StudyGenie/app/src/main/java/com/studygenie/app/data/remote/AiConfig.kt)
- Remove the hardcoded API key constant as it will now be in `BuildConfig`.

---

### Verification Plan

### Automated Tests
- Run `gradle :app:assembleDebug` to verify the build and `BuildConfig` generation.

### Manual Verification
- Deploy the app and test the AI chat.
- Verify that "gemini-2.0-flash" is responding correctly.
- intentionally invalidate the API key in `local.properties` to verify the 401 error handling.

## New Feature Suggestion: **Smart Flashcard Generator**
Add a feature that allows students to highlight or paste a block of study text, and AI automatically generates a set of Question-Answer flashcards that can be saved to a local Room database for later review.
