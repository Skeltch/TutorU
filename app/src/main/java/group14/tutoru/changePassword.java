package group14.tutoru;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;
/*
Activity to change password in edit profile
Created and debugged by Samuel Cheung
*/
public class changePassword extends AppCompatActivity implements AsyncResponse{

    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setupActionBar();

        //Get data from sharedpreferences
        SharedPreferences settings = getSharedPreferences("Userinfo", 0);
        id = Integer.parseInt(settings.getString("id", ""));
        Button submit = (Button) findViewById(R.id.submit);
        if(submit!=null) {
            submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText oldPass = (EditText) findViewById(R.id.oldPassword);
                    EditText newPass = (EditText) findViewById(R.id.newPassword);
                    EditText passConf = (EditText) findViewById(R.id.newPasswordConfirm);
                    //Make sure all fields are not empty
                    if(oldPass.toString().isEmpty() || newPass.toString().isEmpty() || passConf.toString().isEmpty()
                            || oldPass.toString().length()<6){
                        Toast.makeText(getApplicationContext(), "One or more fields are missing", Toast.LENGTH_SHORT).show();
                    }
                    //Confirm that both new passwords are the same
                    else if(!newPass.getText().toString().equals(passConf.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                    //Confirmed that the password length is proper
                    else if(newPass.getText().toString().length()<6){
                        Toast.makeText(getApplicationContext(), "Password length is too small", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        submit();
                    }
                }
            });
        }
    }
    //Called when button is pressed
    //First checks that the old password is correct
    public void submit(){
        EditText oldPass = (EditText) findViewById(R.id.oldPassword);
        HashMap postData = new HashMap();
        postData.put("id", Integer.toString(id));
        postData.put("password", oldPass.getText().toString());
        PostResponseAsyncTask passCheck = new PostResponseAsyncTask(changePassword.this, postData);
        passCheck.useLoad(false);
        passCheck.execute("changePassword.php");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void processFinish(String output){
        //If the old password is correct we can change the password
        if(output.equals("success")){
            //EditText newPass = (EditText) findViewById(R.id.newPassword);
            EditText passConf = (EditText) findViewById(R.id.newPasswordConfirm);
            HashMap postData = new HashMap();
            postData.put("id", Integer.toString(id));
            postData.put("newPassword", passConf.getText().toString());
            PostResponseAsyncTask changePass = new PostResponseAsyncTask(changePassword.this, postData);
            changePass.useLoad(false);
            changePass.execute("changePassword.php");
        }
        //Password change successfully
        else if(output.equals("changed")){
            Toast.makeText(getApplicationContext(), "Password Successfully Changed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(changePassword.this, Profile.class));
        }
        //Old password is incorrect
        else if(output.equals("failed")){
            Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
        }
    }
}

