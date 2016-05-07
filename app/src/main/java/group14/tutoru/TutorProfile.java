package group14.tutoru;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;


public class TutorProfile extends AppCompatActivity {

    // SUBJECT LIST
    ListView subjListView;
    ArrayAdapter<String> subjAdapter;
    String[] profileSubjects = {"Calc", "Math", "Science", "English"};

    // WORK EXP LIST
    ListView workListView;
    ArrayAdapter<String> workAdapter;    String[] workExperiences = {"Rutgers Research Lab", "Rutgers Learning Center", "Private Tutoring Business"};




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Enable up button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize listView for SUBJECT
        subjListView = (ListView) findViewById(R.id.subjectList);
        setListViewHeightBasedOnChildren(subjListView);
        subjListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        subjAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, profileSubjects);
        subjListView.setAdapter(subjAdapter);

        // Initialize listView for WORK EXPERIENCE
        workListView = (ListView) findViewById(R.id.workList);
        setListViewHeightBasedOnChildren(workListView);
        workListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        workAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, workExperiences);
        workListView.setAdapter(workAdapter);



    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, RadioGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
