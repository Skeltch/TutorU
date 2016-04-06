package group14.tutoru;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class editProfile extends AppCompatActivity implements AsyncResponse{

    private int classViewLength;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Profile");

        //Use bundle instead?
        String username = getIntent().getStringExtra("username");
        //These can be final because the reference to the object is not changed
        final String password = getIntent().getStringExtra("password");
        final String email = getIntent().getStringExtra("email");
        final String name = getIntent().getStringExtra("name");
        //final String firstName = getIntent().getStringExtra("first_name");
        //final String lastName = getIntent().getStringExtra("last_name");
        final String gpa = getIntent().getStringExtra("gpa");
        final String gradYear = getIntent().getStringExtra("gradYear");
        final String major = getIntent().getStringExtra("major");
        final String[] classes = getIntent().getStringArrayExtra("classes");
        final String description = getIntent().getStringExtra("description");

        //This however cannot because the length needs to change based on the user input
        classViewLength=classes.length;

        final TextView uUsername = (TextView)findViewById(R.id.username);
        final EditText uPassword = (EditText)findViewById(R.id.password);
        final EditText uEmail = (EditText)findViewById(R.id.email);
        final TextView uName = (TextView)findViewById(R.id.name);
        final EditText uGpa = (EditText)findViewById(R.id.gpa);
        final EditText uGradYear = (EditText)findViewById(R.id.graduation_year);
        final EditText uMajor = (EditText)findViewById(R.id.major);
        //final AutoCompleteTextView uClasses = (AutoCompleteTextView)findViewById(R.id.classes);
        //final MultiAutoCompleteTextView uClasses = (MultiAutoCompleteTextView)findViewById(R.id.classes);
        final EditText uDescription = (EditText)findViewById(R.id.description);




        /*
        ArrayAdapter subjectAdapter = ArrayAdapter.createFromResource(this, R.array.Subjects, android.R.layout.select_dialog_item);
        uClasses.setThreshold(1);
        uClasses.setAdapter(subjectAdapter);
        */

        uUsername.setText(username);
        uPassword.setText(password);
        uEmail.setText(email);
        uName.setText(name);
        uGpa.setText(gpa);
        uGradYear.setText(gradYear);
        uMajor.setText(major);
        //uClasses.setText(classes);
        uDescription.setText(description);

        final LinearLayout classLayout = (LinearLayout)findViewById(R.id.classLayout);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lparams.setMargins(48, 0, 0, 10);

        //final AutoCompleteTextView[] classesArray = new AutoCompleteTextView[classViewLength];
        final List<AutoCompleteTextView> classesList = new ArrayList(classViewLength+1);
        Log.e("classViewLength",Integer.toString(classViewLength));
        Log.e("classesListLength",Integer.toString(classesList.size()));
        final AutoCompleteTextView[] classesArray = classesList.toArray(new AutoCompleteTextView[classesList.size()+1]);
        final ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.Subjects, android.R.layout.select_dialog_item);

        for(int i=0; i<classViewLength; i++){
            Log.e("Message","Should not be called");
            classesArray[i] = new AutoCompleteTextView(this);
            classesArray[i].setText(classes[i]);
            classesArray[i].setLayoutParams(lparams);
            classesArray[i].setId(i);
            classLayout.addView(classesArray[i]);
            classesArray[i].setThreshold(1);
            classesArray[i].setAdapter(adapter);
            //Temp
            classesList.add(classesArray[i]);
        }
        if(classViewLength==0){
            classesArray[0] = new AutoCompleteTextView(this);
            classesArray[0].setLayoutParams(lparams);
            classesArray[0].setId(0);
            classesArray[0].setHint("None");

            classLayout.addView(classesArray[0]);
            classesArray[0].setThreshold(1);
            classesArray[0].setAdapter(adapter);
            //Temp
            classesList.add(classesArray[0]);
            classViewLength++;
        }
        Log.e("Message","Should be called");
        Log.e("message*","views created");
        //ArrayAdapter[] adapterArray = new ArrayAdapter[classViewLength];
        //ArrayList<ArrayAdapter> adapterList = new ArrayList(classViewLength);
        //ArrayAdapter[] adapterArray = adapterList.toArray(new ArrayAdapter[adapterList.size()]);

        //Maybe combine this with for loop above
        /*
        for(int i=0; i<classViewLength; i++) {
            //adapterArray[i] = ArrayAdapter.createFromResource(this, R.array.Subjects, android.R.layout.select_dialog_item);
            classesArray[i].setThreshold(1);
            //classesArray[i].setAdapter(adapterArray[i]);
            classesArray[i].setAdapter(adapter);
        }
        */
        Button addClasses = (Button)findViewById(R.id.addClass);
        if(addClasses!=null){
            addClasses.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(classViewLength>=20){
                        //Message box saying max classes
                    }
                    else{
                        Log.e("classViewLength", Integer.toString(classViewLength));
                        Log.e("before", Integer.toString(classesList.size()));
                        classesList.add(new AutoCompleteTextView(editProfile.this));
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
                    //Implement multiple classes
                    String[] subjects = getResources().getStringArray(R.array.Subjects);
                    String[] tClasses = new String[classViewLength];
                    //uClasses.getText().toString();
                    AutoCompleteTextView[] newClassesArray = classesList.toArray(new AutoCompleteTextView[classesList.size()]);
                    for (int i = 0; i < classViewLength; i++) {
                        tClasses[i] = newClassesArray[i].getText().toString();
                    }
                    boolean validClasses = true;
                    for (int i = 0; i < classViewLength; i++) {
                        if (!Arrays.asList(subjects).contains(tClasses[i]) || tClasses[i].isEmpty()) {
                            validClasses = false;
                        }
                    }
                    //if (Arrays.asList(subjects).contains(tClasses)) {
                    if (validClasses) {
                        String tPassword = uPassword.getText().toString();
                        String tEmail = uEmail.getText().toString();
                        String tGpa = uGpa.getText().toString();
                        String tGradYear = uGradYear.getText().toString();
                        String tMajor = uMajor.getText().toString();
                        String tDescription = uDescription.getText().toString();
                        //Add phone number?


                        HashMap postData = new HashMap();
                        //Check if data has changed
                        SharedPreferences settings = getSharedPreferences("Userinfo", 0);
                        postData.put("id", settings.getString("id", "").toString());
                        if (password != tPassword) {
                            postData.put("password", tPassword);
                        }
                        //Send email
                        if (email != tEmail) {
                            postData.put("email", tEmail);
                        }
                        if (gpa != tGpa) {
                            postData.put("gpa", tGpa);
                        }
                        if (gradYear != tGradYear) {
                            postData.put("graduation_year", tGradYear);
                        }
                        if (major != tMajor) {
                            postData.put("major", tMajor);
                        }
                        //Classes should be entered on a new line and come with suggestions like the search
                        //Implement multiple classes
                        //Right now the system is delete all rows and insert new ones. Need new version
                        JSONObject classJson = new JSONObject();
                        Log.e("Insertion",Integer.toString(classViewLength));
                        for (int i = 0; i < classViewLength; i++) {
                            //postData.put("classes", tClasses);
                            try {
                                if (!tClasses[i].isEmpty()) {
                                    classJson.put(Integer.toString(i), tClasses[i]);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("JSON", classJson.toString());
                        postData.put("classes", classJson.toString());
                        /*
                        if (classes[0] != tClasses) {
                            postData.put("classes", tClasses);
                        }
                        */
                        if (description != tDescription) {
                            postData.put("description", tDescription);
                        }

                        PostResponseAsyncTask editProfile = new PostResponseAsyncTask(editProfile.this, postData);
                        editProfile.execute("editProfile.php");
                        Intent i = new Intent(editProfile.this, Profile.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Class", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    public void processFinish(String output){
        Log.e("MYSQL",output);
    }


}
