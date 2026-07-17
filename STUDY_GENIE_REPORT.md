# 🎓 StudyGenie: Comprehensive Technical Project Report

## 1. Executive Summary
**StudyGenie** is a high-performance academic mentor and productivity ecosystem designed to revolutionize how students manage their academic lives. It integrates a high-intelligence AI mentor powered by Google's latest Gemini 2.0 models with a robust suite of productivity tools, including a Pomodoro focus timer, intelligent study planner, and real-time performance analytics. Built with modern Android development practices, the application offers a seamless, offline-first experience with secure cloud synchronization via Firebase.

---

## 2. Architectural Framework
The project is built upon the **MVVM (Model-View-ViewModel)** architectural pattern, following **Clean Architecture** principles to ensure scalability, testability, and a clear separation of concerns.

### 🏛️ Layered Separation
*   **UI Layer (Jetpack Compose)**: Utilizes a declarative approach with Material 3 components. The UI is entirely state-driven, reacting to changes emitted by the ViewModels via `collectAsStateWithLifecycle`.
*   **Presentation Layer (ViewModels)**: Manages UI state and business logic. It leverages **Kotlin Coroutines** for asynchronous operations and **StateFlow** to expose observable state.
*   **Data Layer (Repository Pattern)**:
    *   **Single Source of Truth**: Repositories abstract the source of data (Local Room vs. Remote Firebase).
    *   **Room Database**: Provides offline-first capability using SQLite via Room 2.8.4.
    *   **Firebase Integration**: Handles Cloud Auth and Firestore sync.
    *   **WorkManager**: Manages background study reminders.

---

## 3. Core Module Deep-Dive

### 🤖 3.1 AI Academic Mentor (Gemini 2.0 Integration)
The AI Assistant is the primary value proposition of StudyGenie, utilizing the **Google AI Studio Developer API**.
*   **Engine**: Integrated with `gemini-2.0-flash` for ultra-low latency.
*   **Technical Implementation**: 
    *   Uses a `GenerativeModel` with custom `systemInstruction` to maintain a "Academic Mentor" persona.
    *   **Chat History**: Managed via `generativeModel.startChat()`, preserving context across multiple interactions.
    *   **Markdown Rendering**: A custom `parseMarkdown` utility in `AiComponents.kt` processes bold, italics, and inline code.
    *   **Code Execution Blocks**: High-fidelity `CodeBlock` component with language highlighting and "Copy to Clipboard" functionality.
*   **Security**: API keys are injected via `BuildConfig` from `local.properties`, ensuring they are never committed to version control.

### 📅 3.2 Intelligent Study Planner & WorkManager
A comprehensive task management system tailored for academics.
*   **Data Persistence**: Uses `TaskEntity` in Room with fields for `subject`, `priority` (High/Medium/Low), and `deadline`.
*   **Background Workers**: When a task is added, a `ReminderWorker` is enqueued via `WorkManager`. It calculates the `initialDelay` based on the task's date and time, ensuring a high-priority notification is delivered exactly when needed, even if the app process is dead.
*   **Reactive Updates**: UI updates in real-time as tasks are marked completed, using Room's `Flow` support.

### ⏱️ 3.3 Focus Mode (Pomodoro Ecosystem)
Designed for deep work sessions.
*   **State Management**: `PomodoroViewModel` uses a Coroutine-based timer that updates every 1000ms.
*   **Visual Engineering**: A custom `Canvas` implementation in `FocusScreen.kt` draws a smooth arc representing the progress. It uses `StrokeCap.Round` and Material 3 `Tertiary` colors for a premium aesthetic.

### 📊 3.4 User Progression & Gamification
*   **User Stats**: Tracked via `UserEntity` (Room). Fields include `XP`, `Level`, `Streak`, and `ProductivityScore`.
*   **Experience Logic**: XP is rewarded upon task completion and focus session finishes, promoting a "level-up" mentality for studying.

---

## 4. Technical Stack & Security

### 🛠️ Technology Table
| Component | Technology | Version |
| :--- | :--- | :--- |
| **Language** | Kotlin | 2.2.10 |
| **UI** | Jetpack Compose (M3) | BOM 2026.02 |
| **AI SDK** | Google Generative AI | 0.9.0 |
| **Local DB** | Room | 2.8.4 |
| **Auth** | Firebase Auth | 34.16.0 |
| **Scheduler** | WorkManager | 2.11.2 |

### 🔐 Security Measures
1.  **BuildConfig Injection**: Sensitive Gemini API keys are never hardcoded.
2.  **Firebase Security Rules**: User data is siloed by UID in Firestore.
3.  **Encrypted Local Data**: Support for encrypted Room databases is ready for future implementation.
4.  **Dark Mode Optimization**: Comprehensive theme support with accessibility-compliant contrast ratios (fixed `onTertiaryContainer` visibility for error bubbles).

---

## 5. Detailed Component Mapping (Nook & Corner)

### 📁 Data Models (`/data/local`)
*   **TaskEntity**: `id`, `subject`, `title`, `date`, `time`, `priority`, `isCompleted`.
*   **UserEntity**: `id` (Firebase UID), `name`, `email`, `level`, `xp`, `streak`, `coins`.

### 🧩 UI Components (`/presentation/components`)
*   **TypingIndicator**: An animated `InfiniteTransition` that mimics AI thinking behavior.
*   **ChatBubble**: Handles two-way communication with distinct shapes for User vs. AI.
*   **SuggestionChips**: Quick-action buttons to jumpstart common student queries.

---

## 6. Implementation Challenges & Solutions
*   **Challenge**: Firebase AI vs. Google AI SDK conflicts.
    *   **Solution**: Migrated entirely to the official Google AI Studio SDK to avoid paid Vertex AI costs and ensure compatibility with `gemini-2.0-flash`.
*   **Challenge**: Real-time progress tracking in Compose.
    *   **Solution**: Used `StateFlow` in ViewModels to drive UI state, ensuring efficient recompositions.

---

## 7. Future Roadmap
1.  **Smart Flashcards**: AI-powered automatic generation of study cards.
2.  **Voice Interaction**: Integrated speech-to-text for hands-free study queries.
3.  **Collaborative Planner**: Shared task boards for group projects using Firestore.

> **Final Note**: StudyGenie represents a fusion of modern AI and practical engineering, providing a production-ready template for educational tools in the Android ecosystem.
