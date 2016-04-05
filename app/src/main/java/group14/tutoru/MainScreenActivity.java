package group14.tutoru;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainScreenActivity extends AppCompatActivity implements AsyncResponse {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Buttons
        Button btnLogin = (Button) findViewById(R.id.btnSignin);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        
        //Click event
        if(btnLogin!=null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Signing in...", Toast.LENGTH_SHORT).show();

                    EditText text = (EditText) findViewById(R.id.username);
                    String username = text.getText().toString();
                    text = (EditText) findViewById(R.id.password);
                    String password = text.getText().toString();
                    if(!username.isEmpty() && !password.isEmpty()) {
                        HashMap postData = new HashMap();
                        postData.put("username", username);
                        postData.put("password", password);

                        PostResponseAsyncTask login = new PostResponseAsyncTask(MainScreenActivity.this, postData);
                        login.execute("login.php");
                        /*If logging in takes a while we'll move the verificaiton to the signin class
                        Intent i = new Intent(getApplicationContext(), SignIn.class);
                        startActivity(i);
                        */
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Missing Field", Toast.LENGTH_SHORT).show();
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
    @Override
    public void processFinish(String output){
        Log.d("result", output);
        try {
            JSONObject login = new JSONObject(output);
            if(login.optString("result").equals("success")){
                //Storing user information, possibly used in future
                SharedPreferences settings = getSharedPreferences("Userinfo",0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("id",login.optString("id").toString());
                //Debugging
                //editor.putString("id","1");
                editor.commit();

                Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainScreenActivity.this, SignIn.class);
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
        //Debugging on phone
        Intent i = new Intent(MainScreenActivity.this, SignIn.class);
        startActivity(i);
    }
}
