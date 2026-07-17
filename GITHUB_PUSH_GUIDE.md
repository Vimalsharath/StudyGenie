# 🚀 How to Push StudyGenie to GitHub

This guide explains how to upload your code to GitHub step-by-step, using both the Terminal and the Android Studio UI.

---

## 🛡️ Step 0: Prerequisites
1.  **GitHub Account**: Create one at [github.com](https://github.com) if you haven't.
2.  **Git Installed**: Verified (git version 2.55.0.windows.2 is present).

---

## 🛠️ Method 1: Using the Terminal (Recommended for Speed)

### 1. Initialize Git in your project
Open the **Terminal** in Android Studio (bottom tab) and run:
```bash
git init
```

### 2. Add your files
This stages all files for the first commit. Your `.gitignore` is already set up to skip private files like `local.properties`.
```bash
git add .
```

### 3. Create the first commit
```bash
git commit -m "Initial commit: StudyGenie AI Migration & Documentation"
```

### 4. Create a Repository on GitHub
1.  Go to [GitHub New Repository](https://github.com/new).
2.  Name it `StudyGenie`.
3.  Do **NOT** check "Initialize this repository with a README" (since we already have code).
4.  Click **Create repository**.

### 5. Link and Push
Copy the URL of your new repo (e.g., `https://github.com/yourusername/StudyGenie.git`) and run:
```bash
git remote add origin https://github.com/yourusername/StudyGenie.git
git branch -M main
git push -u origin main
```
*(Note: You might be asked to sign in via your browser or provide a Personal Access Token).*

---

## 🖥️ Method 2: Using Android Studio UI (Easier)

1.  **Enable Version Control**: 
    Go to `VCS` -> `Enable Version Control Integration` -> Select `Git` -> `OK`.
2.  **Share on GitHub**:
    Go to `Git` (or `VCS`) menu -> `GitHub` -> `Share Project on GitHub`.
3.  **Login**:
    Follow the prompts to log in to your GitHub account.
4.  **Finalize**:
    Enter the repository name (`StudyGenie`) and click **Share**. Android Studio will handle the Init, Add, Commit, and Push for you in one click!

---

## ⚠️ Important Security Note
> [!CAUTION]
> **Check your `.gitignore`**: I have already verified that your `local.properties` (which contains your Gemini API Key) is listed in `.gitignore`. **NEVER** remove it from that file, or your API key will be leaked to the public on GitHub.

---

## 💡 Troubleshooting
- **Permission Denied**: If you get a 403 error, make sure you are logged into the correct GitHub account in `Settings` -> `Version Control` -> `GitHub`.
- **Branch issues**: If `main` doesn't work, try `master` (though `main` is the modern standard).
