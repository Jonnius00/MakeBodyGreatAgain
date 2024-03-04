# MakeBodyGreatAgain

MakeBodyGreatAgain is a fitness app designed to help you keep track of your strength and endurance training programs. With a user-friendly interface, the app allows you to select different training programs, view and manage exercises, and track your progress in real-time.

<img src="https://github.com/Jonnius00/MakeBodyGreatAgain/assets/64559090/42c95377-3a0e-4859-9ffc-076ba2f016ae" width="200">

## Features

- **TopAppBar** with a custom app name and icon for easy navigation.
- **Collapsible and Expandable List Items**: Each exercise in your program can be expanded to view details or collapsed to save space.
- **Multiple Training Programs**: Comes with at least two hardcoded training programs for strength and endurance, each with a minimum of four exercises.
- **LazyColumn for Scrollable Lists**: The app uses `LazyColumn` to display exercises efficiently in a scrollable list.
- **Card UI for Exercises**: Each exercise is presented in a card format for a clean and organized look.
- **Data Management**: Exercise-related data is managed in a separate datasource file, promoting clean code and separation of concerns.
- **Interactive Components**: Interactive sliders to mark the completion of sets and exercises, updating the counters at the top of the app.
- **Custom App Icon**: A distinct app icon that stands out and represents the fitness theme of the app.
- **Internationalization Support**: Uses string resources for text elements, making it easy to translate the app into other languages.
- **Custom App Theme**: Utilizes a custom app theme with the Montserrat font and a color theme based on the primary color #ffa84e.
- **Dark Mode Support**: Includes a toggle for dark mode, making the app comfortable to use in any lighting condition.
- **Instrumentation Tests**: Ensures robustness with tests that verify the functionality of the buttons, counters, and UI displays.

## Instrumentation Tests

- Verify that the "Strength" button is enabled and the "Endurance" button is disabled after startup.
- Ensure that the UI numbers correspond with the data collections.
- Confirm that the number of ticked complete sets and exercises matches the number displayed at the top.

## Development

The application is built using Jetpack Compose, the modern toolkit for building native Android UIs. 

## Usage

To get started with the app, clone the repository to your local machine and open the project in Android Studio. 
The application can be run on an emulator or a physical device running Android.
