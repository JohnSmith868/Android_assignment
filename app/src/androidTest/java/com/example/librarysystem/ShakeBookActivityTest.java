package com.example.librarysystem;


import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ShakeBookActivityTest {

    @Rule
    public ActivityTestRule<ShakeBookActivity> mActivityTestRule = new ActivityTestRule<>(ShakeBookActivity.class);

    @Test
    public void testUIDisplay() {


        ViewInteraction button = onView(
                allOf(withId(R.id.tv_shake_book), isDisplayed()));
        button.check(matches(isDisplayed()));


    }
}
