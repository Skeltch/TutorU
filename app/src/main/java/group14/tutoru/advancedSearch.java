package group14.tutoru;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class advancedSearch extends AppCompatActivity {

    Button dateBtn;
    Button timeBtn;
    int minute_x, hour_x;
    int year_x, month_x, day;
    static final int dialogid = 0;
    static final int tdialogid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Call picker dialogs on button click
        showDialogOnButtonClick();
        showTimeDialogOnButtonClick();

    }

    public void showDialogOnButtonClick(){
        Button dateBtn = (Button) findViewById(R.id.dateBtn);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                showDialog(dialogid);
            }
        });
    }
    public void showTimeDialogOnButtonClick(){
        Button timeBtn = (Button) findViewById(R.id.timeButton);
        timeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                showDialog(tdialogid + 1);
            }
        });
    }


@Override
protected Dialog onCreateDialog(int id){
    if (id == dialogid) {
        return new DatePickerDialog(advancedSearch.this, dpickerListener, year_x, month_x, day);
    }
    if (id == tdialogid + 1)
        return new TimePickerDialog(advancedSearch.this, tpickerListener, minute_x, hour_x, false);
    else
        return null;
}

    protected TimePickerDialog.OnTimeSetListener tpickerListener =
            new TimePickerDialog.OnTimeSetListener(){
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                    hour_x = hourOfDay;
                    minute_x = minute;
                    Toast.makeText(advancedSearch.this, hour_x + ":" + minute_x, Toast.LENGTH_SHORT).show();
                    final TextView dateDisplay = (TextView) findViewById(R.id.timeField);
                    dateDisplay.setText( hour_x + ":" + minute_x);
                }
            };

private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        year_x = year + 116;
        month_x = monthOfYear + 1;
        day = dayOfMonth;
        Toast.makeText(advancedSearch.this, month_x + "/" + day + "/" + year_x, Toast.LENGTH_SHORT).show();
        final TextView dateDisplay = (TextView) findViewById(R.id.dateField);
        dateDisplay.setText( month_x + "/" + day + "/" + year_x);
    }
};



}
