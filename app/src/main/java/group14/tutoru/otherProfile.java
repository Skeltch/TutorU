package group14.tutoru;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class otherProfile extends AppCompatActivity implements AsyncResponse {

    String uEmail;
    String uName;
    String uGpa;
    String uGradYear;
    String uMajor;
    String uClasses;
    String uDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        //setTitle("Jane Doe");


        HashMap postData = new HashMap();
        String id = getIntent().getStringExtra("id");
        if(getIntent().getStringExtra("name")!=null) {
            setTitle(getIntent().getStringExtra("name"));
        }
        postData.put("id",id);
        PostResponseAsyncTask profile = new PostResponseAsyncTask(otherProfile.this,postData);
        profile.execute("profile.php");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        super.onBackPressed();
        return true;
    }
    @Override
    public void processFinish(String output) {
        Log.d("raw output*********",output);
        try {
            JSONObject profile = new JSONObject(output);
            TextView name = (TextView)findViewById(R.id.name);
            TextView email = (TextView)findViewById(R.id.email);
            TextView gpa = (TextView)findViewById(R.id.gpa);
            TextView gradYear = (TextView)findViewById(R.id.graduation_year);
            TextView major = (TextView)findViewById(R.id.major);
            TextView classes = (TextView)findViewById(R.id.classes);
            TextView description = (TextView)findViewById(R.id.description);

            //getActionBar().setTitle(profile.optString("first_name")+" "+profile.optString("last_name"));
            setTitle(profile.optString("first_name") + " " + profile.optString("last_name"));
            getSupportActionBar().setTitle(profile.optString("first_name") + " " + profile.optString("last_name"));


            /*for late
            Log.e("request","Request");
            getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            Log.e("set","set)");
            getActionBar().setTitle(profile.optString("first_name")+" "+profile.optString("last_name"));
            */

            //Hide classes you can tutor and something about yourself if tuttee
            uName = profile.optString("first_name")+" "+profile.optString("last_name");
            name.setText(uName);
            uEmail = profile.optString("email");
            email.setText(uEmail);
            uGpa = profile.optString("gpa");
            gpa.setText(uGpa);
            uGradYear = profile.optString("graduation_year");
            gradYear.setText(uGradYear);
            uMajor = profile.optString("major");
            major.setText(uMajor);
            if(profile.optString("classes")!="null"){
                Log.e("classes",profile.optString("classes"));
                uClasses = profile.optString("classes");
                classes.setText(uClasses);
            }
            if(profile.optString("description")!="null"){
                uDescription = profile.optString("description");
                description.setText(uDescription);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
}
