package group14.tutoru;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//This class is used to show the post-registration page to allow for successful registration and to give the users a message
public class PostRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_registration);
        Thread welcomeThread = new Thread() {
            public void run() {
                try {
                    super.run();
                    //Wait for 5 seconds
                    sleep(5000);
                } catch (Exception e) {

                } finally {
                    //Go back to main screen to allow the user to login
                    Intent i = new Intent(PostRegistrationActivity.this, MainScreenActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
