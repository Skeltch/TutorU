package group14.tutoru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class shiftCalendar extends AppCompatActivity {

    ListView shiftList;
    ArrayAdapter<String> shiftAdpt;
    String[] selectedShifts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Enable up button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up ListView
        // Initialize listView
        shiftList = (ListView) findViewById(R.id.shiftList);
        selectedShifts = getResources().getStringArray(R.array.shiftCalendar);
        shiftAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, selectedShifts);
        shiftList.setAdapter(shiftAdpt);
        shiftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String value = (String) shiftList.getItemAtPosition(itemPosition);
                Toast.makeText(shiftCalendar.this, value, Toast.LENGTH_SHORT).show();

                if (itemPosition >= 0) {

                    Intent launchSession = new Intent(shiftCalendar.this, MatchCreatedTutorSide.class);
                    startActivity(launchSession);
                }
            }

        });

    }

}
