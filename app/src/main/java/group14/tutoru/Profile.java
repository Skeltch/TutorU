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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Profile extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        HashMap postData = new HashMap();
        SharedPreferences settings = getSharedPreferences("Userinfo",0);


        postData.put("id",settings.getString("id","").toString());
        PostResponseAsyncTask profile = new PostResponseAsyncTask(Profile.this,postData);
        Log.e("id**********",settings.getString("id","").toString());
        profile.execute("http://192.168.1.4/app/profile.php");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
            }
        });
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
            JSONObject profile = new JSONObject(output);
            TextView username = (TextView)findViewById(R.id.username);
            TextView password = (TextView)findViewById(R.id.password);
            TextView name = (TextView)findViewById(R.id.name);
            TextView email = (TextView)findViewById(R.id.email);
            TextView gpa = (TextView)findViewById(R.id.gpa);
            TextView gradYear = (TextView)findViewById(R.id.graduation_year);
            TextView major = (TextView)findViewById(R.id.major);

            username.setText(profile.optString("username"));
            password.setText(profile.optString("password"));
            name.setText(profile.optString("first_name")+" "+profile.optString("last_name"));
            email.setText(profile.optString("email"));
            gpa.setText(profile.optString("gpa"));
            gradYear.setText(profile.optString("graduation_year"));
            major.setText(profile.optString("major"));



            //username.setText(username.getText().toString()+profile.optString("username"));
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
}
