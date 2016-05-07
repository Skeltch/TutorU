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



public class MatchList extends AppCompatActivity {


    ListView matchList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        matchList = (ListView) findViewById(R.id.shiftList);
        String[] items = getResources().getStringArray(R.array.matches);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        matchList.setAdapter(adapter);
        matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String value = (String) matchList.getItemAtPosition(itemPosition);
                Toast.makeText(MatchList.this, value, Toast.LENGTH_SHORT).show();

                if (itemPosition >= 0) {

                    Intent launchSession = new Intent(MatchList.this, MatchCreatedTutorSide.class);
                    startActivity(launchSession);
                }
            }

        });



    }

}