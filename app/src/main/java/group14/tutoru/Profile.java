package group14.tutoru;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Profile extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        PostResponseAsyncTask login = new PostResponseAsyncTask(Profile.this);
        login.execute("http://192.168.1.4/app/profile.php");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Uri selectedImage = data.getData();
            try {
                switch (requestCode) {
                    case 1:
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                }
            }
            catch(Exception e){
                Log.e("Error", "Exception in onActivityResult : " + e.getMessage());
            }
        }
        ImageView img = (ImageView) findViewById(R.id.profile);
        img.setImageBitmap(bitmap);
    }
    @Override
    public void processFinish(String output) {

    }
}
