package group14.tutoru;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import java.util.concurrent.TimeUnit;

public class activeSession extends AppCompatActivity {

    TextView countdown;
    private static final String FORMAT = "%02d:%02d:%02d";

    // Get time from session time from server otherwise set to 1 hour
    int timeMili = 3600000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        countdown = (TextView) findViewById(R.id.countdown);
        new CountDownTimer(timeMili, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                countdown.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                countdown.setText("done!");
            }
        }.start();

        // Listeners
        endSessionListener();


    }



    public void endSessionListener()
    {
        Button endButton = (Button) findViewById(R.id.sessEnd);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent liveSession = new Intent(activeSession.this, Review.class);
                liveSession.putExtra("id","5");
                liveSession.putExtra("name", "Jane Doe");
                startActivity(liveSession);
            }
        });
    }

}
