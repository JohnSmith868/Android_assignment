package com.example.librarysystem;


import android.app.Activity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
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
public class HomeActivityTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void HomeActivityTest() {


        ViewInteraction button = onView(
                allOf(withId(R.id.home_toolbar), isDisplayed()));
        button.check(matches(isDisplayed()));



    }

    @Test
    public void testFragment() {


        onView(allOf(withId(R.id.drawer_layout), isDisplayed())).check(matches(isDisplayed())).perform(DrawerActions.open());
        onView(withId(R.id.nav_bar_menu))
                .perform(NavigationViewActions.navigateTo(R.id.nav_item_mybooks));


        onView(allOf(withId(R.id.my_book_rv), isDisplayed())).check(matches(isDisplayed()));

    }
    @Test
    public void testNavigatoMap() {


        onView(allOf(withId(R.id.drawer_layout), isDisplayed())).check(matches(isDisplayed())).perform(DrawerActions.open());
        onView(withId(R.id.nav_bar_menu))
                .perform(NavigationViewActions.navigateTo(R.id.nav_item_branches));


        onView(allOf(withId(R.id.map_btn_show_route), isDisplayed())).check(matches(isDisplayed()));

    }

    @Test
    public void testHome() {


        onView(allOf(withId(R.id.drawer_layout), isDisplayed())).check(matches(isDisplayed())).perform(DrawerActions.open());
        onView(withId(R.id.nav_bar_menu))
                .perform(NavigationViewActions.navigateTo(R.id.nav_item_home));




    }

    @Test
    public void testNavUploadBook() {


        onView(allOf(withId(R.id.drawer_layout), isDisplayed())).check(matches(isDisplayed())).perform(DrawerActions.open());
        onView(withId(R.id.nav_bar_menu))
                .perform(NavigationViewActions.navigateTo(R.id.nav_item_uploadbook));




    }
    @Test
    public void testNavLogout() {


        onView(allOf(withId(R.id.drawer_layout), isDisplayed())).check(matches(isDisplayed())).perform(DrawerActions.open());
        onView(withId(R.id.nav_bar_menu))
                .perform(NavigationViewActions.navigateTo(R.id.nva_item_logout));




    }
    @Test
    public void testShake() {


        onView(allOf(withId(R.id.drawer_layout), isDisplayed())).check(matches(isDisplayed())).perform(DrawerActions.open());
        onView(withId(R.id.nav_bar_menu))
                .perform(NavigationViewActions.navigateTo(R.id.nav_item_shakebook));




    }


}
