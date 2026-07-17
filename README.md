# 🎓 StudyGenie

**StudyGenie** is a high-performance academic mentor and productivity ecosystem built for the modern student. It integrates advanced Artificial Intelligence with robust task management and focus tools to create a seamless learning experience.

Built with **Kotlin** and **Jetpack Compose**, StudyGenie follows modern Android development practices to deliver a fast, responsive, and offline-first experience.

---

## 🚀 Key Features

### 🤖 AI Academic Mentor
Powered by **Google Gemini 2.0 Flash**, our AI assistant provides:
- **Personalized Tutoring**: Detailed explanations of complex academic concepts.
- **Code Assistance**: Generation and debugging for Java, Kotlin, Python, and more.
- **Career Guidance**: Interview preparation and placement tips.
- **Markdown Support**: Beautifully formatted responses with code highlighting.

### 📅 Intelligent Study Planner
Never miss a deadline with our integrated planner:
- **Task Management**: Organize study goals by subject and priority (High, Medium, Low).
- **Background Reminders**: Automated push notifications powered by **Android WorkManager**.
- **Local Persistence**: Full offline support using **Room Database**.

### ⏱️ Focus Mode (Pomodoro)
Boost your productivity with the scientifically proven Pomodoro technique:
- **Custom Timer**: A smooth, Canvas-based circular progress tracker.
- **Gamified Progress**: Earn XP and level up your productivity score as you focus.

### 📊 Performance Analytics
Visualize your growth with data-driven insights:
- **Completion Rates**: Track your daily and weekly progress.
- **Subject Breakdown**: See where you spend most of your study time.

---

## 🛠️ Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/compose) (Material 3)
- **Asynchronous**: Coroutines & Flows
- **Database**: Room (SQLite)
- **Background Tasks**: WorkManager
- **Backend/Auth**: Firebase (Auth & Firestore)
- **AI SDK**: [Google Generative AI SDK](https://github.com/google/generative-ai-android) (Gemini 2.0)

---

## 📦 Setup & Installation

To run this project locally, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/StudyGenie.git
   ```

2. **Add your API Key**:
   - Create a `local.properties` file in the root directory (if it doesn't exist).
   - Add your Google AI Studio API key:
     ```properties
     GEMINI_API_KEY=your_api_key_here
     ```

3. **Firebase Configuration**:
   - Add your `google-services.json` file to the `app/` directory.

4. **Build and Run**:
   - Open the project in **Android Studio (Ladybug or newer)**.
   - Sync Gradle and click **Run**.

---

## 🔐 Security
- **API Keys**: Sensitive credentials are never committed to version control; they are managed via `local.properties` and injected through `BuildConfig`.
- **User Data**: Secured via Firebase Authentication and Firestore security rules.

---

## 📝 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

*Built with ❤️ for students everywhere.*
