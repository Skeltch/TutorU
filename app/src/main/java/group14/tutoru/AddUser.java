package group14.tutoru;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;

//Loading popup
import android.widget.Spinner;
import android.widget.Toast;

//Debugging use
import android.util.Log;

//For input data
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/*
Activity for registering
Created and debugged by Samuel Cheung
*/
public class AddUser extends AppCompatActivity implements AsyncResponse, OnItemSelectedListener {

    //The fields that will be entered by the user
    private EditText etUsername, etPassword, etConfirmPassword, etEmail, etGpa,
                    etFirst_name, etLast_name, etGraduation_year, etMajor;
    //Strings obtained from the edittexts
    private String usern, password, confirmPassword, email, type, gpa,
                    first_name, last_name, dob, graduation_year, major;
    //Static to allow usage
    static EditText etDob;
    static String datePicker;
    //Check edit text to make sure all data is entered
    boolean et;
    //Booleans to check unique username and email
    boolean uniqueU, uniqueE;
    HashMap postData = new HashMap();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        et=false;
        //This is declared separately to allow for uniqueness checking
        etUsername = (EditText)findViewById(R.id.username);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        etConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        etDob = (EditText) findViewById(R.id.dob);
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
                        checkUsername.execute("checkUsername.php");
                    }
                }
            }
        });
        //This checks email uniqueness
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public  void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    email = etEmail.getText().toString();
                    if(!email.isEmpty()){
                        postData.put("email", email);
                        PostResponseAsyncTask checkEmail = new PostResponseAsyncTask(AddUser.this, postData);
                        checkEmail.useLoad(false);
                        checkEmail.execute("checkEmail.php");
                    }
                }
            }
        });
        //Check that the passwords match
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    if(!etConfirmPassword.getText().toString().isEmpty()
                        && !etConfirmPassword.getText().toString().equals(etPassword.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        etConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //Register
        if (btnRegister != null) {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Getting value
                    etPassword = (EditText) findViewById(R.id.password);
                    //etType = (Spinner) findViewById(R.id.type);
                    etGpa = (EditText) findViewById(R.id.gpa);

                    etFirst_name = (EditText) findViewById(R.id.first_name);
                    etLast_name = (EditText) findViewById(R.id.last_name);
                    etGraduation_year = (EditText) findViewById(R.id.graduation_year);
                    etMajor = (EditText) findViewById(R.id.major);

                    //Converting to string
                    usern = etUsername.getText().toString();
                    password = etPassword.getText().toString();
                    confirmPassword = etConfirmPassword.getText().toString();
                    email = etEmail.getText().toString();
                    //type = etType.getText().toString();
                    gpa = etGpa.getText().toString();

                    first_name = etFirst_name.getText().toString();
                    last_name = etLast_name.getText().toString();
                    dob = etDob.getText().toString();
                    graduation_year = etGraduation_year.getText().toString();
                    major = etMajor.getText().toString();


                    //If any are null a required field is empty
                    if (usern.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || type.isEmpty() || gpa.isEmpty()
                            || first_name.isEmpty() || last_name.isEmpty() || dob.isEmpty()
                            || graduation_year.isEmpty() || major.isEmpty()
                            || type.equals("Select Role")) {
                        Toast.makeText(getApplicationContext(), "Required Field Missing", Toast.LENGTH_SHORT).show();
                        et = false;
                    } else {
                        et = true;
                    }
                    //Debugging
                    /*
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
                    */

                    //All fields are entered
                    if (et) {
                        //Error handling
                        if (Float.valueOf(gpa) <= 4.00 && Float.valueOf(gpa) > 0
                                && gpa.length() <= 5 && gpa.length() > 0 && usern.length() <= 16
                                && usern.length() >= 6 && password.length() <= 128 && password.length() >= 6
                                && password.equals(confirmPassword)
                                && first_name.length() <= 35 && last_name.length() <= 35 && major.length() <= 255
                                && uniqueU && uniqueE && email.contains("@")) {
                            Toast.makeText(getApplicationContext(), "Signing up...", Toast.LENGTH_SHORT).show();

                            postData.put("username", usern);
                            postData.put("password", password);
                            postData.put("email", email);
                            postData.put("type", type);
                            postData.put("gpa", gpa);

                            postData.put("first_name", first_name);
                            postData.put("last_name", last_name);
                            postData.put("dob", datePicker);
                            postData.put("graduation_year", graduation_year);
                            postData.put("major", major);

                            PostResponseAsyncTask register = new PostResponseAsyncTask(AddUser.this, postData);
                            register.execute("addUser.php");
                        } else if (Float.valueOf(gpa) > 4.00 && Float.valueOf(gpa) <= 0) {
                            Toast.makeText(getApplicationContext(), "Invalid GPA", Toast.LENGTH_SHORT).show();
                        } else if (gpa.length() > 5 || gpa.length() <= 0) {
                            Log.e("length", String.valueOf(gpa.length()));
                            Toast.makeText(getApplicationContext(), "GPA must be to 3 decimal places", Toast.LENGTH_SHORT).show();
                        } else if (usern.length() > 16) {
                            Toast.makeText(getApplicationContext(), "Username too long", Toast.LENGTH_SHORT).show();
                        } else if (usern.length() < 6) {
                            Toast.makeText(getApplicationContext(), "Username too short", Toast.LENGTH_SHORT).show();
                        } else if (password.length() > 128) {
                            Toast.makeText(getApplicationContext(), "Password too long", Toast.LENGTH_SHORT).show();
                        } else if (password.length() < 6) {
                            Toast.makeText(getApplicationContext(), "Password too short", Toast.LENGTH_SHORT).show();
                        } else if (!password.equals(confirmPassword)) {
                            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                        } else if (first_name.length() > 35) {
                            Toast.makeText(getApplicationContext(), "First name is too long", Toast.LENGTH_SHORT).show();
                        } else if (last_name.length() > 35) {
                            Toast.makeText(getApplicationContext(), "Last name is too long", Toast.LENGTH_SHORT).show();
                        } else if (major.length() > 255) {
                            Toast.makeText(getApplicationContext(), "Major is too long", Toast.LENGTH_SHORT).show();
                        } else if (!uniqueU) {
                            Toast.makeText(getApplicationContext(), "Username is already taken!", Toast.LENGTH_SHORT).show();
                        } else if (!uniqueE) {
                            Toast.makeText(getApplicationContext(), "Email is already taken!", Toast.LENGTH_SHORT).show();
                        } else if (!email.contains("@")){
                            Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                        }
                        //This should NEVER happen
                        else {
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        //Possibly error check for dob (age<x), graduation year (year<x), maybe choose from majors
                    }
                }
            });
        }

        Spinner role = (Spinner)findViewById(R.id.type);
        List<String> list = new ArrayList<String>();
        list.add("Select Role");
        list.add("Tutor");
        list.add("Tutee");
        list.add("Both");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(dataAdapter);
        role.setOnItemSelectedListener(this);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        type = parent.getItemAtPosition(position).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0){
        type="";
    }
    //Dialog for picking date
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //Month indices start at 0 so add 1
            month++;
            String temp = year + "-" + month + "-" + day;
            datePicker = temp;
            etDob.setText(datePicker);
            Log.e("Date", temp);
        }
    }

    public void processFinish(String output){
        //php file echo's the following phrases
        if(output.equals("success")){
            Toast.makeText(this, "Registering Successful", Toast.LENGTH_LONG).show();
            Intent i = new Intent(AddUser.this, PostRegistrationActivity.class);
            startActivity(i);
        }
        else if(output.equals("Unique Username")){
            Toast.makeText(this, "Username is unique!", Toast.LENGTH_SHORT).show();
            uniqueU = true;
        }
        else if(output.equals("Username Taken")){
            Toast.makeText(this, "Username is taken!", Toast.LENGTH_SHORT).show();
            uniqueU = false;
        }
        else if(output.equals("Unique Email")){
            Toast.makeText(this, "Email is unique!", Toast.LENGTH_SHORT).show();
            uniqueE = true;
        }
        else if(output.equals("Email is already in use")){
            Toast.makeText(this, "Email is already in use", Toast.LENGTH_SHORT).show();
            uniqueE = false;
        }
        else{
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
        }
    }
}
