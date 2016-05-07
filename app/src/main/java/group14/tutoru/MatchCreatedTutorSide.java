package group14.tutoru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;



public class MatchCreatedTutorSide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_created_tutor_side);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        //Listeners
        profileViewListener();
        startChatListener();
        scheduleSessionListener();
    }



    public void profileViewListener()
    {
        Button liveSessionBtn = (Button) findViewById(R.id.viewProfile);
        liveSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewProfile = new Intent(MatchCreatedTutorSide.this, TutorProfile.class);
                startActivity(viewProfile);
            }
        });
    }

    public void startChatListener()
    {
        Button liveSessionBtn = (Button) findViewById(R.id.openChat);
        liveSessionBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent startChat = new Intent(MatchCreatedTutorSide.this, TutorProfile.class);
                startActivity(startChat);
            }
        });
    }

    public void scheduleSessionListener()
    {
        Button liveSessionBtn = (Button) findViewById(R.id.scheduleAppt);
        liveSessionBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent scheduleSession = new Intent(MatchCreatedTutorSide.this, ShiftSelection.class);
                startActivity(scheduleSession);
            }
        });
    }
}
