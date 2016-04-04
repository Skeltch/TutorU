package group14.tutoru;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

//THIS ACTIVITY NEEDS A BACK BUTTON
public class Profile extends AppCompatActivity implements AsyncResponse {

    String uUsername;
    String uPassword;
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
        setContentView(R.layout.activity_profile);

        HashMap postData = new HashMap();
        SharedPreferences settings = getSharedPreferences("Userinfo",0);


        postData.put("id", settings.getString("id", ""));
        PostResponseAsyncTask profile = new PostResponseAsyncTask(Profile.this,postData);
        Log.e("id**********",settings.getString("id",""));
        profile.useLoad(false);
        profile.execute("profile.php");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
            }
        });

        Button edit = (Button) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(Profile.this,editProfile.class);
                i.putExtra("username",uUsername);
                i.putExtra("password",uPassword);
                i.putExtra("name",uName);
                i.putExtra("email",uEmail);
                i.putExtra("gpa",uGpa);
                i.putExtra("gradYear",uGradYear);
                i.putExtra("major",uMajor);
                i.putExtra("classes",uClasses);
                i.putExtra("description",uDescription);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if(NavUtils.shouldUpRecreateTask(this,upIntent)){
                    TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(upIntent)
                        .startActivities();
                }
                else{
                    NavUtils.navigateUpTo(this,upIntent);
                }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Uri selectedImage = data.getData();
            try {
                switch (requestCode) {
                    case 1:
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                }
            }
            catch(Exception e){
                Log.e("Error", "Exception in onActivityResult : " + e.getMessage());
            }
        }
        ImageView img = (ImageView) findViewById(R.id.profile);
        img.setImageBitmap(bitmap);
    }
    @Override
    public void processFinish(String output) {
        Log.d("raw output*********",output);
        try {
            JSONObject profileT = new JSONObject(output);
            JSONObject profile = profileT.optJSONObject("info");
            JSONArray classesArray = profileT.optJSONArray("classes");
            JSONObject tutorInfo = profileT.optJSONObject("tutorInfo");

            TextView username = (TextView)findViewById(R.id.username);
            TextView password = (TextView)findViewById(R.id.password);
            TextView name = (TextView)findViewById(R.id.name);
            TextView email = (TextView)findViewById(R.id.email);
            TextView gpa = (TextView)findViewById(R.id.gpa);
            TextView gradYear = (TextView)findViewById(R.id.graduation_year);
            TextView major = (TextView)findViewById(R.id.major);
            TextView classes = (TextView)findViewById(R.id.classes);
            TextView description = (TextView)findViewById(R.id.description);

            //Hide classes you can tutor and something about yourself if tuttee
            uUsername = profile.optString("username");
            username.setText(uUsername);
            uPassword = profile.optString("password");
            password.setText(uPassword);
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
            //Implement loop for all classes
            if(classesArray.length()>0) {
                if (classesArray.getJSONObject(0).optString("classes") != "null") {
                    Log.e("classes", classesArray.getJSONObject(0).optString("classes"));
                    uClasses = classesArray.getJSONObject(0).optString("classes");
                    classes.setText(uClasses);
                }
            }
            if(tutorInfo.optString("description")!="null"){
                Log.e("tutorInfo",tutorInfo.optString("description"));
                uDescription = tutorInfo.optString("description");
                description.setText(uDescription);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
}
