package group14.tutoru;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class search extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    Spinner pricespinner;
    AutoCompleteTextView subject;
    RatingBar rating;
    String selectedSubject = "Subject";
    String priceBand = "Less than $10";
    int ratingPicked = 0;
    int subjectPicked = 0;
    int pricePicked = 0;
    float starRating;

    private static RatingBar rating_b;


    /*public void listenerForRatingBar(){
        rating_b = (RatingBar) findViewById(R.id.ratingBar);
        rating_b.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener(){
                    @Override
                    public void onRatingChanged(RatingBar rating)
                }
        );
    } */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize advanced options button
        Button advancedOptionsBtn = (Button) findViewById(R.id.advancedButton);
        advancedOptionsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Intent advancedOptions = new Intent(search.this, advancedSearch.class);
                //startActivity(advancedOptions);
            }
        });


        listenerForRatingBar();

        // Initialize search button
        Button searchBtn = (Button) findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(pricePicked > 0 && subjectPicked > 0 && ratingPicked > 0)
                {
                    HashMap postData = new HashMap();
                    postData.put("Subject", selectedSubject);
                    postData.put("PriceRange", priceBand);
                    postData.put("StarRating", starRating);


                    Intent searchInit = new Intent(search.this, Cards.class);
                    startActivity(searchInit);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Missing Field", Toast.LENGTH_SHORT).show();
                }

            }
        });



        // Subject search
        subject = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        ArrayAdapter subjectAdapter = ArrayAdapter.createFromResource(this, R.array.Subjects, android.R.layout.select_dialog_item);
        subject.setThreshold(1);
        subject.setAdapter(subjectAdapter);
        //subject.setOnItemSelectedListener(this);
        subject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                TextView myText = (TextView) view;
                selectedSubject = view.toString();
                ++subjectPicked;
                //Toast.makeText(search.this, "You selected: " + myText.getText(), Toast.LENGTH_SHORT).show();
            }
        });


        // Create price spinner
        pricespinner = (Spinner)findViewById(R.id.priceSpinner);
        ArrayAdapter adapter=ArrayAdapter.createFromResource(this, R.array.PriceBands, android.R.layout.simple_spinner_dropdown_item);
        pricespinner.setAdapter(adapter);
        pricespinner.setOnItemSelectedListener(this);





    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i , long l){
        TextView myText = (TextView) view;
        Toast.makeText(this, "You selected: "+myText.getText(), Toast.LENGTH_SHORT).show();
        priceBand = myText.toString();
        ++pricePicked;
    }



    public void listenerForRatingBar(){
        rating = (RatingBar) findViewById(R.id.ratingBar);
        rating.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        starRating = rating;
                        Toast.makeText(search.this, "You selected: " + starRating , Toast.LENGTH_SHORT).show();
                        ++ratingPicked;
                    }
                }
        );

    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
