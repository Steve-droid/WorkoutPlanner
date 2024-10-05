# Workout Planner

Workout Planner is an Android application built with Kotlin and Jetpack Compose that helps users
create and manage their workout routines. This app allows users to design custom training cycles,
track their progress, and maintain a consistent workout schedule.

## Features

- User Authentication: Secure login and registration system
- Create Training Cycles: Design personalized workout routines
- Exercise Catalog: Browse and add exercises to your workouts (powered by API Ninjas)
- Progress Tracking: Monitor your performance across different weeks
- Intuitive UI: Clean and modern interface built with Jetpack Compose

## Screenshots

<img src="screenshots/Login%20Screen.png" width="200" alt="Login Screen">
<img src="screenshots/Registration%20Screen.png" width="200" alt="Registration Screen">
<img src="screenshots/Home%20Screen.png" width="200" alt="Home Screen">
<img src="screenshots/Create%20Cycle%20Dialog.png" width="200" alt="Create Cycle Dialog">
<img src="screenshots/Create%20Cycle%20Screen.png" width="200" alt="Create Cycle Screen">
<img src="screenshots/Exercise%20Catalog.png" width="200" alt="Exercise Catalog">
<img src="screenshots/Cycle%20Detail%20Screen.png" width="200" alt="Cycle Detail Screen">
<img src="screenshots/Exercise%20editing.png" width="200" alt="Exercise editing">
<img src="screenshots/Training%20Cycle%20Detail-%20Further%20Weeks.png" width="200" alt="Training Cycle Detail- Further Weeks">
## Technologies Used

- Kotlin
- Jetpack Compose
- Firebase Authentication
- Firebase Firestore
- MVVM Architecture
- Coroutines
- Flow
- Retrofit (for API calls)

## Getting Started

To get started with this project, follow these steps:

1. Clone the repository
   ```
   git clone https://github.com/your-username/workout-planner.git
   ```

2. Open the project in Android Studio

3. Set up Firebase:
    - Create a new Firebase project
    - Add an Android app to your Firebase project
    - Download the `google-services.json` file and place it in the app directory
    - Enable Authentication and Firestore in your Firebase console

4. Set up API Ninjas:
    - Sign up for an account at [API Ninjas](https://api-ninjas.com/)
    - Obtain an API key for the Exercises API
    - Create a `local.properties` file in the root project directory if it doesn't exist
    - Add your API key to the `local.properties` file:
      ```
      api.key=your_api_key_here
      ```

5. Build and run the app on an emulator or physical device

**Note:** The exercise catalog feature requires an API key from API Ninjas. Without this key, the
app will not be able to fetch exercise data. If you're reviewing this code and don't have an API
key, please note that the exercise catalog functionality will not work.

## Project Structure

This project uses a single-module structure for simplicity. All code is contained within the `app`
module:

- `MainActivity.kt`: Entry point of the application
- `WorkoutPlanner.kt`: Main composable that handles navigation
- `SharedViewModel.kt`: ViewModel that manages the app's state and business logic
- `Data.kt`: Data classes and models
- `ApiService.kt`: Interface for API calls to API Ninjas
- `ui/theme/`: Contains theme-related files (Color, Type, Theme)
- Other Kotlin files: Various screens and components of the app

## Future Improvements

- Implement data persistence for offline usage
- Add social features to share workout routines
- Integrate with fitness tracking devices
- Implement push notifications for workout reminders
- Create a mock API for demonstration purposes to remove the API key requirement
- Consider migrating to a multi-module architecture for improved scalability and maintainability

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the [MIT License](LICENSE).
