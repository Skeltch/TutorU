package group14.tutoru;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

        //Getting from intent the id and name of the person the user will be reviewing
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView reviewTitle = (TextView) findViewById(R.id.reviewTitle);
        String name = getIntent().getStringExtra("name");
        if(name!=null && reviewTitle!=null){
            reviewTitle.setText(name);
        }
        String id = getIntent().getStringExtra("id");
        HashMap postData = new HashMap();
        postData.put("id",id);
        PostResponseAsyncTask profile = new PostResponseAsyncTask(this,postData);
        profile.execute("review.php");
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
                    postData.put("reviewerID", settings.getString("id", ""));
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
        if(rating!=null) {
            rating.setOnRatingBarChangeListener(
                    new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            starRating = rating;
                        }
                    }
            );
        }
    }
    @Override
    public void processFinish(String output){
        if(!output.equals("success")){
            try {
                JSONObject image = new JSONObject(output);
                String encodedImage = image.optString("imageString");
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ImageView picture = (ImageView)findViewById(R.id.picture);
                picture.setImageBitmap(decodedByte);
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}
