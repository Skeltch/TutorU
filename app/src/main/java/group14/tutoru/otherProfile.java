package group14.tutoru;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class otherProfile extends AppCompatActivity implements AsyncResponse {

    String uEmail, uName, uGpa, uGradYear, uMajor, uClasses, uDescription, uPrice, role, commendNum;
    HashMap<String,String> commendMap;
    boolean empty;
    float ratingNum;
    int loadNum;
    String ratingString, id;
    profileFragment tab1;
    reviewFragment tab2;
    availabilityFragment tab3;
    ArrayList<String> stack;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_other_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Activity for Profile
        HashMap postData = new HashMap();
        if(getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
            stack = new ArrayList();
            stack.add(0,id);
        }
        else{
            stack = getIntent().getStringArrayListExtra("stack");
            if(stack.size()==0){
                finish();
            }
            else {
                id = stack.get(stack.size() - 1);
            }
        }
        if (getIntent().getStringExtra("name") != null) {
            setTitle(getIntent().getStringExtra("name"));
        }
        postData.put("id", id);
        PostResponseAsyncTask profile = new PostResponseAsyncTask(otherProfile.this, postData);
        profile.execute("otherProfile.php");

        //Button to request the tutor from their profile
        Button request = (Button) findViewById(R.id.request);
        if (request != null) {
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Replace with proper class
                /*
                Intent i = new Intent(this, request.class);
                i.putExtra("id",id);
                startActivity(i);
                */
                }
            });
        }
        //End activity for profile

        //Start activity for reviews
        commendMap = new HashMap();
        HashMap postDataReviews = new HashMap();
        //Log.e("id", id);
        postDataReviews.put("id", id);
        //Get reviews up to limit
        PostResponseAsyncTask list = new PostResponseAsyncTask(otherProfile.this, postDataReviews);
        list.execute("getReviews.php");
        //End activity for reviews
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //stack = getIntent().getStringArrayListExtra("stack");
        if(stack.size()==0){
            finish();
        }
        /*
        else if(stack.get(stack.size() - 1).equals("-1")){
            Intent resultIntent = new Intent();
            stack.remove(stack.size() - 1);
            resultIntent.putExtra("stack", stack);
            setResult(1, resultIntent);
            finish();
        }
        */
        else{
            id=stack.get(stack.size()-1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_tabbed_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(stack!=null) {
            Intent resultIntent = new Intent();
            stack.remove(stack.size() - 1);
            resultIntent.putExtra("stack", stack);
            setResult(1, resultIntent);
        }
        finish();
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    /*
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed_profile, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
    */


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //Fragments listed here
        //To replace fragment must have it available already
        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            switch(position){
                case 0:
                    tab1 = new profileFragment();
                    args.putInt("num", 0);
                    tab1.setArguments(args);
                    return tab1;
                case 1:
                    //Change to something else
                    tab2 = new reviewFragment();
                    args.putInt("num", 1);
                    tab1.setArguments(args);
                    return tab2;

                case 2:
                    tab3 = new availabilityFragment();
                    return tab3;

            }
            return null;
        }

        @Override
        public int getCount() {
            //return amount sets the amount of tabs
            if(role!=null){
                if(role.equals("Tutee")){
                    return 2;
                }
                return 2;
            }
            else{
                return 2;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Profile";
                case 1:
                    if(role!=null){
                        if(role.equals("Tutee")){
                            return "Tags";
                        }
                    }
                    else {
                        return "Reviews";
                    }
                /*
                case 2:
                    return "Availability";
                    */
            }
            return null;
        }
    }

    public void gotoReview(View view){
        Intent review = new Intent(otherProfile.this, Review.class);
        review.putExtra("id", id);
        startActivityForResult(review, 1);
    }

    public void loadMore(View view){
        if(!empty) {
            loadNum++;
            HashMap loadData = new HashMap();
            loadData.put("loadNum", Integer.toString(loadNum));
            loadData.put("id", id);
            PostResponseAsyncTask load = new PostResponseAsyncTask(otherProfile.this, loadData);
            load.execute("getReviews.php");
        }
        else{
            Toast.makeText(getApplicationContext(), "No more reviews", Toast.LENGTH_SHORT).show();
        }
    }

    public void processFinish(String output){
        try {
            if(output.equals("commend")){

            }
            Object json = new JSONTokener(output).nextValue();
            SharedPreferences settings = getSharedPreferences("Userinfo", 0);
            if(settings.getString("id", "").equals(id)) {
                findViewById(R.id.request).setVisibility(View.GONE);
                Button review = (Button) findViewById(R.id.reviewButton);
                review.setVisibility(View.GONE);
            }
            //Load profile from data
            if (json instanceof JSONObject) {
                //Get the JSON objects
                JSONObject profileT = new JSONObject(output);
                JSONObject profile = profileT.optJSONObject("info");
                JSONArray classesArray = profileT.optJSONArray("classes");

                //Get views to set data
                ImageView profilePic = (ImageView) findViewById(R.id.profile);
                TextView name = (TextView) findViewById(R.id.name);
                TextView email = (TextView) findViewById(R.id.email);
                TextView gpa = (TextView) findViewById(R.id.gpa);
                TextView gradYear = (TextView) findViewById(R.id.graduation_year);
                TextView major = (TextView) findViewById(R.id.major);
                TextView classes = (TextView) findViewById(R.id.classes);
                TextView rating = (TextView) findViewById(R.id.rating);
                TextView description = (TextView) findViewById(R.id.description);
                TextView price = (TextView) findViewById(R.id.price);
                LinearLayout emailView = (LinearLayout) findViewById(R.id.emailView);
                View emailBorder = findViewById(R.id.emailBorder);

                //If the profile's role is tutee we must change the views
                role = profile.optString("role");
                if(role.equals("Tutee")) {
                    //Hide tabs or new activity
                    TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);

                    /*
                    final View touchView = findViewById(R.id.container);
                    touchView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });
                    */
                    //Update tabs
                    mSectionsPagerAdapter.notifyDataSetChanged();
                    /*
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    ft.replace(tab2.id(), tab3, "Availability");
                    ft.commit();
                    */
                    //getSupportFragmentManager().beginTransaction().
                    //remove(getSupportFragmentManager().findFragmentById(R.id.reviewFragment)).commit();
                    if(tab2!=null) {
                        Log.e("tab2", "gone");
                        //tab2.gone();
                        tab2.setTutee();

                    }
                    if(tab3!=null){
                        Log.e("tab3","gone");
                        tab3.gone();
                    }
                }
                //Load profile picture
                String encodedImage = profileT.optString("imageString");
                if (!encodedImage.isEmpty()) {
                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    profilePic.setImageBitmap(decodedByte);
                }

                //Setting information
                uName = profile.optString("first_name") + " " + profile.optString("last_name");
                if(name!=null) {
                    name.setText(uName);
                }
                uEmail = profile.optString("email");
                if(email!=null) {
                    if (uEmail.isEmpty()) {
                        emailView.setVisibility(View.GONE);
                        emailBorder.setVisibility(View.VISIBLE);
                    } else {
                        email.setText(uEmail);
                    }
                }
                if (!profile.optString("rating").equals("null")) {
                    DecimalFormat temp = new DecimalFormat("#.###");
                    //This function ensures that the decimal is to 3 places
                    ratingNum=Float.parseFloat(profile.optString("rating"));
                    String num = Double.toString(Double.valueOf(temp.format(ratingNum)));
                    num+=" out of 5  stars";
                    ratingString=num;
                    if(rating!=null) {
                        rating.setText(num);
                    }
                }

                uGpa = profile.optString("gpa");
                //Checking if optional data sets are available and handle them accordingly
                if(!uGpa.equals("null")){
                    DecimalFormat temp = new DecimalFormat("#.###");
                    //This function ensures that the decimal is to 3 places
                    uGpa = Double.toString(Double.valueOf(temp.format(Float.parseFloat(uGpa))));
                    if(gpa!=null) {
                        gpa.setText(uGpa);
                    }
                }
                else{
                    findViewById(R.id.gpaRow).setVisibility(View.GONE);
                }
                uGradYear = profile.optString("graduation_year");
                if(!uGradYear.equals("null")){
                    gradYear.setText(uGradYear);
                }
                else{
                    findViewById(R.id.gradRow).setVisibility(View.GONE);
                }
                uMajor = profile.optString("major");
                major.setText(uMajor);
                //Title is name
                getSupportActionBar().setTitle(uName);

                //Collapsingtoolbar title is name
                //CollapsingToolbarLayout collapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                //collapsing.setTitle(uName);

                //Textview is name
                //TextView title = (TextView) findViewById(R.id.title);
                //title.setText(uName);
                //title.setShadowLayer(10, 0, 0, Color.BLACK);
                //title.setText(uName);

                //Spannable text is name
                //Spannable text = new SpannableString(uName);
                //text.setSpan(new BackgroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                //text.setSpan(new ForegroundColorSpan(Color.BLUE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                uClasses = "";
                //Check length and handle accordingly
                if (classesArray.length() == 0) {
                    uClasses = "None";
                }
                //Load classes in a simple manner
                for (int i = 0; i < classesArray.length(); i++) {
                    uClasses += classesArray.getJSONObject(i).optString("classes");
                    if (i != classesArray.length() - 1) {
                        uClasses += '\n';
                    }
                }
                classes.setText(uClasses);

                //Handle optional value with numbers accordingly
                if (!profile.optString("price").equals("null")) {
                    //DecimalFormat priceFormat = new DecimalFormat("##.##");
                    //This function ensures that the price is in the correct format
                    uPrice = profile.optString("price");
                    //uPrice = Double.toString(Double.valueOf(temp.format(Float.parseFloat(uPrice))));
                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                    uPrice = currencyFormatter.format(Double.parseDouble(uPrice));
                    price.setText(uPrice);
                } else {
                    price.setText("Not set");
                }
                description.setText("None");
                if (!profile.optString("description").equals("null")) {
                    uDescription = profile.optString("description");
                    description.setText(uDescription);
                }
            }
            //Load data for reviews
            else if (json instanceof JSONArray) {
                LinearLayout reviewList = (LinearLayout) findViewById(R.id.reviewList);
                if (loadNum == 0) {
                    LinearLayout loadSection = addReviews(output);
                    reviewList.addView(loadSection);
                }
                else{
                    //This will only happen once if there are no more reviews
                    if(output.equals("[]")) {
                        empty=true;
                        Toast.makeText(getApplicationContext(), "No more reviews", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        LinearLayout loadSection = addReviews(output);
                        loadSection.setOrientation(LinearLayout.VERTICAL);
                        loadSection.setVisibility(View.GONE);
                        reviewList.addView(loadSection);
                        expand(loadSection);
                    }

                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
    public LinearLayout addReviews(String output){
        try {
            LinearLayout loadSection = new LinearLayout(otherProfile.this);
            loadSection.setOrientation(LinearLayout.VERTICAL);

            //Getting views
            TextView averageRating = (TextView) findViewById(R.id.averageRating);
            RatingBar averageRatingBar = (RatingBar) findViewById(R.id.ratingBar);
            TextView headerBar = (TextView) findViewById(R.id.header);

            JSONArray reviews = new JSONArray(output);
            //OnClickListener for author profiles
            View.OnClickListener gotoProfile = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences settings = getSharedPreferences("Userinfo", 0);
                    //Setting the id the of the view as the id of the reviewer allows for information to be passed
                    //Through a view very simply. Since ids are unique there will be no id conflict
                    //Replaced with parents so multiple views inside entry can get id
                    String parentID = Integer.toString(((LinearLayout) (v.getParent()).getParent()).getId());
                    //String parentID = Integer.toString(((LinearLayout) v.getParent()).getId());
                    if (settings.getString("id", "").equals(parentID)) {
                        Intent profile = new Intent(otherProfile.this, Profile.class);
                        profile.putExtra("stack", stack);
                        startActivityForResult(profile, 1);
                    } else {
                        Intent i = new Intent(otherProfile.this, otherProfile.class);
                        stack.add(parentID);
                        i.putExtra("stack", stack);
                        startActivityForResult(i, 1);
                    }
                }
            };
            View.OnClickListener gotoCommend = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Add undo commend  ability
                    String parentID = Integer.toString(((LinearLayout) (v.getParent()).getParent()).getId());
                    commendNum = commendMap.get(parentID);
                    Log.e("commendNum", commendNum);
                    commendNum = Integer.toString(Integer.parseInt(commendNum) + 1);
                    commendMap.put(parentID, commendNum);
                    Log.e("newNum", commendNum);
                    String commendString = "(" + commendNum + ")" + "Commend";
                    TextView temp = (TextView) v;
                    temp.setText(commendString);
                    HashMap commendData = new HashMap();
                    commendData.put("commend", parentID);
                    commendData.put("tutorID", id);
                    PostResponseAsyncTask commend = new PostResponseAsyncTask(otherProfile.this, commendData);
                    commend.execute("review.php");
                }
            };
            View.OnClickListener gotoReport = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String parentID = Integer.toString(((LinearLayout) (v.getParent()).getParent()).getId());
                    HashMap reportData = new HashMap();
                    reportData.put("report", parentID);
                    reportData.put("tutorID", id);
                    PostResponseAsyncTask report = new PostResponseAsyncTask(otherProfile.this, reportData);
                    report.execute("review.php");
                }
            };
            averageRatingBar.setRating(ratingNum);
            averageRating.setText(ratingString);
            //Dynamically add reviews
            for (int i = 0; i < reviews.length(); i++) {
                //Set average rating
                //style="?android:attr/ratingBarStyleSmall"

                //Loading the reviews into strings
                String name = reviews.getJSONObject(i).optString("name");
                String title = reviews.getJSONObject(i).optString("title");
                String review = reviews.getJSONObject(i).optString("review");
                String rating = reviews.getJSONObject(i).optString("rating");
                String date = reviews.getJSONObject(i).optString("date");
                String reviewerID = reviews.getJSONObject(i).optString("reviewerID");
                commendNum = reviews.getJSONObject(i).optString("commends");
                //Each review will be put into an entry layout which will be added to the whole list layout
                LinearLayout entry = new LinearLayout(otherProfile.this);
                //Setting id as reviewer id so we can retrieve it later from multiple child views
                Log.e("reviewerID", reviewerID);
                entry.setId(Integer.parseInt(reviewerID));
                //Setting the rating bar accordingly
                //Different layouts for different parts of the review
                LinearLayout.LayoutParams entryParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //Left top right bottom
                entryParams.setMargins(20, 20, 0, 15);
                entry.setLayoutParams(entryParams);
                entry.setOrientation(LinearLayout.VERTICAL);
                    /*
                    ratingBar.setScaleX(Float.parseFloat("0.5"));
                    ratingBar.setScaleY(Float.parseFloat("0.5"));
                    ratingBar.setPivotX(0);
                    ratingBar.setPivotY(0);
                    */
                //Header will be in a horizontal format to fit rating bar and title
                LinearLayout header = new LinearLayout(otherProfile.this);
                entryParams.setMargins(20, 0, 0, 25);
                header.setLayoutParams(entryParams);
                LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                //Adding rating to header
                RatingBar ratingBar = new RatingBar(otherProfile.this, null, R.attr.ratingBarStyleSmall);
                ratingBar.setIsIndicator(true);
                //Making sure rating isn't null
                if (!rating.equals("null")) {
                    ratingBar.setRating(Float.parseFloat(rating));
                } else {
                    ratingBar.setVisibility(View.GONE);
                }
                ratingBar.setLayoutParams(ratingParams);
                header.addView(ratingBar);
                //Adding title of each review to header
                LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                headerParams.setMargins(10, 0, 0, 0);
                TextView headerString = new TextView(otherProfile.this);
                headerString.setTextSize(15);
                headerString.setTypeface(null, Typeface.BOLD);
                headerString.setText(title);
                headerString.setLayoutParams(headerParams);
                header.addView(headerString);
                //Add header to the entry
                entry.addView(header);

                //Add the author row
                LinearLayout authorRow = new LinearLayout(otherProfile.this);
                //Add views to author row
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lparams.setMargins(20, 0, 0, 10);
                TextView author = new TextView(otherProfile.this);
                author.setTextSize(12);
                //String temp = "By " + name + " on " + date;
                String temp = "By ";
                author.setText(temp);
                author.setLayoutParams(lparams);
                //entry.addView(author);
                authorRow.addView(author);

                //Add text with author's name that is clickable to go to their profile
                //style="?android:attr/borderlessButtonStyle"
                //android:background="@android:color/transparent"
                TextView authorButton = new TextView(otherProfile.this);
                authorButton.setText(name);
                authorButton.setTypeface(null, Typeface.BOLD);
                //Setting id as reviewer id so we can retrieve it later to go to their profile
                //authorButton.setId(Integer.parseInt(reviewerID));
                authorButton.setOnClickListener(gotoProfile);

                //Add date
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
                } catch (ParseException e) {
                    //This should never happen
                    e.printStackTrace();
                }
                String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
                String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                String year = Integer.toString(calendar.get(Calendar.YEAR));
                date = month + " " + day + ", " + year;
                authorRow.addView(authorButton);
                TextView dateView = new TextView(otherProfile.this);
                temp = " on " + date;
                dateView.setText(temp);
                authorRow.addView(dateView);
                //Add author row to entry
                entry.addView(authorRow);

                //Simply adding the text of the review to entry
                TextView body = new TextView(otherProfile.this);
                body.setText(review);
                body.setLayoutParams(lparams);
                entry.addView(body);

                //Adding buttons and interactions with each review
                //LinearLayout extraRow = new LinearLayout(otherProfile.this);
                //RelativeLayout  extraRow = new RelativeLayout(otherProfile.this);
                LinearLayout extraRow = new LinearLayout(otherProfile.this);
                extraRow.setLayoutParams(lparams);
                TextView commend = new TextView(otherProfile.this);
                //Add number of commends this review has gotten
                //String commendString="Commend";
                //Create hashmap with uniqueID?

                String commendString = "(" + commendNum + ")" + "Commend";
                commendMap.put(reviewerID, commendNum);
                commend.setText(commendString);
                commend.setTypeface(null, Typeface.BOLD);
                commend.setOnClickListener(gotoCommend);
                extraRow.addView(commend);

                //Not sure if any of these are useful
                LinearLayout.LayoutParams extra = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                extra.setMargins(50, 0, 0, 0);

                    /*
                    RelativeLayout.LayoutParams extraRelative = new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    extraRelative.setMargins(20,0,0,30);
                    */
                //extraRelative.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                TextView report = new TextView(otherProfile.this);
                report.setText("Report");
                report.setTypeface(null, Typeface.BOLD);
                report.setLayoutParams(extra);
                //report.setLayoutParams(extraRelative);
                //report.setGravity(Gravity.CENTER);
                //extraRelative.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                //Report.setLayoutParams(extraRelative);
                report.setOnClickListener(gotoReport);
                extraRow.addView(report);


                //extraRelative.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                //Add ability to comment?
                //TextView Comment = new TextView(otherProfile.this);
                //Comment.setText("Comment");
                //Comment.setOnClickListener(comment);
                entry.addView(extraRow);
                //Finally insert into partial full view
                //reviewList.addView(entry);
                loadSection.addView(entry);
            }
            //No reviews, fix views and leave proper message
            if (reviews.length() == 0) {
                averageRatingBar.setVisibility(View.GONE);
                averageRating.setVisibility(View.GONE);
                headerBar.setText("This tutor has no reviews");
            }
            return loadSection;
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }
    }
    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
