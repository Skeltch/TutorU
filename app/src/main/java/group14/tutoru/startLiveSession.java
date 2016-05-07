package group14.tutoru;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class startLiveSession extends AppCompatActivity implements AsyncResponse {

    ListView apptList;
    boolean sessionBegun = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_live_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apptList = (ListView) findViewById(R.id.apptList);
        String[] items = getResources().getStringArray(R.array.appointments);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        apptList.setAdapter(adapter);
        apptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String value = (String) apptList.getItemAtPosition(itemPosition);

                if(itemPosition >= 0)
                {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(startLiveSession.this);
                    builder1.setMessage("Are you sure you're ready to start the session?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    sessionBegun = true;
                                    HashMap postData = new HashMap();
                                    postData.put("sessionStatus", sessionBegun);
                                    PostResponseAsyncTask startSession = new PostResponseAsyncTask(startLiveSession.this, postData);
                                    startSession.execute("http://192.168.1.4/app/search.php");

                                    Intent launchSession = new Intent(startLiveSession.this, activeSession.class);
                                    startActivity(launchSession);
                                    dialog.cancel();

                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();


                }


            }
        });

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
                editor.commit();

                Toast.makeText(this, "Begin Session", Toast.LENGTH_LONG).show();
                Intent i = new Intent(startLiveSession.this, activeSession.class);
                startActivity(i);
            }

        }
        catch(JSONException e){
            e.printStackTrace();
        }
        //Debugging on phone
        //Intent i = new Intent(MainScreenActivity.this, SignIn.class);
        //startActivity(i);
    }

}
