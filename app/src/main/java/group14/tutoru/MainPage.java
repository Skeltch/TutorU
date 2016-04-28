package group14.tutoru;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;


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

        //setTitle("Search bar placeholder");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences settings = getSharedPreferences("Userinfo",0);
        String role = settings.getString("role", "");
        String name = settings.getString("first_name","") + " " + settings.getString("last_name","");

        View header = navigationView.getHeaderView(0);
        TextView primary = (TextView) header.findViewById(R.id.primaryHeader);
        TextView secondary = (TextView) header.findViewById(R.id.secondaryHeader);

        if(primary!=null && secondary!=null) {
            primary.setText(name);
            secondary.setText(role);
        }

        if(!role.equals("Tutee")) {
            navigationView.getMenu().findItem(R.id.advanced_search).setVisible(false);
            navigationView.getMenu().findItem(R.id.temp).setVisible(true);
        }
        else{
            navigationView.getMenu().findItem(R.id.advanced_search).setVisible(true);
            navigationView.getMenu().findItem(R.id.temp).setVisible(false);
        }

        /*
        SearchView searchView = (SearchView) findViewById(R.id.search);
        String[] subjects = getResources().getStringArray(R.array.Subjects);
        */
        final AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.search);
        ArrayAdapter subjectAdapter = ArrayAdapter.createFromResource(this, R.array.Subjects, android.R.layout.select_dialog_item);
        search.setThreshold(1);
        search.setAdapter(subjectAdapter);
        //Start new activity on item selected or on pressing return
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Intent i = new Intent(MainPage.this, PerformSearch.class);
                i.putExtra("searchTerm", search.getText());
                startActivity(i);
                return true;
            }
        });
        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainPage.this, PerformSearch.class);
                i.putExtra("searchTerm", parent.getItemAtPosition(position).toString());
                startActivity(i);
            }
        });

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
        //default android code causes issues
        //This checks whether the user just logged in
        minimizeApp();
    }

    public void minimizeApp(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*
        getMenuInflater().inflate(R.menu.main_page, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        */

        /*
        //Because searchView is only compatible with cursoradapters
        String[] columnNames = {"_id","text"};
        MatrixCursor cursor = new MatrixCursor(columnNames);
        String[] array = getResources().getStringArray(R.array.Subjects);
        String[] temp = new String[2];
        int id=0;
        for(String item : array){
            temp[0] = Integer.toString(id++);
            temp[1] = item;
            cursor.addRow(temp);
        }
        String[] from ={"text"};
        CursorAdapter subjectsAdapter = new SimpleCursorAdapter()
        */
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.Subjects, android.R.layout.select_dialog_item);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_title));
        //searchView.setOnQueryTextListener(MainPage.this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        item.expandActionView();
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
        } else if (id == R.id.advanced_search) {
            Intent i = new Intent(MainPage.this, search.class);
            startActivity(i);
        } else if (id == R.id.notifications){
            //Notifications
            Intent i = new Intent(MainPage.this, Review.class);
            i.putExtra("id","6");
            startActivity(i);
        } else if (id == R.id.schedule) {
            //Schedule
        } else if (id == R.id.friends) {
            //Tutor/Tutee management
        } else if (id == R.id.settings) {
            //Settings
        } else if (id == R.id.log_out) {
            //Do more
            //Clear all shared preferences to log out
            SharedPreferences settings = getSharedPreferences("Userinfo",0);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(MainPage.this, MainScreenActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void processFinish(String output){
        try {
            JSONObject profileT = new JSONObject(output);
            JSONObject profile = profileT.optJSONObject("info");
            JSONArray classesArray = profileT.optJSONArray("classes");
            //JSONObject tutorInfo = profileT.optJSONObject("tutorInfo");
            TextView featuredTutor = (TextView)findViewById(R.id.tutorInfo);
            featuredId=Integer.parseInt(profile.optString("id"));
            featuredName=profile.optString("first_name" + " " + "last_name");
            if(profile.optString("gpa").isEmpty() || profile.optString("gpa")=="null"){
                //do nothing
            }
            else {
                DecimalFormat gpa = new DecimalFormat("#.###");
                //This function ensures that the decimal is to 3 places
                String uGpa = Double.toString(Double.valueOf(gpa.format(Float.parseFloat(profile.optString("gpa")))));
                String info = "Name: " + profile.optString("first_name") + " " + profile.optString("last_name")
                        + "\nGpa: " + uGpa
                        + "\nMajor: " + profile.optString("major")
                        + "\nGraduation Year: " + profile.optString("graduation_year");
                //Initiating classes
                String classString = "\nClasses: ";
                //No classes, temporary text instead
                if (classesArray.length() == 0) {
                    classString += "None";
                }
                //Multiple featured tutors?
                else {
                    classString += classesArray.getJSONObject(0).optString("classes");
                }
                for (int i = 1; i < classesArray.length(); i++) {
                    classString += ", " + classesArray.getJSONObject(i).optString("classes");
                }
                //String for reviews, price, etc
                String temp = profile.optString("description");
                String description = "\nDescription: ";
                if (temp.isEmpty()) {
                    description += "None";
                } else {
                    description += temp;
                }
                temp = info + classString + description;
                featuredTutor.setText(temp);

                String encodedImage = profileT.optString("imageString");
                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ImageView profilePic = (ImageView) findViewById(R.id.profilePic);
                profilePic.setImageBitmap(decodedByte);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
}
