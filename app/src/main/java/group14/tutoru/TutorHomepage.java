package group14.tutoru;

import android.content.Intent;
import android.net.sip.SipManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ethanwong.tutoru.MainActivity;

public class TutorHomepage extends AppCompatActivity {

    public SipManager mSipManager = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_homepage_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button message = (Button)findViewById(R.id.button);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TutorHomepage.this, MainActivity.class));
            }
        });



        // Session Initiation Protocol
        if (mSipManager == null)
        {
            mSipManager = SipManager.newInstance(this);
        }

        // Call Listeners
        selectShiftsListener();
        profilelistener();
        liveSessionListener();
        matchListListener();
    }





    // Listener for shifts button
    public void selectShiftsListener()
    {
        Button shiftBtn = (Button) findViewById(R.id.shiftBtn);
        shiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shiftSelector = new Intent(TutorHomepage.this, ShiftSelection.class);
                startActivity(shiftSelector);
            }
        });
    }

    public void profilelistener()
    {
        Button profBtn = (Button) findViewById(R.id.profBtn);
        profBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Intent profPage = new Intent(TutorHomepage.this, TutorProfile.class);
                //startActivity(profPage);
            }
        });
    }

    public void liveSessionListener()
    {
        Button liveSessionBtn = (Button) findViewById(R.id.liveSessBtn);
        liveSessionBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent liveSession = new Intent(TutorHomepage.this, startLiveSession.class);
                startActivity(liveSession);
            }
        });
    }

    public void matchListListener()
    {
        Button matchBtn = (Button) findViewById(R.id.matchBtn);
        matchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent liveSession = new Intent(TutorHomepage.this, MatchList.class);
                startActivity(liveSession);
            }
        });
    }
}
