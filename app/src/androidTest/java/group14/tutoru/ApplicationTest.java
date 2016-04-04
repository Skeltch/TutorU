package group14.tutoru;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
 @RunWith(AndroidJUnit4.class)
 @LargeTest
 public class MainActivityInstrumentationTest{
	 @Rule
	 public ActivityTestRule mActivityRule = new ActivityTestRule<>(MainCtivity.class);
	 
	 @Test
	 public void sayHello(){
		 onView(withText("Say hello!")).perform(click());
		 
		 onView(withId(R.id.textView)).check(matches(withText("Hello, world!")));
	 }
 }
 /*
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
}
*/