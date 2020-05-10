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
public class UpLoadBooksActivityTest {

    @Rule
    public ActivityTestRule<UpLoadBooksActivity> mActivityTestRule = new ActivityTestRule<>(UpLoadBooksActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction editText = onView(
                allOf(withId(R.id.txe_upload_title), isDisplayed()));
        editText.check(matches(isDisplayed()));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.txe_upload_author), isDisplayed()));
        editText2.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.btn_uploadbook), isDisplayed()));
        button.check(matches(isDisplayed()));


    }
}
