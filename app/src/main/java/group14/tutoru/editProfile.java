package group14.tutoru;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/*
Activity used to edit profile
Created and debugged by Samuel Cheung
*/
public class editProfile extends AppCompatActivity implements AsyncResponse{

    private int classViewLength;
    private int maxClasses=10;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Temporary function to catch unexepected errors that were not handled
        //This generates a simple error report and sends it to the server
        //Turn this into a generic class that will be extended in every activity
        final Thread.UncaughtExceptionHandler oldHandler =
                Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(
                            final Thread paramThread,
                            final Throwable paramThrowable
                    ) {
                        HashMap errorData = new HashMap();
                        errorData.put("thread",paramThread.getName());
                        errorData.put("exception", paramThrowable.getMessage());
                        PostResponseAsyncTask error = new PostResponseAsyncTask(editProfile.this, errorData);
                        error.execute("error.php");
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(editProfile.this);
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                        startActivity(new Intent(editProfile.this, MainPage.class));
                                    }
                                });
                        dlgAlert.setMessage("An unexpected error has occurred. We apologize for the inconvenience");
                        dlgAlert.setTitle("Oops");
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        /*
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 2000);
                        */
                        if (oldHandler != null)
                            oldHandler.uncaughtException(
                                    paramThread,
                                    paramThrowable
                            ); //Delegates to Android's error handling
                        else
                            System.exit(2); //Prevents the service/app from freezing
                    }
                });
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Change to full name
        setTitle("Profile");

        //Can possibly use bundle instead
        String username = getIntent().getStringExtra("username");
        //These can be final because the reference to the object is not changed
        final String password = getIntent().getStringExtra("password");
        final String email = getIntent().getStringExtra("email");
        final String name = getIntent().getStringExtra("name");
        //final String firstName = getIntent().getStringExtra("first_name");
        //final String lastName = getIntent().getStringExtra("last_name");
        final String gpa = getIntent().getStringExtra("gpa");
        final String dob = getIntent().getStringExtra("dob");
        final String gradYear = getIntent().getStringExtra("gradYear");
        final String major = getIntent().getStringExtra("major");
        final String[] classes = getIntent().getStringArrayExtra("classes");
        final String price = getIntent().getStringExtra("price");
        final String description = getIntent().getStringExtra("description");

        //This however cannot because the length needs to change based on the user input
        if(classes!=null) {
            classViewLength = classes.length;
        }
        else{
            classViewLength=0;
        }

        final TextView uUsername = (TextView)findViewById(R.id.username);
        final TextView uPassword = (TextView)findViewById(R.id.password);
        final TextView uEmail = (TextView)findViewById(R.id.email);
        final TextView uName = (TextView)findViewById(R.id.name);
        final EditText uGpa = (EditText)findViewById(R.id.gpa);
        final TextView uDob = (TextView) findViewById(R.id.dateOfBirth);
        final EditText uGradYear = (EditText)findViewById(R.id.graduation_year);
        final EditText uMajor = (EditText)findViewById(R.id.major);
        final EditText uPrice = (EditText)findViewById(R.id.price);
        final EditText uDescription = (EditText)findViewById(R.id.description);

        uUsername.setText(username);
        uPassword.setText(password);
        uEmail.setText(email);
        uName.setText(name);
        if(gpa.equals("null")){
            uGpa.setHint("Not Set");
        }
        else{
            uGpa.setText(gpa);
        }
        uDob.setText(dob);
        if(gradYear.equals("null")){
            uGradYear.setHint("Not Set");
        }
        else {
            uGradYear.setText(gradYear);
        }
        uPrice.setText(price);
        uMajor.setText(major);
        //Getting role from sharedpreferences
        SharedPreferences settings = getSharedPreferences("Userinfo", 0);
        id = Integer.parseInt(settings.getString("id", ""));
        String role = settings.getString("role", "");

        //Variables for the multiple class views
        final LinearLayout classLayout = (LinearLayout) findViewById(R.id.classLayout);
        final List<AutoCompleteTextView> classesList = new ArrayList(classViewLength + 1);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lparams.setMargins(48, 0, 0, 10);
        //final AutoCompleteTextView[] classesArray = classesList.toArray(new AutoCompleteTextView[classesList.size() + 1]);
        final AutoCompleteTextView[] classesArray = classesList.toArray(new AutoCompleteTextView[classViewLength + 1]);
        final ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.Subjects, android.R.layout.select_dialog_item);

        Log.e("Role", role);
        if(role.equals("Tutor") || role.equals("Both")) {
            uDescription.setText(description);
            //Debugging
            Log.e("classViewLength", Integer.toString(classViewLength));
            Log.e("classesListLength", Integer.toString(classesList.size()));

            for (int i = 0; i < classViewLength; i++) {
                classesArray[i] = new AutoCompleteTextView(this);
                classesArray[i].setText(classes[i]);
                classesArray[i].setLayoutParams(lparams);
                classesArray[i].setId(i);
                classLayout.addView(classesArray[i]);
                classesArray[i].setThreshold(1);
                classesArray[i].setAdapter(adapter);
                classesArray[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);
                classesList.add(classesArray[i]);
            }
            if (classViewLength == 0) {
                classesArray[0] = new AutoCompleteTextView(this);
                classesArray[0].setLayoutParams(lparams);
                classesArray[0].setId(0);
                classesArray[0].setHint("None");

                classLayout.addView(classesArray[0]);
                classesArray[0].setThreshold(1);
                classesArray[0].setAdapter(adapter);
                classesList.add(classesArray[0]);
                classViewLength++;
            }
        }
        else{
            classLayout.setVisibility(View.GONE);
            LinearLayout descriptionView = (LinearLayout) findViewById(R.id.descriptionView);
            descriptionView.setVisibility(View.GONE);

        }
        //Go to change pass activity
        Button changePass = (Button)findViewById(R.id.changePass);
        if(changePass!=null) {
            changePass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(editProfile.this, changePassword.class));
                }
            });
        }
        //Add class
        Button addClasses = (Button)findViewById(R.id.addClass);
        if(addClasses!=null){
            addClasses.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(classViewLength>=maxClasses){
                        //OK box instead
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(editProfile.this);
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                    }
                                });
                        dlgAlert.setMessage("We set a max amount of courses to prevent Tutors from spreading themselves too thin" +
                                ". This way Tutors won't input every class they've ever taken to gain an \"edge\" over other.");
                        dlgAlert.setTitle("Max Amount of Courses");
                        //dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        //Toast.makeText(getApplicationContext(), "Max Classes", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.e("classViewLength", Integer.toString(classViewLength));
                        Log.e("before", Integer.toString(classesList.size()));
                        AutoCompleteTextView temp = new AutoCompleteTextView(editProfile.this);
                        temp.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                        temp.setInputType(InputType.TYPE_CLASS_TEXT);
                        classesList.add(temp);
                        Log.e("add","true");
                        Log.e("classesList", Integer.toString(classesList.size()));
                        classesList.get(classViewLength).setLayoutParams(lparams);
                        classLayout.addView(classesList.get(classViewLength));
                        classesList.get(classViewLength).setThreshold(1);
                        classesList.get(classViewLength).setAdapter(adapter);
                        classViewLength++;
                        Log.e("success","success");
                    }
                }
            });
        }

        Button submitEdit = (Button) findViewById(R.id.submitEdit);
        if(submitEdit!=null) {
            submitEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Check the class field is valid
                    String[] subjects = getResources().getStringArray(R.array.Subjects);
                    String[] tClasses = new String[classViewLength];
                    AutoCompleteTextView[] newClassesArray = classesList.toArray(new AutoCompleteTextView[classesList.size()]);

                    String tGpa = uGpa.getText().toString();
                    String tGradYear = uGradYear.getText().toString();
                    String tMajor = uMajor.getText().toString();
                    for (int i = 0; i < classViewLength; i++) {
                        tClasses[i] = newClassesArray[i].getText().toString();
                    }
                    boolean validClasses = true;
                    for (int i = 0; i < classViewLength; i++) {
                        if (!Arrays.asList(subjects).contains(tClasses[i]) && !tClasses[i].isEmpty()) {
                            validClasses = false;
                        }
                    }
                    boolean validInput = false;
                    try {
                        if(tMajor.isEmpty()){
                            Toast.makeText(getApplicationContext(), "Field Major is missing", Toast.LENGTH_SHORT).show();
                        }
                        else if(tGradYear.isEmpty() || tGradYear.length()!=4){
                            Toast.makeText(getApplicationContext(), "Invalid Graduation Year", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            validInput=true;
                        }
                    } catch(NumberFormatException e){
                        Toast.makeText(getApplicationContext(), "Invalid Number.", Toast.LENGTH_SHORT).show();
                    }
                    if (validClasses && validInput) {
                        String tPassword = uPassword.getText().toString();
                        String tEmail = uEmail.getText().toString();
                        String tPrice = uPrice.getText().toString();
                        String tDescription = uDescription.getText().toString();
                        //Add phone number?
                        boolean validNums = true;
                        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                        //Try parsing the numbers
                        if(!tGpa.isEmpty()) {
                            try {
                                Float.parseFloat(tGpa);
                                Integer.parseInt(tGradYear);
                                tPrice = currencyFormatter.parse(tPrice).toString();
                                if (tGpa.length() != 5 || Float.parseFloat(tGpa) > 4 || Float.parseFloat(tGpa) <= 0) {
                                    //Log.e(Float.toString(gpa.length()), gpa);
                                    Toast.makeText(getApplicationContext(), "Invalid GPA. GPA must be to 3 decimals", Toast.LENGTH_SHORT).show();
                                    validNums=false;
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(getApplicationContext(), "Invalid Numbers", Toast.LENGTH_SHORT).show();
                                validNums = false;
                            } catch (ParseException e) {
                                //It will work regardless since the input is limited to numbers
                                //This is just to make android happy
                                //Toast.makeText(getApplicationContext(), "Invalid Price", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            validNums=true;
                        }
                        if (validNums) {
                            HashMap postData = new HashMap();
                            //Check if data has changed
                            postData.put("id", Integer.toString(id));
                                if (!tGpa.equals(gpa)) {
                                    if (tGpa.isEmpty()) {
                                        postData.put("gpa", "NULL");
                                    } else {
                                        postData.put("gpa", tGpa);
                                    }
                                }
                            if (!gradYear.equals(tGradYear)) {
                                if(tGradYear.isEmpty()){
                                    postData.put("graduation_year","NULL");
                                }
                                else {
                                    postData.put("graduation_year", tGradYear);
                                }
                            }
                            if (!major.equals(tMajor)) {
                                postData.put("major", tMajor);
                            }
                            //Classes should be entered on a new line and come with suggestions like the search
                            int notEmpty = 0;
                            JSONArray classJson = new JSONArray();
                            for (int i = 0; i < classViewLength; i++) {
                                //postData.put("classes", tClasses);
                                try {
                                    if (!tClasses[i].isEmpty()) {
                                        Log.e("Classes", tClasses[i]);
                                        Log.e("classViewLength", Integer.toString(classViewLength));
                                        classJson.put(notEmpty, tClasses[i]);
                                        notEmpty++;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            postData.put("classes", classJson.toString());
                            if (!tPrice.equals(price)) {
                                if (tPrice.isEmpty()) {
                                    postData.put("price", "NULL");
                                } else {
                                    postData.put("price", tPrice);
                                }
                            }
                            if (!tDescription.equals(description)) {
                                postData.put("description", tDescription);
                            }
                            //Debugging
                            for(Object temp : postData.keySet()){
                                String key = temp.toString();
                                String value = postData.get(key).toString();
                                Log.e(key,value);
                            }

                            PostResponseAsyncTask editProfile = new PostResponseAsyncTask(editProfile.this, postData);
                            editProfile.useLoad(false);
                            editProfile.execute("editProfile.php");
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Class", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
    @Override
    public void processFinish(String output){
        finish();
        startActivity(new Intent(editProfile.this, Profile.class));
    }


}
