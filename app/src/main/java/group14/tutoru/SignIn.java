package group14.tutoru;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

//What happens after you sign in, replace with something more reasonable
public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
            Thread LogIn = new Thread() {
            public void run() {
                try {
                    super.run();
                    //Temporary
                    sleep(5000);
                } catch (Exception e) {

                } finally {
                    //Go back to main screen to allow the user to login
                    //This isn't really needed, for future reference, this is how you change a UI element
                    /*
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        }
                    });
                    */
                    Intent i = new Intent(SignIn.this, MainPage.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        LogIn.start();
    }
}
