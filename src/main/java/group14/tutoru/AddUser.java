package group14.tutoru;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//Loading popup
import android.widget.Toast;

//Debugging use
import android.util.Log;

//For input data
import java.util.HashMap;


public class AddUser extends AppCompatActivity implements AsyncResponse {


    private EditText etUsername, etPassword, etEmail, etType, etGpa,
                    etFirst_name, etLast_name, etDob, etGraduation_year, etMajor;
    private String usern, password, email, type, gpa,
                    first_name, last_name, dob, graduation_year, major;
    boolean et;
    HashMap postData = new HashMap();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        et=false;
        //This is declared separately to allow for uniqueness checking
        etUsername = (EditText)findViewById(R.id.username);
        etEmail = (EditText) findViewById(R.id.email);
        //Would like to implement an email is already being used feature as well
        //This checks the username against our database when the user has changed into another box
        //This allows us to check for uniqueness
        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    usern = etUsername.getText().toString();
                    if(!usern.isEmpty()) {
                        postData.put("username", usern);
                        //We need new here as android only allows a task to be executed once
                        //If a user enters a username that is not unique entering a new one would
                        //Crash the app.
                        PostResponseAsyncTask checkUsername = new PostResponseAsyncTask(AddUser.this, postData);
                        checkUsername.useLoad(false);
                        checkUsername.execute("http://192.168.1.4/app/checkUsername.php");
                    }
                }
            }
        });
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public  void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    email = etEmail.getText().toString();
                    if(!email.isEmpty()){
                        postData.put("email", email);
                        PostResponseAsyncTask checkEmail = new PostResponseAsyncTask(AddUser.this, postData);
                        checkEmail.useLoad(false);
                        checkEmail.execute("http://192.168.1.4/app/checkEmail.php");
                    }
                }
            }
        });
        if (btnRegister != null) {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Getting value
                    etPassword = (EditText) findViewById(R.id.password);
                    etType = (EditText) findViewById(R.id.type);
                    etGpa = (EditText) findViewById(R.id.gpa);

                    etFirst_name = (EditText) findViewById(R.id.first_name);
                    etLast_name = (EditText) findViewById(R.id.last_name);
                    etDob = (EditText) findViewById(R.id.dob);
                    etGraduation_year = (EditText) findViewById(R.id.graduation_year);
                    etMajor = (EditText) findViewById(R.id.major);

                    //Converting to string
                    usern = etUsername.getText().toString();
                    password = etPassword.getText().toString();
                    email = etEmail.getText().toString();
                    type = etType.getText().toString();
                    gpa = etGpa.getText().toString();

                    first_name = etFirst_name.getText().toString();
                    last_name =  etLast_name.getText().toString();
                    dob = etDob.getText().toString();
                    graduation_year = etGraduation_year.getText().toString();
                    major = etMajor.getText().toString();


                    //If any are null a required field is empty
                    if(usern.isEmpty() || password.isEmpty() || email.isEmpty() || type.isEmpty() || gpa.isEmpty()
                            || first_name.isEmpty() || last_name.isEmpty() || dob.isEmpty()
                            || graduation_year.isEmpty() || major.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Required Field Missing", Toast.LENGTH_SHORT).show();
                        et=false;
                    }
                    else {
                        et=true;
                    }
                    //Debugging
                    Log.e("Value", usern);
                    Log.e("Value", password);
                    Log.e("Value", email);
                    Log.e("Value", type);
                    Log.e("Value", gpa);

                    Log.e("Value", first_name);
                    Log.e("Value", last_name);
                    Log.e("Value", dob);
                    Log.e("Value", graduation_year);
                    Log.e("Value", major);

                    //Need something to make sure that if email and username are taken they cannot continue
                    //All fields are entered
                    if(et) {
                        //Need to handle text too large
                        //Error handling
                        if (Float.valueOf(gpa) <= 4.00 && Float.valueOf(gpa) > 0 && gpa.length()<=4 && gpa.length()>0 && usern.length() <= 16
                                && usern.length() >= 6 && password.length() <= 25 && password.length() >= 6
                                && first_name.length()<=35 && last_name.length()<=35 && major.length()<=255) {
                            Toast.makeText(getApplicationContext(), "Signing up...", Toast.LENGTH_SHORT).show();

                            postData.put("username", usern);
                            postData.put("password", password);
                            postData.put("email", email);
                            postData.put("type", type);
                            postData.put("gpa", gpa);

                            postData.put("first_name", first_name);
                            postData.put("last_name", last_name);
                            postData.put("dob", dob);
                            postData.put("graduation_year", graduation_year);
                            postData.put("major", major);

                            PostResponseAsyncTask register = new PostResponseAsyncTask(AddUser.this, postData);
                            register.execute("http://192.168.1.4/app/addUser.php");
                        } else if (Float.valueOf(gpa) > 4.00 && Float.valueOf(gpa) <= 0) {
                            Toast.makeText(getApplicationContext(), "Invalid GPA", Toast.LENGTH_SHORT).show();
                        } else if(gpa.length()>4 || gpa.length()<=0){
                            Toast.makeText(getApplicationContext(), "GPA must be to 3 decimal places", Toast.LENGTH_SHORT).show();
                        } else if (usern.length() > 16) {
                            Toast.makeText(getApplicationContext(), "Username too long", Toast.LENGTH_SHORT).show();
                        } else if (usern.length() < 6) {
                            Toast.makeText(getApplicationContext(), "Username too short", Toast.LENGTH_SHORT).show();
                        }
                        //Maybe create a larger upperbound?
                        else if (password.length() > 25) {
                            Toast.makeText(getApplicationContext(), "Password too long", Toast.LENGTH_SHORT).show();
                        } else if (password.length() < 6) {
                            Toast.makeText(getApplicationContext(), "Password too short", Toast.LENGTH_SHORT).show();
                        } else if(first_name.length() >35){
                            Toast.makeText(getApplicationContext(), "First name is too long", Toast.LENGTH_SHORT).show();
                        } else if(last_name.length() > 35){
                            Toast.makeText(getApplicationContext(), "Last name is too long", Toast.LENGTH_SHORT).show();
                        } else if (major.length() > 255){
                            Toast.makeText(getApplicationContext(), "Major is too long", Toast.LENGTH_SHORT).show();
                        }
                        //Possibly error check for dob (age<x), graduation year (year<x), maybe choose from majors
                    }
                }
            });
        }
    }
    @Override
    public void processFinish(String output){
        //php file echo's the following phrases
        if(output.equals("success")){
            Toast.makeText(this, "Registering Successful", Toast.LENGTH_LONG).show();
            Intent i = new Intent(AddUser.this, PostRegistrationActivity.class);
            startActivity(i);
        }
        else if(output.equals("Unique Username")){
            Toast.makeText(this, "Username is unique!", Toast.LENGTH_SHORT).show();
        }
        else if(output.equals("Username Taken")){
            Toast.makeText(this, "Username is taken!", Toast.LENGTH_SHORT).show();
        }
        else if(output.equals("Unique Email")){
            Toast.makeText(this, "Email is unique!", Toast.LENGTH_SHORT).show();
        }
        else if(output.equals("Email is already in use")){
            Toast.makeText(this, "Email is already in use", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
        }
        //Debugging
        Log.e("output", output);
    }
}
