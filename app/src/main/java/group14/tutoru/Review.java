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
/*
Review activity for reviewing tutors
Created and debugged by Samuel Cheung
*/
public class Review extends AppCompatActivity implements AsyncResponse{


    private float starRating=0;
    private int tutorID;
    private String tutorName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView reviewTitle = (TextView) findViewById(R.id.reviewTitle);
        //Get information passed through intent
        tutorName = getIntent().getStringExtra("name");
        if(tutorName!=null && reviewTitle!=null){
            reviewTitle.setText(tutorName);
        }
        String id = getIntent().getStringExtra("id");
        tutorID = Integer.parseInt(id);
        HashMap postData = new HashMap();
        //Information required for starting activity which includes getting the picture
        //And checking that the user has not left a review previously
        postData.put("tutorID",id);
        postData.put("reviewerID", getSharedPreferences("Userinfo",0).getString("id",""));
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
                    Log.e("values", Integer.toString(tutorID) + "1" + settings.getString("id","") + "2" + name + "3"
                        + title.getText().toString() + "4" + review.getText().toString() + "5" + Float.toString(starRating));
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
    //Rating bar
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
        try {
            JSONObject review = new JSONObject(output);
            //Outputs to direct the activity
            if (review.optString("activity").equals("review")) {
                Intent i = new Intent(Review.this, otherProfile.class);
                i.putExtra("id", Integer.toString(tutorID));
                i.putExtra("name", tutorName);
                startActivity(i);
            }
            else if(review.optString("activity").equals("redirect")) {
                //Temporary
                Toast.makeText(this, "You have already reviewed this tutor", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Review.this, MainPage.class));
                //Allow tutee to edit review
                //Intent i = new Intent(Review.this, editReview.class);
                //startActivity(i);
            } else {
                    JSONObject image = new JSONObject(output);
                    String encodedImage = image.optString("imageString");
                    if (!encodedImage.isEmpty()) {
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        ImageView picture = (ImageView) findViewById(R.id.picture);
                        picture.setImageBitmap(decodedByte);
                    }
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }
}
