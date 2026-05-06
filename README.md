# Programming Portfolio - Student Application (Quizzy)

*This document confirms the development work completed for the Student Quiz Application (Quizzy), along with supporting screenshots and a reflective report.*

All required tasks have been completed and verified.

Please refer to the screenshots below for evidence of implementation.

---

## Quizzy Application Screen

| **Welcome Screen** | **Name Screen** | **Home Screen** |
|:---:|:---:|:---:|
| ![Welcome Screen](./images/Student_Application(Quizzy)/welcome_screen.png) | ![Name Screen](./images/Student_Application(Quizzy)/name_screen.png) | ![Home Screen](./images/Student_Application(Quizzy)/home_screen.png) |

| **Topic Screen** | **Quiz Screen** | **Result Screen** |
|:---:|:---:|:---:|
| ![Topic Screen](./images/Student_Application(Quizzy)/topic_screen.png) | ![Quiz Screen](./images/Student_Application(Quizzy)/quiz_screen.png) | ![Result Screen](./images/Student_Application(Quizzy)/result_screen.png) |

| **Review Screen** | **Quiz History Screen** | **Quiz History Detail Screen** |
|:---:|:---:|:---:|
| ![Review Screen](./images/Student_Application(Quizzy)/review_screen.png) | ![Quiz History Screen](./images/Student_Application(Quizzy)/quiz_history.png) | ![Quiz History Detail Screen](./images/Student_Application(Quizzy)/quiz_history_details.png) |

| **Profile Screen** | **Leaderboard Screen** |
|:---:|:---:|
| ![Profile Screen](./images/Student_Application(Quizzy)/profile_screen.png) | ![Leaderboard Screen](./images/Student_Application(Quizzy)/Leaderboard_screen.png) |

---

# REFLECTIVE REPORT

## 1. Introduction

This project involved the design and development of a mobile quiz application named **Quizzy**, built using **Kotlin** and **Jetpack Compose** in Android Studio. The main goal of the application was to provide users with an interactive quiz experience across multiple topics, while also maintaining a record of previous attempts.

The application successfully meets the **minimum requirements**, as well as **Extension 1 and Extension 2**, including review functionality and persistent data storage.

---

## 2. Technologies and Architecture

The application was developed using modern Android development practices:

- **Jetpack Compose** – Used for building a declarative UI
- **MVVM Architecture** – Ensured separation of concerns and maintainability
- **ViewModel** – Managed UI state across configuration changes
- **Room Database** – Stored quiz history locally
- **Kotlin Flow / StateFlow** – Enabled reactive data handling
- **Kotlinx Serialization** – Used for loading quiz data from JSON
- **Navigation Compose** – Handled screen navigation

These technologies allowed the application to be scalable, responsive, and easy to manage.

---

## 3. Key Features Implementation

### 3.1 Topic Selection Screen
The app allows users to select from multiple quiz topics. This was implemented using a **LazyColumn**, ensuring efficient rendering of UI components.

### 3.2 Quiz Screen
The quiz screen presents one question at a time with multiple-choice answers and a countdown timer.

A key challenge here was preventing the quiz from resetting during screen rotation. This was solved by:
- Moving question logic into the **ViewModel**
- Avoiding recomposition-based reshuffling
- Persisting state using **StateFlow**

### 3.3 Result Screen
After completing the quiz, users are shown their score along with options to restart or return home.

### 3.4 Review Screen (Extension 1)
Users can review their answers with:
- Correct answers highlighted
- Incorrect answers clearly indicated

### 3.5 Quiz History (Extension 2)
The app stores previous attempts using **Room Database**, allowing users to:
- View past scores
- Access detailed attempt history

---

## 4. Challenges and Solutions

### 4.1 Screen Rotation Issues
Initially, rotating the device caused the quiz to restart. This issue was resolved by properly using the **ViewModel** to store quiz state.

### 4.2 UI Responsiveness
Different screen sizes caused layout inconsistencies. This was improved by:
- Using flexible layouts
- Applying proper padding and spacing
- Supporting both portrait and landscape modes

### 4.3 Navigation Problems
Navigation between screens initially failed due to improper route handling. This was fixed using a well-structured **NavGraph**.

---

## 5. Use of AI in Development

AI tools were used to:
- Debug errors and fix crashes
- Improve UI design and layout structure
- Generate and refine code snippets
- Understand complex concepts like StateFlow and MVVM

AI significantly improved development speed and helped in solving problems more efficiently.

---

## 6. Improvements for Future Work

Although the application is fully functional, several improvements could be made:

- Add user authentication
- Introduce online leaderboard functionality
- Enhance UI with animations and themes
- Add sound effects and gamification features

---

## 7. Conclusion

Overall, the project was successful in meeting all requirements and provided valuable experience in modern Android development. The use of Jetpack Compose and MVVM architecture resulted in a clean and maintainable application.

The challenges faced during development helped strengthen problem-solving skills and understanding of mobile application design.

---

## Bibliography

- Google (2023) *Jetpack Compose Documentation*. Available at: https://developer.android.com/jetpack/compose  
- Google (2023) *Android Developers Guide*. Available at: https://developer.android.com  
- Kotlin Documentation (2023) *Kotlin Language Guide*. Available at: https://kotlinlang.org/docs/home.html  
