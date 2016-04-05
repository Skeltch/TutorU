package group14.tutoru;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncResponse {

    int featuredId;
    String featuredName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Search bar placeholder");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        PostResponseAsyncTask featured = new PostResponseAsyncTask(MainPage.this);
        featured.useLoad(false);
        featured.execute("featured.php");

        Button featuredButton = (Button) findViewById(R.id.featured);
        featuredButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(MainPage.this, otherProfile.class);
                i.putExtra("id",String.valueOf(featuredId));
                i.putExtra("name",String.valueOf(featuredName));
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Creating issues when closing the sidebar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent i = new Intent(MainPage.this, Profile.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(MainPage.this, search.class);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {
            PostResponseAsyncTask login = new PostResponseAsyncTask(MainPage.this);
            login.execute("tutors.php");
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void processFinish(String output){
        Log.e("output", output);
        try {
            JSONObject profileT = new JSONObject(output);
            JSONObject profile = profileT.optJSONObject("info");
            JSONArray classesArray = profileT.optJSONArray("classes");
            JSONObject tutorInfo = profileT.optJSONObject("tutorInfo");
            TextView featuredTutor = (TextView)findViewById(R.id.tutorInfo);
            featuredId=Integer.parseInt(profile.optString("id"));
            featuredName=profile.optString("first_name" + " " + "last_name");
            String info="Name: "+profile.optString("first_name")+" "+profile.optString("last_name")
                    +"\nGpa: "+profile.optString("gpa")
                    +"\nMajor: "+profile.optString("major")
                    +"\nGraduation Year: "+profile.optString("graduation_year");

            //Initiating classes
            String classString="\nClasses: ";
            //No classes, temporary text instead
            if(classesArray.length()==0){
                classString+="None";
            }
            //For one, don't add comma
            else{
                classString+=classesArray.getJSONObject(0).optString("classes");
            }
            for(int i=1; i<classesArray.length(); i++) {
                classString+=", " + classesArray.getJSONObject(i).optString("classes");
            }
            //String for reviews, price, etc
            String temp=tutorInfo.optString("description");
            String description = "\nDescription: ";
            if(temp.isEmpty()){
                description+="None";
            }
            else{
                description+=temp;
            }
            temp = info + classString + description;
            featuredTutor.setText(temp);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
}
