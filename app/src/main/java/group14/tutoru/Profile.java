package group14.tutoru;

import android.Manifest;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
/*
Profile page of user
Created and debugged by Samuel Cheung
*/
public class Profile extends AppCompatActivity implements AsyncResponse {

    String uUsername;
    String uEmail;
    String uName;
    String uGpa;
    String uGradYear;
    String uMajor;
    String[] uClasses;
    String uDescription;
    String uDob;
    String uPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Getting information about the user
        HashMap postData = new HashMap();
        SharedPreferences settings = getSharedPreferences("Userinfo",0);
        postData.put("id", settings.getString("id", ""));
        PostResponseAsyncTask profile = new PostResponseAsyncTask(Profile.this,postData);
        Log.e("id", settings.getString("id", ""));
        profile.useLoad(false);
        profile.execute("profile.php");

        //Setting toolbar name and other properties
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Spannable text = new SpannableString(settings.getString("first_name","") + " " + settings.getString("last_name",""));
        text.setSpan(new BackgroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new ForegroundColorSpan(Color.BLUE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setSubtitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle(text);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x303F9F));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //Button to add picture
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab!=null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        if(!Settings.System.canWrite(Profile.this)){
                            ActivityCompat.requestPermissions(Profile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
                        }
                    }
                    else{
                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
                    }
                }
            });
        }

        //Button to edit profile
        Button edit = (Button) findViewById(R.id.edit);
        if(edit!=null) {
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Profile.this, editProfile.class);
                    i.putExtra("username", uUsername);
                    i.putExtra("password", "");
                    i.putExtra("name", uName);
                    i.putExtra("email", uEmail);
                    i.putExtra("gpa", uGpa);
                    i.putExtra("gradYear", uGradYear);
                    i.putExtra("dob",uDob);
                    i.putExtra("major", uMajor);
                    i.putExtra("classes", uClasses);
                    i.putExtra("price", uPrice);
                    i.putExtra("description", uDescription);
                    startActivity(i);
                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case 5: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.e("Permission", "Granted");
                    startActivityForResult(new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
                }
                else{
                    Log.e("Permission", "Denied");
                }
            }
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
        //com.android.camera.action.CROP is not supported in many devices
        //The UI is poor anyways
        /*
        Uri picUri = data.getData();
        try{
            if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                cropIntent.setDataAndType(picUri, "image/*");
                cropIntent.putExtra("crop", "true");
                cropIntent.putExtra("aspectX", "16");
                cropIntent.putExtra("aspectY", "9");
                cropIntent.putExtra("outputX", 256*16);
                cropIntent.putExtra("outputY", 256*9);
                cropIntent.putExtra("return-data", true);
                startActivityForResult(cropIntent, 2);
            }
            else if(requestCode == 2 && resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap bitmap = null;
                bitmap = extras.getParcelable("data");
                ImageView pic = (ImageView)findViewById(R.id.profile);
                pic.setImageBitmap(bitmap);
            }
        } catch(ActivityNotFoundException e){
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
        */
        if (resultCode == Activity.RESULT_OK) {
            Bitmap bitmap;
            Bitmap scaled = null;
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                try {
                    switch (requestCode) {
                        case 1:
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            //Other options to fitting picture
                            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                            scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                            DisplayMetrics display = getResources().getDisplayMetrics();
                            //Stretches
                            //scaled = Bitmap.createScaledBitmap(bitmap, Math.round(display.widthPixels/display.density), 200, true);
                    }
                } catch (Exception e) {
                    Log.e("Error", "Exception in onActivityResult : " + e.getMessage());
                }
            }
            //Temp fix
            ImageView img = (ImageView) findViewById(R.id.profile);
            //BitmapDrawable bmDrawable = new BitmapDrawable(getResources(), bitmap);
            PostResponseAsyncTask fileUpload = new PostResponseAsyncTask(this, scaled);
            fileUpload.execute("uploadString.php");
            img.setImageBitmap(scaled);
        }
    }
    @Override
    public void processFinish(String output) {
        if(!output.equals("success") && !output.equals("failed")) {
            try {
                JSONObject profileT = new JSONObject(output);
                JSONObject profile = profileT.optJSONObject("info");
                JSONArray classesArray = profileT.optJSONArray("classes");
                if(profile.optString("gpa")=="null"){
                    SharedPreferences settings = getSharedPreferences("Userinfo",0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(Profile.this, MainScreenActivity.class));
                }
                else {
                    ImageView profilePic = (ImageView) findViewById(R.id.profile);
                    TextView username = (TextView) findViewById(R.id.username);
                    TextView password = (TextView) findViewById(R.id.password);
                    TextView name = (TextView) findViewById(R.id.name);
                    TextView email = (TextView) findViewById(R.id.email);
                    TextView gpa = (TextView) findViewById(R.id.gpa);
                    TextView gradYear = (TextView) findViewById(R.id.graduation_year);
                    TextView major = (TextView) findViewById(R.id.major);
                    TextView description = (TextView) findViewById(R.id.description);
                    TextView dob = (TextView) findViewById(R.id.dateOfBirth);
                    TextView price = (TextView) findViewById(R.id.price);

                    String encodedImage = profileT.optString("imageString");
                    if(!encodedImage.isEmpty()) {
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        profilePic.setImageBitmap(decodedByte);
                    }

                    //Hide classes you can tutor, price and description if tutee
                    uUsername = profile.optString("username");
                    username.setText(uUsername);
                    password.setText("password");
                    uName = profile.optString("first_name") + " " + profile.optString("last_name");
                    name.setText(uName);
                    uEmail = profile.optString("email");
                    email.setText(uEmail);
                    uGpa = profile.optString("gpa");
                    DecimalFormat temp = new DecimalFormat("#.###");
                    //This function ensures that the decimal is to 3 places
                    uGpa = Double.toString(Double.valueOf(temp.format(Float.parseFloat(uGpa))));
                    gpa.setText(uGpa);
                    uDob = profile.optString("dob");
                    dob.setText(uDob);
                    uGradYear = profile.optString("graduation_year");
                    gradYear.setText(uGradYear);
                    uMajor = profile.optString("major");
                    major.setText(uMajor);
                    SharedPreferences settings = getSharedPreferences("Userinfo", 0);
                    LinearLayout classLayout = (LinearLayout) findViewById(R.id.classLayout);
                    LinearLayout priceLayout = (LinearLayout) findViewById(R.id.priceLayout);
                    LinearLayout descriptionView = (LinearLayout) findViewById(R.id.descriptionView);
                    String role = settings.getString("role", "");
                    if (role.equals("Tutor") || role.equals("Both")) {
                        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lparams.setMargins(48, 0, 0, 10);
                        if (classesArray.length() != 0) {
                            uClasses = new String[classesArray.length()];
                        } else {
                            uClasses = new String[0];
                        }
                        if (classesArray.length() == 0) {
                            TextView newClass = new TextView(this);
                            newClass.setText("None");
                            newClass.setLayoutParams(lparams);
                            classLayout.addView(newClass);
                        }
                        for (int i = 0; i < classesArray.length(); i++) {
                            TextView newClass = new TextView(this);
                            uClasses[i] = classesArray.getJSONObject(i).optString("classes");
                            newClass.setText(uClasses[i]);
                            newClass.setLayoutParams(lparams);
                            classLayout.addView(newClass);
                        }
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
                        if (!profile.optString("description").equals("null")) {
                            uDescription = profile.optString("description");
                            description.setText(uDescription);
                        } else {
                            description.setText("Enter something about yourself!");
                        }
                    } else {
                        classLayout.setVisibility(View.GONE);
                        priceLayout.setVisibility(View.GONE);
                        descriptionView.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
