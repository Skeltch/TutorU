package group14.tutoru;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cards extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String uClass = getIntent().getStringExtra("class");
        Log.e("Classes", uClass+".");
        HashMap postData = new HashMap();
        postData.put("class", uClass);
        PostResponseAsyncTask search = new PostResponseAsyncTask(Cards.this, postData);
        search.execute("search.php");
        /*
        RecyclerView list = (RecyclerView) findViewById(R.id.cardList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(manager);

        TutorAdapter tutors = new TutorAdapter(createList(10));
        list.setAdapter(tutors);
        //tutors.notifyDataSetChanged();
        */
    }
    public void processFinish(String output){
        try {
            JSONArray tutors = new JSONArray(output);
            View.OnClickListener cardClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.GONE);
                    //Intent profile = new Intent(Cards.this, otherProfile.class);
                    //profile.putExtra("id",v.getId());
                    //startActivityForResult(profile);
                }
            };
            FrameLayout frame = (FrameLayout)findViewById(R.id.cardFrame);
            for(int i=0; i<tutors.length(); i++){
                String id = tutors.optJSONObject(i).optString("id");
                String firstName = tutors.optJSONObject(i).optString("first_name");
                String lastName = tutors.optJSONObject(i).optString("last_name");
                String gpa = tutors.optJSONObject(i).optString("gpa");
                String gradYear = tutors.optJSONObject(i).optString("graduation_year");
                String major =  tutors.optJSONObject(i).optString("major");
                String description =  tutors.optJSONObject(i).optString("description");
                String rating =  tutors.optJSONObject(i).optString("rating");
                String name = firstName + " " + lastName;

                CardView card = new CardView(Cards.this);
                LinearLayout linLayout = new LinearLayout(Cards.this);
                ImageView pic = new ImageView(Cards.this);
                RatingBar ratingBar = new RatingBar(Cards.this);
                TextView tutorText = new TextView(Cards.this);

                //LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams
                //        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                CardView.LayoutParams cardParams = new CardView.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                cardParams.setMargins(10, 10, 10, 10);
                card.setCardElevation(10);
                card.setRadius(10);
                card.setLayoutParams(cardParams);

                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                linLayout.setLayoutParams(lparams);
                linLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                imageParams.setMargins(15, 10, 0, 0);
                pic.setScaleType(ImageView.ScaleType.CENTER_CROP);

                LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                imageParams.setMargins(10, 0, 0, 0);
                ratingBar.setLayoutParams(ratingParams);

                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                imageParams.setMargins(16,0,16,16);
                tutorText.setLayoutParams(textParams);
                tutorText.setTextSize(15);

                String nameString ="Name: " + name;
                String gpaString = "";
                if(!gpa.equals("null")) {
                    DecimalFormat gpaFormat = new DecimalFormat("#.###");
                    //This function ensures that the decimal is to 3 places
                    gpa = Double.toString(Double.valueOf(gpaFormat.format(Float.parseFloat(gpa))));
                    gpaString = "\nGpa: " + gpa;
                }
                String averageRating="\nAverage Rating: ";
                if(!rating.equals("null")) {
                    DecimalFormat decTemp = new DecimalFormat("#.###");
                    //This function ensures that the decimal is to 3 places
                    String num = Double.toString(Double.valueOf(decTemp.format(Float.parseFloat(rating))));
                    ratingBar.setRating(Float.parseFloat(num));
                    averageRating += num;
                }
                else{
                    ratingBar.setRating(0);
                    averageRating="\nNo reviews yet";
                }
                String gradYearString = "";
                if(!gradYear.equals("null")){
                    gradYearString = "\nGraduation Year: " + gradYear;
                }
                String majorString = "\nMajor: " + major;
                String descriptionString = "\nDescription: " + description;
                String temp = nameString + gpaString + averageRating + gradYearString + majorString + descriptionString;
                tutorText.setText(temp);

                linLayout.addView(pic);
                linLayout.addView(ratingBar);
                linLayout.addView(tutorText);
                card.addView(linLayout);
                card.setId(Integer.parseInt(id));
                card.setOnClickListener(cardClick);
                /*
                final int targetHeight = card.getMeasuredHeight();
                Animation a = new Animation(){
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        card.getLayoutParams().height = interpolatedTime == 1
                                ? ViewGroup.LayoutParams.WRAP_CONTENT
                                : (int)(targetHeight * interpolatedTime);
                        card.requestLayout();
                    }

                    @Override
                    public boolean willChangeBounds() {
                        return true;
                    }
                };
                a.setDuration((int) (targetHeight / card.getContext().getResources().getDisplayMetrics().density));
                card.setAnimation(a);
                */
                frame.addView(card);
                /*
                //Initiating classes
                String classString = "\nClasses: ";
                //No classes, temporary text instead
                if (classesArray.length() == 0) {
                    classString += "None";
                }
                //Multiple featured tutors?
                else {
                    //classString += classesArray.getJSONObject(0).optString("classes");
                    classString += classesArray.get(0);
                }
                for (int i = 1; i < classesArray.length(); i++) {
                    //uClasses[i] = (String)classesArray.get(i);
                    classString += ", " + classesArray.get(i);
                }
                //String for reviews, price, etc
                String uPrice = "Not set";
                if (!price.equals("null")){
                    //This function ensures that the price is in the correct format
                    uPrice = profile.optString("price");
                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                    uPrice = currencyFormatter.format(Double.parseDouble(uPrice));
                }
                String price = "\nPrice per hour: ";
                price += uPrice;
                String temp = profile.optString("description");
                String description = "\nDescription: ";
                if (temp.isEmpty()) {
                    description += "None";
                } else {
                    description += temp;
                }
                temp = info + classString + price + description;
                */
            }
        } catch(JSONException e){
            e.printStackTrace();
        }

    }
    /*
    private List<TutorInfo> createList(int size) {

        List<TutorInfo> result = new ArrayList<>();
        for (int i=1; i <= size; i++) {
            TutorInfo ci = new TutorInfo();
            result.add(ci);
        }

        return result;
    }
    */
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

class TutorInfo{
    String tutorInfo, name, gpa, gradYear, major, classes, description, price;

}
