package group14.tutoru;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class tabbedProfile extends AppCompatActivity implements AsyncResponse {

    String uEmail, uName, uGpa, uGradYear, uMajor, uClasses, uDescription, uPrice;
    float ratingNum;
    String ratingString, id;
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
        setContentView(R.layout.activity_tabbed_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        id = getIntent().getStringExtra("id");
        if(getIntent().getStringExtra("name")!=null) {
            setTitle(getIntent().getStringExtra("name"));
        }
        postData.put("id", id);
        PostResponseAsyncTask profile = new PostResponseAsyncTask(tabbedProfile.this, postData);
        profile.execute("otherProfile.php");

        //Button to request the tutor from their profile
        Button request = (Button) findViewById(R.id.request);
        if(request!=null) {
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
        HashMap postDataReviews = new HashMap();
        Log.e("id", id);
        postDataReviews.put("id",id);
        //Get all reviews
        PostResponseAsyncTask list = new PostResponseAsyncTask(tabbedProfile.this, postDataReviews);
        list.execute("getReviews.php");
        //End activity for reviews
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed_profile, menu);
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
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    profileFragment tab1 = new profileFragment();
                    return tab1;
                case 1:
                    //Change to calendar
                    reviewFragment tab2 = new reviewFragment();
                    return tab2;
                case 2:
                    availabilityFragment tab3 = new availabilityFragment();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Profile";
                case 1:
                    return "Reviews";
                case 2:
                    return "Availability";
            }
            return null;
        }
    }

    public void gotoReview(View view){
        Intent review = new Intent(tabbedProfile.this, Review.class);
        review.putExtra("id", id);
        startActivity(review);
    }

    public void processFinish(String output){
        try {
            Object json = new JSONTokener(output).nextValue();
            if (json instanceof JSONObject) {
                JSONObject profileT = new JSONObject(output);
                JSONObject profile = profileT.optJSONObject("info");
                JSONArray classesArray = profileT.optJSONArray("classes");

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

                String encodedImage = profileT.optString("imageString");
                if (!encodedImage.isEmpty()) {
                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    profilePic.setImageBitmap(decodedByte);
                }

                //Hide classes you can tutor and something about yourself if tuttee
                uName = profile.optString("first_name") + " " + profile.optString("last_name");
                name.setText(uName);
                uEmail = profile.optString("email");
                if (uEmail.isEmpty()) {
                    emailView.setVisibility(View.GONE);
                    emailBorder.setVisibility(View.VISIBLE);
                } else {
                    email.setText(uEmail);
                }
                if (!profile.optString("rating").equals("null")) {
                    DecimalFormat temp = new DecimalFormat("#.###");
                    //This function ensures that the decimal is to 3 places
                    ratingNum=Float.parseFloat(profile.optString("rating"));
                    String num = Double.toString(Double.valueOf(temp.format(ratingNum)));
                    num+=" out of 5  stars";
                    ratingString=num;
                    rating.setText(num);
                }

                uGpa = profile.optString("gpa");
                if(!uGpa.equals("null")){
                    DecimalFormat temp = new DecimalFormat("#.###");
                    //This function ensures that the decimal is to 3 places
                    uGpa = Double.toString(Double.valueOf(temp.format(Float.parseFloat(uGpa))));
                    gpa.setText(uGpa);
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
                getSupportActionBar().setTitle(uName);

                uClasses = "";
                if (classesArray.length() == 0) {
                    uClasses = "None";
                }
                for (int i = 0; i < classesArray.length(); i++) {
                    uClasses += classesArray.getJSONObject(i).optString("classes");
                    if (i != classesArray.length() - 1) {
                        uClasses += '\n';
                    }
                }
                classes.setText(uClasses);

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
            } else if (json instanceof JSONArray) {
                LinearLayout reviewList = (LinearLayout) findViewById(R.id.reviewList);
                //Different layouts for different parts of the review
                //Rating bar needs its own parameters to space out text and stars equally
                /*
                LinearLayout.LayoutParams entryParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                entryParams.setMargins(25, 0, 0, 10);
                */


                TextView averageRating = (TextView) findViewById(R.id.averageRating);
                RatingBar averageRatingBar = (RatingBar) findViewById(R.id.ratingBar);
                ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, 3);

                JSONArray reviews = new JSONArray(output);
                //Dynamically add reviews
                for (int i = 0; i < reviews.length(); i++) {
                    //Set average rating
                    averageRatingBar.setRating(ratingNum);
                    averageRating.setText(ratingString);
                    //style="?android:attr/ratingBarStyleSmall"
                    LinearLayout entry = new LinearLayout(tabbedProfile.this);
                    LinearLayout header = new LinearLayout(tabbedProfile.this);
                    //Each review will be put into an entry layout which will be added to the whole list layout
                    LinearLayout.LayoutParams entryParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //Left top right bottom
                    entryParams.setMargins(20, 20, 0, 15);
                    entry.setLayoutParams(entryParams);
                    entryParams.setMargins(20,0,0,25);
                    header.setLayoutParams(entryParams);

                    //Set the round layout for entry
                    //entry.setBackground(ContextCompat.getDrawable(tabbedProfile.this, R.drawable.round_layout));
                    entry.setOrientation(LinearLayout.VERTICAL);
                    //Loading the reviews into strings
                    String name = reviews.getJSONObject(i).optString("name");
                    String title = reviews.getJSONObject(i).optString("title");
                    String review = reviews.getJSONObject(i).optString("review");
                    String rating = reviews.getJSONObject(i).optString("rating");
                    String date = reviews.getJSONObject(i).optString("date");
                    //Setting the rating bar accordingly
                    RatingBar ratingBar = new RatingBar(tabbedProfile.this,null,R.attr.ratingBarStyleSmall);
                    ratingBar.setIsIndicator(true);
                    if(!rating.equals("null")) {
                        ratingBar.setRating(Float.parseFloat(rating));
                    }
                    else{
                        ratingBar.setVisibility(View.GONE);
                    }
                    /*
                    ratingBar.setScaleX(Float.parseFloat("0.5"));
                    ratingBar.setScaleY(Float.parseFloat("0.5"));
                    ratingBar.setPivotX(0);
                    ratingBar.setPivotY(0);
                    */
                    LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ratingBar.setLayoutParams(ratingParams);
                    header.addView(ratingBar);

                    //Title of each review with name of the reviewer
                    LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    headerParams.setMargins(10, 0, 0, 0);
                    TextView headerString = new TextView(tabbedProfile.this);
                    headerString.setTextSize(15);
                    headerString.setTypeface(null, Typeface.BOLD);
                    //String temp = title + " by " + name;
                    headerString.setText(title);
                    headerString.setLayoutParams(headerParams);
                    header.addView(headerString);
                    entry.addView(header);

                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lparams.setMargins(20, 0, 0, 10);
                    TextView author = new TextView(tabbedProfile.this);
                    author.setTextSize(12);
                    //Add date
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
                    } catch(ParseException e){
                        e.printStackTrace();
                    }
                    String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
                    String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                    String year = Integer.toString(calendar.get(Calendar.YEAR));
                    date = month + " " + day + ", " + year;
                    String temp = "By " + name + " on " + date;
                    author.setText(temp);
                    author.setLayoutParams(lparams);
                    entry.addView(author);
                    /*
                    LinearLayout authorRow = new LinearLayout(tabbedProfile.this);
                    authorRow.addView(author);

                    //style="?android:attr/borderlessButtonStyle"
                    //android:background="@android:color/transparent"
                    Button authorButton = new Button(tabbedProfile.this);
                    authorButton.setText(name);
                    authorButton.setBackgroundColor(Color.TRANSPARENT);
                    authorButton.setMinHeight(0);
                    authorButton.setMinWidth(0);

                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    buttonParams.setMargins(0, -10, 0, -5);
                    authorButton.setLayoutParams(buttonParams);
                    //authorButton.setTextSize(8);
                    authorRow.addView(authorButton);
                    entry.addView(authorRow);
                    */

                    //Body of review
                    TextView body = new TextView(tabbedProfile.this);
                    body.setText(review);
                    body.setLayoutParams(lparams);
                    entry.addView(body);
                    //Line break (necessary?)
                    /*
                    if(i!=reviews.length()-1) {
                        View lineBreak = new View(tabbedProfile.this);
                        lineBreak.setLayoutParams(viewParams);
                        lineBreak.setBackgroundColor(ContextCompat.getColor(tabbedProfile.this, R.color.colorPrimary));
                        entry.addView(lineBreak);
                    }
                    */
                    //Finally insert into full view
                    reviewList.addView(entry);
                }
                if(reviews.length()==0){
                    averageRatingBar.setVisibility(View.GONE);
                    averageRating.setText("This tutor has no reviews");
                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
}
