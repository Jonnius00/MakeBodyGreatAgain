package com.example.makebodygreatagain

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.hamcrest.core.IsNot.not

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.makebodygreatagain", appContext.packageName)
    }

    @get:Rule
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun checkButtonsStateAfterStartup() {
        composeTestRule
            .onNodeWithTag("StrengthButton")
            .assertIsEnabled()

        composeTestRule
            .onNodeWithTag("EnduranceButton")
            .assertIsNotEnabled()
    }

    @Test
    fun checkNumbersDisplayedMatchData() {
        // You'll need to replace "X" and "Y" with the actual expected values
        // based on your initial data setup
        composeTestRule
            .onNodeWithTag("TotalSetsText")
            .assertTextContains("Total Sets:")
            .assertTextContains("/ 2")

        composeTestRule
            .onNodeWithTag("CompletedExercisesText")
            .assertTextContains("Completed Exercises:")
            .assertTextContains("/ 2")
   }

    @Test
    fun variationOneOfCompletedSetsAndExercises() {
        // Perform actions to complete sets/exercises
        // Check if the UI has updated accordingly
        // Click on switches/buttons to complete sets
        // Now check if the UI has updated to reflect the completion
        composeTestRule
            .onNodeWithTag("TotalSetsText")
            .assertTextContains("Total Sets: 2")
            // X should be the updated total sets count

        composeTestRule
            .onNodeWithTag("CompletedExercisesText")
            .assertTextContains("Completed Exercises: 2")
            // Y should be the updated completed exercises count

    }

}