package group14.tutoru;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ShiftSelection extends AppCompatActivity implements AsyncResponse {

    // initialize variables
    int minute_from, hour_from;
    int minute_to, hour_to;
    int year_x, month_x, day;
    static final int dialogid = 0;
    static final int todialogid = 0;
    static final int fromdialogid = 0;
    boolean toORfrom = true;
    int datePicked = 0;
    int toTimePicked = 0;
    int fromTimePicked = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_parameters);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable up button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Initialize functions on create
        showFromTimeDialogOnButtonClick();
        showToTimeDialogOnButtonClick();
        showDateDialogOnButtonClick();
        listenerForSave();
        listenerForCalendar();
        listenerForcalendarView();
    }


    // Craete listener for calendar button
    public void listenerForCalendar()
    {
        Button calendarButton = (Button) findViewById(R.id.calendarBtn);
        calendarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(ShiftSelection.this, "Go Calendar Page", Toast.LENGTH_SHORT).show();
                // Finish intent here.
            }
        });
    }

    // Create listener for save button
    public void listenerForSave()
    {
        Button saveButton = (Button) findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (datePicked > 0 && toTimePicked > 0 && fromTimePicked > 0)
                {
                    Toast.makeText(ShiftSelection.this, "Shift saved!", Toast.LENGTH_LONG).show();
                    Toast.makeText(ShiftSelection.this, "You can now add another shift.", Toast.LENGTH_LONG).show();

                    // Clear text fields
                    final TextView dateDisplay = (TextView) findViewById(R.id.date);
                    dateDisplay.setText("");
                    final TextView toTimeDisplay = (TextView) findViewById(R.id.toTime);
                    toTimeDisplay.setText("");
                    final TextView fromTimeDisplay = (TextView) findViewById(R.id.fromTime);
                    fromTimeDisplay.setText("");


                    // NEED TO ADD: SEND DATA TO SERVER
                    HashMap postData = new HashMap();
                    postData.put("MinuteFrom", minute_from);
                    postData.put("HourFrom", hour_from);
                    postData.put("MinuteTo", minute_to);
                    postData.put("HourTo", hour_to);
                    postData.put("DatePicked", datePicked);


                    PostResponseAsyncTask saveShifts = new PostResponseAsyncTask(ShiftSelection.this, postData);
                    saveShifts.execute("http://192.168.1.4/app/search.php");


                }
                else
                    Toast.makeText(ShiftSelection.this, "Please select all fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // opens calendar diaglog when date button is picked
    public void showDateDialogOnButtonClick()
    {
        Button dateBtn = (Button) findViewById(R.id.dateBtn);

        dateBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View V)
            {
                showDialog(dialogid);
            }
        });
    }

    // opens fromTime diaglog when time button is picked
    public void showFromTimeDialogOnButtonClick()
    {
        Button timeBtn = (Button) findViewById(R.id.fromBtn);
        timeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View V)
            {
                showDialog(fromdialogid + 1);
            }
        });
    }

    // opens fromTime diaglog when time button is picked
    public void showToTimeDialogOnButtonClick()
    {
        Button timeBtn = (Button) findViewById(R.id.toBtn);
        timeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View V)
            {
                showDialog(todialogid + 2);
            }
        });
    }

    // determine which dialogid is being picjed to open corrent dialog type
    @Override
    protected Dialog onCreateDialog(int id){
        if (id == dialogid) {
            return new DatePickerDialog(ShiftSelection.this, dpickerListener, year_x, month_x, day);
        }
        if (id == fromdialogid + 1)
        {
            toORfrom = true;
            return new TimePickerDialog(ShiftSelection.this, tpickerListener, minute_from, hour_from, false);
        }
        if (id == todialogid + 2)
        {
            toORfrom = false;
            return new TimePickerDialog(ShiftSelection.this, tpickerListener, minute_to, hour_to, false);
        }
        else
            return null;
    }


    public void listenerForcalendarView() {
        Button calBtn = (Button) findViewById(R.id.calendarBtn);
        calBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shiftSelector = new Intent(ShiftSelection.this, shiftCalendar.class);
                startActivity(shiftSelector);
            }
        });
    }
    // time picker listener for initialization
    protected TimePickerDialog.OnTimeSetListener tpickerListener =
            new TimePickerDialog.OnTimeSetListener()
            {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                {

                    if (toORfrom == true)
                    {
                        hour_from = hourOfDay;
                        minute_from = minute;
                        Toast.makeText(ShiftSelection.this, "fromDialog", Toast.LENGTH_SHORT).show();
                        final TextView dateDisplay = (TextView) findViewById(R.id.fromTime);
                        dateDisplay.setText(hour_from + ":" + minute_from);
                        fromTimePicked++;
                    }
                    else if (toORfrom == false)
                    {
                        hour_to = hourOfDay;
                        minute_to = minute;
                        Toast.makeText(ShiftSelection.this, hour_to + ":" + minute_to, Toast.LENGTH_SHORT).show();
                        final TextView dateDisplay = (TextView) findViewById(R.id.toTime);
                        dateDisplay.setText(hour_to + ":" + minute_to);
                        toTimePicked++;
                    }
                }
            };

    // date picker listener for initialization
    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            year_x = year + 116;
            month_x = monthOfYear + 1;
            day = dayOfMonth;
            Toast.makeText(ShiftSelection.this, month_x + "/" + day + "/" + year_x, Toast.LENGTH_SHORT).show();
            final TextView dateDisplay = (TextView) findViewById(R.id.date);
            dateDisplay.setText( month_x + "/" + day + "/" + year_x);
            datePicked++;
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_work_parameters, menu);
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

                Toast.makeText(this, "Shift Saved", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ShiftSelection.this, TutorHomepage.class);
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
