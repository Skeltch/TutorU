package group14.tutoru;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.LinearLayout;
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
    //String uFirstName;
    //String uLastName;
    String uGpa;
    String uGradYear;
    String uMajor;
    String[] uClasses;
    String uDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        HashMap postData = new HashMap();
        SharedPreferences settings = getSharedPreferences("Userinfo",0);
        //Max class length? 20?
        //uClasses = new String[20];

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
        if(fab!=null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
                }
            });
        }

        Button edit = (Button) findViewById(R.id.edit);
        if(edit!=null) {
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Profile.this, editProfile.class);
                    i.putExtra("username", uUsername);
                    i.putExtra("password", uPassword);
                    i.putExtra("name", uName);
                    //i.putExtra("first_name",uFirstName);
                    //i.putExtra("last_name",uLastName);
                    i.putExtra("email", uEmail);
                    i.putExtra("gpa", uGpa);
                    i.putExtra("gradYear", uGradYear);
                    i.putExtra("major", uMajor);
                    //uClasses Array
                    i.putExtra("classes", uClasses);
                    i.putExtra("description", uDescription);
                    startActivity(i);
                }
            });
        }
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
    //Next, work on profile picture, replace button with invisible button on picture, cropping, scaling, etc
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
        //Temp fix
        ImageView img = (ImageView) findViewById(R.id.profile);
        //BitmapDrawable bmDrawable = new BitmapDrawable(getResources(), bitmap);
        int nh = (int) (bitmap.getHeight() * (512.0/bitmap.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap,512,nh,true);
        img.setImageBitmap(scaled);
    }
    @Override
    public void processFinish(String output) {
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
            //TextView classes = (TextView)findViewById(R.id.classes);
            TextView description = (TextView)findViewById(R.id.description);

            //Hide classes you can tutor and something about yourself if tutee
            uUsername = profile.optString("username");
            username.setText(uUsername);
            uPassword = profile.optString("password");
            password.setText(uPassword);
            uName = profile.optString("first_name")+" "+profile.optString("last_name");
            //uFirstName = profile.optString("first_name");
            //uLastName = profile.optString("last_name");
            name.setText(uName);
            uEmail = profile.optString("email");
            email.setText(uEmail);
            uGpa = profile.optString("gpa");
            gpa.setText(uGpa);
            uGradYear = profile.optString("graduation_year");
            gradYear.setText(uGradYear);
            uMajor = profile.optString("major");
            major.setText(uMajor);

            LinearLayout classLayout = (LinearLayout)findViewById(R.id.classLayout);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lparams.setMargins(48,0,0,10);
            //For the first class, must overwrite the current textview
            /*
            if(classesArray.length()>0) {
                if (classesArray.getJSONObject(0).optString("classes") != "null") {
                    Log.e("classes", classesArray.getJSONObject(0).optString("classes"));
                    uClasses = classesArray.getJSONObject(0).optString("classes");
                    classes.setText(uClasses);
                }
            }
            */
            if(classesArray.length()!=0) {
                uClasses = new String[classesArray.length()];
            }
            else{
                uClasses= new String[1];
            }
            if(classesArray.length()==0){
                TextView newClass = new TextView(this);
                newClass.setText("None");
                newClass.setLayoutParams(lparams);
                classLayout.addView(newClass);
            }
            for(int i=0; i<classesArray.length(); i++){
                TextView newClass = new TextView(this);
                uClasses[i]=classesArray.getJSONObject(i).optString("classes");
                newClass.setText(uClasses[i]);
                newClass.setLayoutParams(lparams);
                classLayout.addView(newClass);
            }

            if(tutorInfo.optString("description")!="null"){
                Log.e("tutorInfo",tutorInfo.optString("description"));
                uDescription = tutorInfo.optString("description");
                description.setText(uDescription);
            }
            else{
                description.setText("Enter something about yourself!");
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
}
