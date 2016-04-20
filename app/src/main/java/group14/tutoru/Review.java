package group14.tutoru;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.HashMap;

public class Review extends AppCompatActivity implements AsyncResponse{


    private float starRating=0;
    private int reviewerID;
    private int tutorID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Getting from intent the id of the person the user will be reviewing
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
         */
        listenerForRatingBar();
        Button submit = (Button)findViewById(R.id.Submit);
        if(submit!=null) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText title = (EditText) findViewById(R.id.title);
                    EditText review = (EditText) findViewById(R.id.review);
                    HashMap postData = new HashMap();

                    SharedPreferences settings = getSharedPreferences("Userinfo",0);
                    postData.put("tutorID", Integer.toString(tutorID));
                    postData.put("reviewerID", tings.getString("id", ""));
                    String name = settings.getString("first_name","") + " " + settings.getString("last_name","");
                    postData.put("name", name);
                    postData.put("title",title.getText().toString());
                    postData.put("review",review.getText().toString());
                    postData.put("rating", Float.toString(starRating));
                    PostResponseAsyncTask send = new PostResponseAsyncTask(Review.this, postData);
                    send.execute("review.php");
                }
            });
        }
    }
    public void listenerForRatingBar(){
        RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
        rating.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        starRating = rating;
                    }
                }
        );

    }
    @Override
    public void processFinish(String output){
        //do stuff
    }
}
