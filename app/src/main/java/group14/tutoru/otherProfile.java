package group14.tutoru;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
/*
Other profile activity
Created and debugged by Samuel Cheung
*/
public class otherProfile extends AppCompatActivity implements AsyncResponse {

    //Strings for the textviews
    String uEmail, uName, uGpa, uGradYear, uMajor, uClasses, uDescription, uPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        //Finding the width of the screen and scaling the profile picture accordingly
        /*
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = width / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        AppBarLayout bar = (AppBarLayout)findViewById(R.id.app_bar);
        bar.setMinimumHeight(dp);
        bar.getLayoutParams().height=dp;
        Log.e("dp",Integer.toString(dp));
        Log.e("imagedp",Integer.toString(bar.getHeight()));
        */


        HashMap postData = new HashMap();
        final String id = getIntent().getStringExtra("id");
        if(getIntent().getStringExtra("name")!=null) {
            setTitle(getIntent().getStringExtra("name"));
        }
        postData.put("id",id);
        PostResponseAsyncTask profile = new PostResponseAsyncTask(otherProfile.this,postData);
        profile.execute("otherProfile.php");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Button to request the tutor from their profile
        Button request = (Button) findViewById(R.id.request);
        if(request!=null) {
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Replace with proper class
                    /*
                    Intent i = new Intent(this, request.class);
                    i.putExtra("id",id);
                    startActivity(i);
                    */
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        super.onBackPressed();
        return true;
    }
    public void toReviews(View v){
        Intent i = new Intent(otherProfile.this, ListReviews.class);
        i.putExtra("id", getIntent().getStringExtra("id"));
        startActivity(i);
    }
    @Override
    public void processFinish(String output) {
        try {
            JSONObject profileT = new JSONObject(output);
            JSONObject profile = profileT.optJSONObject("info");
            JSONArray classesArray = profileT.optJSONArray("classes");
            //JSONObject tutorInfo = profileT.optJSONObject("tutorInfo");

            ImageView profilePic = (ImageView) findViewById(R.id.profile);
            TextView name = (TextView)findViewById(R.id.name);
            TextView email = (TextView)findViewById(R.id.email);
            TextView gpa = (TextView)findViewById(R.id.gpa);
            TextView gradYear = (TextView)findViewById(R.id.graduation_year);
            TextView major = (TextView)findViewById(R.id.major);
            TextView classes = (TextView)findViewById(R.id.classes);
            TextView rating = (TextView) findViewById(R.id.rating);
            TextView description = (TextView)findViewById(R.id.description);
            TextView price = (TextView) findViewById(R.id.price);

            LinearLayout emailView = (LinearLayout)findViewById(R.id.emailView);
            View emailBorder = findViewById(R.id.emailBorder);

            String encodedImage = profileT.optString("imageString");
            if(!encodedImage.isEmpty()) {
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                profilePic.setImageBitmap(decodedByte);
            }

            //Hide classes you can tutor and something about yourself if tuttee
            uName = profile.optString("first_name")+" "+profile.optString("last_name");
            name.setText(uName);
            uEmail = profile.optString("email");
            if(uEmail.isEmpty()){
                emailView.setVisibility(View.GONE);
                emailBorder.setVisibility(View.VISIBLE);
            }
            else {
                email.setText(uEmail);
            }
            uGpa = profile.optString("gpa");

            if(!profile.optString("rating").equals("null")){
                DecimalFormat temp = new DecimalFormat("#.###");
                //This function ensures that the decimal is to 3 places
                String num = Double.toString(Double.valueOf(temp.format(Float.parseFloat(profile.optString("rating")))));
                rating.setText(num);
            }

            DecimalFormat temp = new DecimalFormat("#.###");
            //This function ensures that the decimal is to 3 places
            uGpa = Double.toString(Double.valueOf(temp.format(Float.parseFloat(uGpa))));
            gpa.setText(uGpa);
            uGradYear = profile.optString("graduation_year");
            gradYear.setText(uGradYear);
            uMajor = profile.optString("major");
            major.setText(uMajor);

            getSupportActionBar().setTitle(uName);

            uClasses="";
            if(classesArray.length()==0){
                uClasses = "None";
            }
            for(int i=0; i<classesArray.length(); i++){
                uClasses += classesArray.getJSONObject(i).optString("classes");
                if(i!=classesArray.length()-1){
                    uClasses+='\n';
                }
            }
            classes.setText(uClasses);

            if (!profile.optString("price").equals("null")){
                //DecimalFormat priceFormat = new DecimalFormat("##.##");
                //This function ensures that the price is in the correct format
                uPrice = profile.optString("price");
                //uPrice = Double.toString(Double.valueOf(temp.format(Float.parseFloat(uPrice))));
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                uPrice = currencyFormatter.format(Double.parseDouble(uPrice));
                price.setText(uPrice);
            } else{
                price.setText("Not set");
            }
            description.setText("None");
            if(!profile.optString("description").equals("null")){
                uDescription = profile.optString("description");
                description.setText(uDescription);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
}
