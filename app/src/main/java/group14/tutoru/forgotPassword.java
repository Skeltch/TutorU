package group14.tutoru;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.HashMap;

public class forgotPassword extends AppCompatActivity implements AsyncResponse {

    boolean buttonUsername=false;
    boolean buttonEmail=false;
    boolean buttonContact=false;
    LinearLayout usernameLayout, emailLayout, contactUsLayout;
    EditText username, email, contactUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        usernameLayout = (LinearLayout) findViewById(R.id.usernameLayout);
        emailLayout = (LinearLayout) findViewById(R.id.emailLayout);
        contactUsLayout = (LinearLayout) findViewById(R.id.contactUsLayout);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        contactUs = (EditText) findViewById(R.id.contactUs);
    }
    public void usernameExpand(View view){
        if(!buttonUsername) {
            expand(usernameLayout);
            if(buttonEmail){
                collapse(emailLayout);
            }
            else if(buttonContact){
                collapse(contactUsLayout);
            }
            buttonUsername=true;
            buttonEmail=false;
            buttonContact=false;
        }
    }
    public void emailExpand(View view){
        if(!buttonEmail) {
            expand(emailLayout);
            if(buttonUsername){
                collapse(usernameLayout);
            }
            else if (buttonContact){
                collapse(contactUsLayout);
            }
            buttonUsername=false;
            buttonEmail=true;
            buttonContact=false;
        }
    }
    public void next(View view){
        HashMap postData = new HashMap();
        if(buttonUsername){
            postData.put("username",username.getText().toString());
            //Send data
        }
        else if (buttonEmail){
            postData.put("email",email.getText().toString());
        }
        else{
            postData.put("contactUs",contactUs.getText().toString());
        }
        PostResponseAsyncTask retrieve = new PostResponseAsyncTask(forgotPassword.this, postData);
        retrieve.execute("forgotPassword.php");
    }
    public void neitherExpand(View view){
        if(!buttonContact){
            expand(contactUsLayout);
            if(buttonUsername){
                collapse(usernameLayout);
            }
            else if(buttonEmail){
                collapse(emailLayout);
            }
            buttonContact=true;
            buttonUsername=false;
            buttonEmail=false;
        }
    }
    public void processFinish(String output){
        //temp
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

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
