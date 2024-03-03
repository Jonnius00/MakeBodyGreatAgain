package com.example.makebodygreatagain

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
            //.assertIsEnabled()
    }

    @Test
    fun checkNumbersDisplayedMatchData() {
        composeTestRule
            .onNodeWithTag("TotalSetsText")
            .assertTextContains("Total Sets: 0 / 4")

        composeTestRule
            .onNodeWithTag("CompletedExercisesText")
            .assertTextContains("Completed Exercises: 0 / 4")
   }

    @Test
    fun variationOneOfCompletedSetsAndExercises() {
        // Perform actions to complete sets/exercises
        // Click on switches/buttons to complete sets
        composeTestRule
            //.onAllNodesWithText("SetSwitch").onFirst()
            //.onNodeWithText("Set 1")
            //.onNodeWithTag(switchTag)
            .onNodeWithTag("SetSwitchRunning").performClick()
        // composeTestRule.onNodeWithTag("SetSwitchSwimming1").performClick()
        // composeTestRule.onNodeWithTag("SetSwitchCycling1").performClick()
        // composeTestRule.onNodeWithTag("SetSwitchRunningRowing1").performClick()

        //composeTestRule.onAllNodes(hasTestTagStartingWith("SetSwitch")).onFirst().performClick()

        // Check if the UI has updated accordingly
        // Now check if the UI has updated to reflect the completion
        composeTestRule
            .onNodeWithTag("TotalSetsText")
            .assertTextContains("Total Sets: 1 / 4")

        composeTestRule
            .onNodeWithTag("CompletedExercisesText")
            .assertTextContains("Completed Exercises: 1 / 4")
    }
}

