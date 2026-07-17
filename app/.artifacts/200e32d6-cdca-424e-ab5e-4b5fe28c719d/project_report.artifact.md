# 🎓 StudyGenie: Technical Project Report

**StudyGenie** is a high-performance academic mentor and productivity ecosystem built for the modern student. It integrates advanced Artificial Intelligence with robust task management and focus tools to create a seamless learning experience.

---

## 🏗️ Architectural Overview

The project follows the **Clean Architecture** philosophy implemented via the **MVVM (Model-View-ViewModel)** pattern. This ensures a clear separation of concerns, high testability, and easy maintainability.

### Layered Structure
- **UI Layer (Jetpack Compose)**: Declarative UI components that react to state changes in the ViewModel.
- **Presentation Layer (ViewModel)**: Manages UI state, handles user interactions, and communicates with the Data layer using Kotlin Coroutines and Flows.
- **Data Layer (Repository & Sources)**:
    - **Local**: Room Database for offline-first capability and high-speed data access.
    - **Remote**: Firebase Firestore for cloud synchronization and Google AI Studio for intelligent processing.
    - **Worker**: WorkManager for reliable background task scheduling (e.g., study reminders).

---

## 🚀 Core Features & Modules

### 1. 🤖 AI Academic Mentor
- **Engine**: Powered by **Google AI Studio (Gemini 2.0 Flash)**.
- **Capabilities**: Academic tutoring, code generation, career guidance, and complex analogy explanations.
- **Technical Detail**: Uses the official `com.google.ai.client.generativeai` SDK. Features include system instructions for persona maintenance, conversation history (chat memory), and Markdown support for equations and code blocks.

### 2. 📅 Intelligent Study Planner
- **Functionality**: CRUD operations for study tasks categorized by subject and priority.
- **Local Sync**: Tasks are stored in a **Room Database** (`TaskEntity`) for instant access.
- **Cloud Sync**: Optional synchronization with **Firebase Firestore** for cross-device consistency.
- **Reminders**: Integrated with **Android WorkManager** to trigger push notifications via `ReminderWorker`.

### 3. ⏱️ Focus Mode (Pomodoro Ecosystem)
- **Logic**: Implements the standard 25-minute Pomodoro technique.
- **Visuals**: A custom `Canvas`-based circular progress indicator in the `FocusScreen`.
- **State Management**: Uses `TimerState` (Running, Paused, Finished) to manage the lifecycle of the focus session.

### 4. 📊 Performance Analytics
- **Metrics**: Total tasks, completion rates, and subject-wise effort breakdown.
- **Visualization**: Data-driven insights calculated in `AnalyticsViewModel` and presented via modern UI components.

### 5. 🔐 Security & User Management
- **Authentication**: Robust login and registration system using **Firebase Authentication**.
- **Data Protection**: Sensitive API keys are managed through `local.properties` and injected via `BuildConfig` to prevent exposure in version control.

---

## 🛠️ Technical Stack

| Component | Technology |
| :--- | :--- |
| **Language** | Kotlin 2.2.10 |
| **UI Toolkit** | Jetpack Compose (Material 3) |
| **Concurrency** | Kotlin Coroutines & Flows |
| **Database** | Room 2.8.4 |
| **AI SDK** | Google Generative AI SDK 0.9.0 |
| **Backend** | Firebase (Auth, Firestore) |
| **Scheduling** | WorkManager |
| **Networking** | OKHttp / Google GenAI SDK |

---

## 📁 Project Directory Mapping

```text
app/src/main/java/com/studygenie/app/
├── data/
│   ├── local/             # Room DB, DAOs, Entities, Workers
│   ├── remote/            # AI Configuration & Remote APIs
│   └── model/             # Core Data Models (Task, User)
├── presentation/
│   ├── screens/           # Main UI Screens (AI, Planner, Dashboard)
│   ├── components/        # Reusable UI Widgets (TaskCard, ChatBubble)
│   └── navigation/        # Compose Navigation Logic
├── repository/            # Single source of truth for data access
├── viewmodel/             # UI State & Business Logic
└── ui/theme/              # Material 3 Design System (Colors, Typography)
```

---

## 🏆 Key Differentiators
- **Offline-First Design**: Users can manage tasks and focus without an active internet connection.
- **AI-Native**: Intelligent assistance is baked into the core workflow, not just as a plugin.
- **Dynamic Theming**: Full support for Android 12+ Dynamic Color palettes.
- **Low Latency**: Uses `gemini-2.0-flash` for near-instant AI responses.

> [!NOTE]
> This project is designed to be highly scalable. The repository pattern allows for switching data sources (e.g., from Firestore to a different Backend-as-a-Service) with minimal changes to the UI layer.
