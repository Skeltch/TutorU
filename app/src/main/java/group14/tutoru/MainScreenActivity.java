package group14.tutoru;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
/*
Login page that lets you go to registration page as well
Created and debugged by Samuel Cheung
*/
public class MainScreenActivity extends AppCompatActivity implements AsyncResponse {

    private int attempts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        SharedPreferences settings = getSharedPreferences("Userinfo", 0);

        if(settings.contains("id")){
            startActivity(new Intent(this, MainPage.class));
        }

        //Buttons
        Button btnLogin = (Button) findViewById(R.id.btnSignin);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        
        //Click event
        if(btnLogin!=null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attempts++;
                    if(attempts>=5){
                        Toast.makeText(MainScreenActivity.this, "Max number of attempts reached", Toast.LENGTH_LONG).show();
                        //Reset after certain time
                        new CountDownTimer(30000, 1000){
                            public void onTick(long m){
                                //do nothing
                            }
                            public void onFinish(){
                                attempts=0;
                            }
                        }.start();
                    }
                    else {
                        EditText text = (EditText) findViewById(R.id.username);
                        String username = text.getText().toString();
                        text = (EditText) findViewById(R.id.password);
                        String password = text.getText().toString();
                        if (!username.isEmpty() && !password.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Signing in...", Toast.LENGTH_SHORT).show();
                            HashMap postData = new HashMap();
                            postData.put("username", username);
                            postData.put("password", password);

                            PostResponseAsyncTask login = new PostResponseAsyncTask(MainScreenActivity.this, postData);
                            login.execute("login.php");
                        /*
                        If logging in takes a while we'll move the verification to the signin class
                        Intent i = new Intent(getApplicationContext(), SignIn.class);
                        startActivity(i);
                        */
                        } else {
                            Toast.makeText(getApplicationContext(), "Missing Field", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        
        if (btnRegister != null) {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainScreenActivity.this, AddUser.class);
                    startActivity(i);
                }
            });
        }
    }
    public void onBackPressed(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
    @Override
    public void processFinish(String output){
        try {
            JSONObject login = new JSONObject(output);
            if(login.optString("result").equals("success")){
                //Storing user information, possibly used in future
                SharedPreferences settings = getSharedPreferences("Userinfo",0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("id",login.optString("id"));
                editor.putString("role",login.optString("role"));
                editor.putString("first_name",login.optString("first_name"));
                editor.putString("last_name",login.optString("last_name"));
                editor.commit();

                Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainScreenActivity.this, MainPage.class);
                startActivity(i);
            }
            else if(login.optString("result").equals("Login Failed")){
                Toast.makeText(this, "Failed, Incorrect Username or Password", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Failed could not connect to server",Toast.LENGTH_LONG).show();
            }
        }
        catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Failed, error connecting to server",Toast.LENGTH_LONG).show();
        }
    }
}
