package group14.tutoru;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
/*
Activity to list all of a person's reviews
Created and debugged by Samuel Cheung
*/
public class ListReviews extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reviews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Id of the tutor we want to see the reviews for
        String id = getIntent().getStringExtra("id");
        HashMap postData = new HashMap();
        postData.put("id",id);
        //Get all reviews
        PostResponseAsyncTask list = new PostResponseAsyncTask(ListReviews.this, postData);
        list.execute("getReviews.php");
    }

    public void processFinish(String output){
        LinearLayout reviewList = (LinearLayout)findViewById(R.id.reviewList);
        //Different layouts for different parts of the review
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //Left top right bottom
        lparams.setMargins(40, 0, 0, 20);
        LinearLayout.LayoutParams entryParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        entryParams.setMargins(10, 5, 5, 10);
        try{
            JSONArray reviews = new JSONArray(output);
            //Dynamically add reviews
            for(int i=0; i<reviews.length(); i++){
                LinearLayout entry = new LinearLayout(ListReviews.this);
                //Each review will be put into an entry layout which will be added to the whole list layout
                entry.setLayoutParams(entryParams);
                //Set the round layout for entry
                entry.setBackground(ContextCompat.getDrawable(ListReviews.this, R.drawable.round_layout));
                entry.setOrientation(LinearLayout.VERTICAL);
                //Loading the reviews into strings
                String name = reviews.getJSONObject(i).optString("name");
                String title = reviews.getJSONObject(i).optString("title");
                String review = reviews.getJSONObject(i).optString("review");
                String rating = reviews.getJSONObject(i).optString("rating");
                //Setting the rating bar accordingly
                RatingBar ratingBar = new RatingBar(ListReviews.this);
                ratingBar.setNumStars(4);
                ratingBar.setIsIndicator(true);
                ratingBar.setRating(Float.parseFloat(rating));
                ratingBar.setLayoutParams(lparams);
                entry.addView(ratingBar);

                //Title of each review with name of the reviewer
                TextView header = new TextView(ListReviews.this);
                header.setTextSize(25);
                String temp = title + " by " + name;
                header.setText(temp);
                header.setLayoutParams(lparams);
                entry.addView(header);

                //Body of review
                TextView body = new TextView(ListReviews.this);
                body.setText(review);
                body.setLayoutParams(lparams);
                entry.addView(body);
                //Finally insert into full view
                reviewList.addView(entry);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }
}
