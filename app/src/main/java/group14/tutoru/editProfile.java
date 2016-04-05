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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;

public class editProfile extends AppCompatActivity implements AsyncResponse{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Profile");

        String username = getIntent().getStringExtra("username");
        final String password = getIntent().getStringExtra("password");
        final String email = getIntent().getStringExtra("email");
        final String name = getIntent().getStringExtra("name");
        final String gpa = getIntent().getStringExtra("gpa");
        final String gradYear = getIntent().getStringExtra("gradYear");
        final String major = getIntent().getStringExtra("major");
        final String[] classes = getIntent().getStringArrayExtra("classes");
        final String description = getIntent().getStringExtra("description");

        final TextView uUsername = (TextView)findViewById(R.id.username);
        final EditText uPassword = (EditText)findViewById(R.id.password);
        final EditText uEmail = (EditText)findViewById(R.id.email);
        final TextView uName = (TextView)findViewById(R.id.name);
        final EditText uGpa = (EditText)findViewById(R.id.gpa);
        final EditText uGradYear = (EditText)findViewById(R.id.graduation_year);
        final EditText uMajor = (EditText)findViewById(R.id.major);
        final AutoCompleteTextView uClasses = (AutoCompleteTextView)findViewById(R.id.classes);
        final EditText uDescription = (EditText)findViewById(R.id.description);

        ArrayAdapter subjectAdapter = ArrayAdapter.createFromResource(this, R.array.Subjects, android.R.layout.select_dialog_item);
        uClasses.setThreshold(1);
        uClasses.setAdapter(subjectAdapter);

        uUsername.setText(username);
        uPassword.setText(password);
        uEmail.setText(email);
        uName.setText(name);
        uGpa.setText(gpa);
        uGradYear.setText(gradYear);
        uMajor.setText(major);
        //uClasses.setText(classes);
        uDescription.setText(description);


        Button submitEdit = (Button) findViewById(R.id.submitEdit);
        submitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check the class field is valid
                //Implement multiple classes
                String[] subjects = getResources().getStringArray(R.array.Subjects);
                String tClasses=uClasses.getText().toString();
                if(Arrays.asList(subjects).contains(tClasses)) {

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
                    if (classes[0] != tClasses) {
                        postData.put("classes", tClasses);
                    }
                    if (description != tDescription) {
                        postData.put("description", tDescription);
                    }

                    PostResponseAsyncTask editProfile = new PostResponseAsyncTask(editProfile.this, postData);
                    editProfile.execute("editProfile.php");
                    Intent i = new Intent(editProfile.this, Profile.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Invalid Class", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void processFinish(String output){
        Log.e("MYSQL", output);
    }


}
